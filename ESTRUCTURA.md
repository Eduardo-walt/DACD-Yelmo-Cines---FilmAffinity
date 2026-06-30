ESTRUCTURA DEL PROYECTO MAVEN MULTIMÓDULO
==========================================

cinema-data-integration/
│
├── pom.xml (PADRE - gestiona todo el proyecto)
│   ├── Define versión: 1.0-SNAPSHOT
│   ├── Packaging: pom (multimódulo)
│   ├── Dependencias comunes:
│   │   ├── SQLite JDBC 3.44.0.0
│   │   ├── Gson 2.10.1 (JSON)
│   │   ├── JSoup 1.15.3 (Web scraping)
│   │   ├── OkHttp 4.11.0 (HTTP client)
│   │   └── JUnit 4.13.2 (Testing)
│   └── Modules:
│       ├── yelmo-module
│       └── filmaffinity-module
│
├── yelmo-module/
│   ├── pom.xml (hereda del padre)
│   │   └── mainClass: com.cinema.yelmo.Main
│   │
│   └── src/
│       ├── main/
│       │   ├── java/com/cinema/yelmo/
│       │   │   ├── Main.java (punto de entrada)
│       │   │   ├── feeder/ (responsable de consumir datos)
│       │   │   │   ├── ShowtimeFeeder.java (interfaz)
│       │   │   │   └── YelmoShowtimeFeeder.java (implementación)
│       │   │   ├── serializer/ (responsable de persistir)
│       │   │   │   ├── ShowtimeSerializer.java (interfaz)
│       │   │   │   └── DatabaseShowtimeSerializer.java (implementación)
│       │   │   ├── transformer/ (transforma datos crudos)
│       │   │   │   └── ShowtimeTransformer.java
│       │   │   ├── model/ (modelos de dominio)
│       │   │   │   ├── Cinema.java
│       │   │   │   ├── Showtime.java
│       │   │   │   └── Movie.java
│       │   │   └── control/ (orquesta el flujo)
│       │   │       └── YelmoController.java
│       │   │
│       │   └── resources/
│       │       └── (ficheros de configuración)
│       │
│       └── test/
│           └── java/com/cinema/yelmo/
│               └── (tests unitarios)
│
├── filmaffinity-module/
│   ├── pom.xml (hereda del padre)
│   │   └── mainClass: com.cinema.filmaffinity.Main
│   │
│   └── src/
│       ├── main/
│       │   ├── java/com/cinema/filmaffinity/
│       │   │   ├── Main.java (punto de entrada)
│       │   │   ├── feeder/ (responsable de consumir datos)
│       │   │   │   ├── ReviewFeeder.java (interfaz)
│       │   │   │   └── FilmAffinityReviewFeeder.java (implementación)
│       │   │   ├── serializer/ (responsable de persistir)
│       │   │   │   ├── ReviewSerializer.java (interfaz)
│       │   │   │   └── DatabaseReviewSerializer.java (implementación)
│       │   │   ├── transformer/ (transforma datos crudos)
│       │   │   │   └── ReviewTransformer.java
│       │   │   ├── model/ (modelos de dominio)
│       │   │   │   ├── Film.java
│       │   │   │   ├── FilmReview.java
│       │   │   │   └── Rating.java
│       │   │   └── control/ (orquesta el flujo)
│       │   │       └── FilmAffinityController.java
│       │   │
│       │   └── resources/
│       │       └── (ficheros de configuración)
│       │
│       └── test/
│           └── java/com/cinema/filmaffinity/
│               └── (tests unitarios)


FLUJO DE EJECUCIÓN (en cada módulo)
====================================

Main
  ↓
YelmoController / FilmAffinityController (orquesta)
  ↓
ShowtimeFeeder / ReviewFeeder (obtiene datos)
  ↓
ShowtimeTransformer / ReviewTransformer (convierte a objetos internos)
  ↓
ShowtimeSerializer / ReviewSerializer (persiste en BD)
  ↓
SQLite (yelmo.db / filmaffinity.db)


CARACTERÍSTICAS DEL PROYECTO
==============================

✓ Multimódulo: Dos módulos independientes
✓ Packaging: JAR ejecutable (cada módulo)
✓ Java: 11 (compatible)
✓ Dependencias centralizadas en POM padre
✓ Separación de responsabilidades clara
✓ Preparado para testing
✓ Escalable: fácil añadir más módulos
