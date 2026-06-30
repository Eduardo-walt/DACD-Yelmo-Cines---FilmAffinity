# Cinema Data Integration — Yelmo Cines & FilmAffinity

Proyecto multimódulo en Java 21 para la asignatura *Desarrollo de Aplicaciones para Ciencia de Datos*. Grado en Ciencia e Ingeniería de Datos — ULPGC.

**Equipo:** [PON AQUÍ TU NOMBRE COMPLETO Y EL DE TU COMPAÑERO/A]

## 1. Descripción y propuesta de valor

El proyecto integra dos fuentes de datos independientes — cartelera de cines (Yelmo) y valoraciones de películas (FilmAffinity/TMDB) — en una arquitectura de eventos en tiempo real, permitiendo a un usuario final consultar de forma unificada qué películas tienen sesiones disponibles y cuál es su valoración crítica, sin tener que consultar dos fuentes por separado.

**Propuesta de valor:** ayudar al usuario a decidir qué película ver combinando disponibilidad de sesiones (horarios, cines, formato) con la calidad percibida de la película (valoraciones y reseñas), centralizando ambas señales en un único punto de consulta.

## 2. Arquitectura del sistema
┌─────────────────┐     ┌──────────────────────┐
│  Yelmo Module    │     │ FilmAffinity Module   │
│  (Feeder/Pub)    │     │  (Feeder/Pub)          │
│  scraping cartelera│   │  TMDB API ratings      │
└────────┬─────────┘     └──────────┬─────────────┘
│ topic: Movie             │ topic: Review
▼                          ▼
┌─────────────────────────────────────┐
│         ActiveMQ (broker)            │
└────────────────┬──────────────────┬─┘
│ durable sub      │ durable sub
▼                  ▼
┌────────────────────────────────┐
│     Event Store Builder         │
│  (subscriber, persiste en disco)│
└────────────────┬────────────────┘
▼
eventstore/{topic}/{ss}/{YYYYMMDD}.events
│
▼
┌─────────────────────────┐
│     Business Unit         │
│ (datamart en memoria +    │
│  consumo tiempo real +    │
│  lectura histórico)       │
└────────────┬───────────────┘
▼
CLI (consultas)

**Flujo de datos:**
1. Los feeders (Yelmo, FilmAffinity) capturan datos de sus fuentes externas, los persisten en su propia base SQLite, y publican un evento por cada registro al broker ActiveMQ.
2. El Event Store Builder consume de forma durable ambos topics y los persiste de forma append-only en ficheros `.events` (NDJSON), organizados por topic, fuente y fecha.
3. El Business Unit se suscribe en tiempo real a los mismos topics y, además, carga el histórico desde el event store al arrancar, manteniendo un datamart en memoria.
4. El usuario consulta ese datamart a través de una CLI.

## 3. Arquitectura de aplicación (módulos)
cinema-data-integration/
├── pom.xml                       (padre, gestiona el multimódulo)
├── yelmo-module/                 (Sprint 1 + 2: captura y publicación)
├── filmaffinity-module/          (Sprint 1 + 2: captura y publicación)
├── event-store-module/           (Sprint 2: persistencia de eventos)
├── business-unit-module/         (Sprint 3: explotación de datos)
└── eventstore/                   (datos generados, NDJSON)

### Componentes por módulo (Yelmo / FilmAffinity)

| Componente | Responsabilidad |
|---|---|
| **Feeder** | Obtiene datos crudos de la fuente externa (API/scraping) |
| **Publisher** | Serializa el evento y lo publica en ActiveMQ |
| **Transformer** | Convierte datos externos al modelo interno |
| **Serializer** | Persiste en SQLite |
| **Controller** | Orquesta el flujo: obtener → transformar → persistir → publicar |
| **Model** | Representación interna de los datos, desacoplada de la fuente |

### Event Store Builder
- `EventMessage`: modelo del evento (`ts`, `ss`, `topic`, `payload`).
- `EventStoreBuilder`: subscriber durable a los topics `Movie` y `Review`.
- `FileSystemEventStorage`: estrategia de persistencia en disco, calcula la fecha del fichero a partir del `ts` del evento (no de la hora de proceso).

### Business Unit
- Carga eventos históricos del event store al arrancar (reconstrucción del datamart).
- Se suscribe en tiempo real (durable) a los mismos topics.
- Mantiene un datamart en memoria (`Map<String, List<JsonObject>>` por fuente y topic).
- Expone una CLI para consultas.

## 4. Justificación de diseño

**Elección de APIs/fuentes:** Yelmo (scraping, contenido dinámico de cartelera) y TMDB (API, valoraciones de películas en cartelera) se eligieron por su variabilidad temporal y porque comparten la entidad "película", lo que permite cruzarlas de forma natural en el Sprint 3.

