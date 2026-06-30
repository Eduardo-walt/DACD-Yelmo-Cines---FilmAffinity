# Cinema Data Integration - Sprint 1

## Descripción del Proyecto

Proyecto multimódulo en Java que recopila información de dos fuentes de datos independientes:

1. **Módulo Yelmo**: Cartelera, horarios y precios de cines
2. **Módulo FilmAffinity**: Valoraciones, reseñas y ratings de películas

Cada módulo funciona de manera autónoma, capturando datos completos e históricos sin dependencias cruzadas.

## Estructura del Proyecto

```
cinema-data-integration/
├── pom.xml (padre - gestiona el multimódulo)
├── yelmo-module/ (módulo independiente para Yelmo)
│   └── src/main/java/com/cinema/yelmo/
│       ├── Main.java
│       ├── control/YelmoController.java
│       ├── feeder/... (obtiene datos)
│       ├── transformer/... (transforma)
│       ├── serializer/... (persiste)
│       └── model/... (objetos internos)
│
└── filmaffinity-module/ (módulo independiente para FilmAffinity)
    └── src/main/java/com/cinema/filmaffinity/
        ├── Main.java
        ├── control/FilmAffinityController.java
        ├── feeder/... (obtiene datos)
        ├── transformer/... (transforma)
        ├── serializer/... (persiste)
        └── model/... (objetos internos)
```

## Tecnologías

- **Java**: 11+
- **Build**: Maven 3.6+
- **BD**: SQLite 3
- **Parsing**: Gson (JSON), JSoup (HTML/Scraping)
- **HTTP**: OkHttp 3
- **Testing**: JUnit 4

## Componentes de cada módulo

### Feeder
- **Interfaz**: Define contrato para obtener datos
- **Implementación**: Consume API o realiza scraping
- **Responsabilidad**: Solo obtener datos crudos

### Transformer
- **Responsabilidad**: Convertir datos externos a objetos del modelo interno
- **Ejemplo**: JSON → Weather/Movie

### Serializer
- **Interfaz**: Define contrato para persistir datos
- **Implementación**: Guarda en SQLite
- **Responsabilidad**: Solo persistir en BD

### Controller
- **Responsabilidad**: Orquestar el flujo
- **Patrón**: Obtiene → Transforma → Persiste

### Model
- **Responsabilidad**: Representación interna de los datos
- **Objetivo**: Desacoplar de API externa

## Bases de datos

### yelmo.db
```sql
CINEMAS (id, name, location, captured_at)
MOVIES (id, title, genre, duration, captured_at)
SHOWTIMES (id, cinema_id, movie_id, start_time, language, format, price, captured_at)
```

### filmaffinity.db
```sql
MOVIES (id, title, year, genre, captured_at)
RATINGS (id, movie_id, average_rating, number_votes, captured_at)
REVIEWS (id, movie_id, user_review, review_date, captured_at)
```

## Ejecución

### Compilar todo el proyecto
```bash
mvn clean install
```

### Compilar un módulo específico
```bash
mvn clean install -pl yelmo-module
mvn clean install -pl filmaffinity-module
```

### Ejecutar un módulo
```bash
java -cp yelmo-module/target/yelmo-module-1.0-SNAPSHOT.jar com.cinema.yelmo.Main
java -cp filmaffinity-module/target/filmaffinity-module-1.0-SNAPSHOT.jar com.cinema.filmaffinity.Main
```

## Principios de Diseño

✓ **Independencia**: Cada módulo es autónomo
✓ **Separación de Responsabilidades**: Feeder, Transformer, Serializer bien diferenciados
✓ **Abstracción**: Interfaces para cambiar implementaciones
✓ **Sin Acoplamiento**: No comparten datos ni dependencias
✓ **Histórico**: Cada captura incluye timestamp `captured_at`
✓ **Escalabilidad**: Preparado para futuras integraciones

## Próximos Pasos

1. Definir modelos de datos (Model)
2. Crear interfaces (Feeder, Serializer)
3. Implementar Feeders (consumo de datos)
4. Implementar Serializers (persistencia)
5. Implementar Transformers (normalización)
6. Crear Controllers (orquestación)
7. Crear Main (punto de entrada)
8. Testing