**Diseño del datamart:** se optó por una estructura en memoria (`Map` indexado por fuente, `ss`) en lugar de una base de datos adicional, porque:
- El volumen de datos por ejecución es manejable en memoria (miles de eventos).
- Permite reconstrucción rápida al arrancar (se relee el event store).
- Evita duplicar la persistencia que ya ofrece el event store (que actúa como fuente de verdad / *source of truth* para la reconstrucción).
- Las consultas que ofrece la CLI son agregaciones simples (conteos, listados), que no requieren índices persistentes.

## 5. Principios y patrones de diseño aplicados

- **Separación de responsabilidades (SRP):** Feeder, Transformer, Serializer y Publisher son clases independientes, cada una con una única razón de cambio.
- **Programación contra interfaces:** `ShowtimeFeeder`, `ReviewFeeder`, `EventStorage` son interfaces que desacoplan la implementación concreta del resto del sistema.
- **Publisher/Subscriber:** desacopla productores (feeders) de consumidores (Event Store Builder, Business Unit) a través de ActiveMQ; ninguno conoce al otro directamente.
- **Suscripción durable:** garantiza que ni el Event Store Builder ni el Business Unit pierdan eventos si se detienen temporalmente.
- **Patrón Strategy (implícito):** `FileSystemEventStorage` implementa `EventStorage`, permitiendo cambiar la estrategia de persistencia sin tocar `EventStoreBuilder`.
- **Idempotencia temporal:** el nombre de fichero `.events` se calcula a partir del `ts` del evento, no de la hora de procesamiento, evitando inconsistencias si se reprocesan eventos antiguos.

## 6. Tecnologías

- Java 21, Maven (multimódulo)
- SQLite 3 (persistencia por feeder)
- ActiveMQ 5.15.12 (broker de mensajería, JMS)
- Gson 2.8.9+ (serialización JSON)
- JSoup (scraping), OkHttp (consumo de API)

## 7. Requisitos previos

- Java 21 instalado.
- Maven 3.6+.
- ActiveMQ corriendo en `tcp://localhost:61616` (consola admin en `http://localhost:8161/admin`, usuario/contraseña por defecto `admin`/`admin`).

## 8. Compilación

```bash
# Proyecto completo
mvn clean install

# Un módulo específico
mvn clean install -pl yelmo-module
mvn clean install -pl filmaffinity-module
mvn clean install -pl event-store-module
mvn clean install -pl business-unit-module
```

## 9. Ejecución (orden obligatorio)

```bash
# 1. Arrancar ActiveMQ (externo al proyecto)

# 2. Arrancar el Event Store Builder (dejar corriendo)
java -cp event-store-module/target/classes com.cinema.eventstore.EventStoreBuilder

# 3. Ejecutar los feeders (capturan, persisten en SQLite y publican eventos)
java -cp yelmo-module/target/classes com.cinema.yelmo.Main
java -cp filmaffinity-module/target/classes com.cinema.filmaffinity.Main

# 4. Ejecutar la Business Unit (carga histórico + tiempo real + CLI)
java -cp business-unit-module/target/classes com.cinema.businessunit.BusinessUnit
```

## 10. Ejemplos de uso (CLI de Business Unit)

=== BUSINESS UNIT - CLI ===
Comandos: stats | reviews | sesiones | salir

stats
Sesiones en datamart: 1443
Reseñas en datamart: 20


reviews
Fuente: filmaffinity-feeder (20 reseñas)
{"movieId":...,"rating":...,"comment":"..."}


sesiones
Fuente: yelmo-feeder (1443 sesiones)
{"cinema":"premium-alisios","movie":"...","time":"..."}


salir
Cerrando business-unit...

## 11. Estructura del Event Store (datos de ejemplo)

eventstore/
├── Movie/
│   └── yelmo-feeder/
│       └── 20260630.events
└── Review/
└── filmaffinity-feeder/
└── 20260630.events

Cada línea es un evento JSON independiente (NDJSON):
```json
{"ts":1782900000,"ss":"yelmo-feeder","topic":"Movie","payload":{"cinema":"premium-alisios","movie":"Dune: Part Two","time":"2026-06-30T18:30:00"}}
```

## 12. Bases de datos (Sprint 1)

**yelmo.db**

CINEMAS (id, name, location, captured_at)
MOVIES (id, title, genre, duration, captured_at)
SHOWTIMES (id, cinema_id, movie_id, start_time, language, format, price, captured_at)

**filmaffinity.db**

MOVIES (id, title, year, genre, captured_at)
RATINGS (id, movie_id, average_rating, number_votes, captured_at)
REVIEWS (id, movie_id, user_review, review_date, captured_at)

## 13. Estado del proyecto

- ✅ Sprint 1: captura y persistencia independiente — completo.
- ✅ Sprint 2: arquitectura Publisher/Subscriber con ActiveMQ y Event Store — completo, verificado end-to-end.
- ✅ Sprint 3: Business Unit con datamart, consumo en tiempo real e histórico, CLI — completo (versión mínima funcional).

- 
