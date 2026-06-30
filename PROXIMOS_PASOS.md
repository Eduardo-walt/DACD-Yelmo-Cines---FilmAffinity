# Próximos Pasos - Sprint 1

## Estado Actual

✅ **Completado**:
- Estructura Maven multimódulo creada
- POM padre configurado con dependencias
- Dos módulos independientes (yelmo-module y filmaffinity-module)
- Carpetas base creadas
- README y documentación de estructura

---

## Paso 1: Definir los Modelos de Datos (Models)

**Objetivo**: Crear las clases de modelo que representarán internamente los datos que capturamos.

### Módulo Yelmo - Crear estos archivos:

```
src/main/java/com/cinema/yelmo/model/
├── Cinema.java          (representa un cine)
├── Movie.java           (representa una película)
└── Showtime.java        (representa una función/horario)
```

**Ejemplo de Cinema.java**:
```java
public class Cinema {
    private int id;
    private String name;
    private String location;
    private LocalDateTime capturedAt;
    
    // Constructor, getters, setters...
}
```

**Qué incluir en cada modelo**:
- Atributos que representan los datos
- Constructor (al menos uno con todos los parámetros)
- Getters y Setters
- Método `toString()` para debugging
- Campo `capturedAt` (LocalDateTime) para el histórico

### Módulo FilmAffinity - Crear estos archivos:

```
src/main/java/com/cinema/filmaffinity/model/
├── Film.java            (representa una película)
├── Rating.java          (representa una valoración)
└── FilmReview.java      (representa una reseña)
```

**Qué incluir**: Igual que en Yelmo (atributos, constructores, getters, setters, capturedAt).

---

## Paso 2: Crear las Interfaces (Contracts)

**Objetivo**: Definir contratos que especifiquen qué debe hacer cada componente.

### Módulo Yelmo - Crear estos archivos:

```
src/main/java/com/cinema/yelmo/feeder/
└── ShowtimeFeeder.java (interfaz)

src/main/java/com/cinema/yelmo/serializer/
└── ShowtimeSerializer.java (interfaz)
```

**ShowtimeFeeder.java**:
```java
public interface ShowtimeFeeder {
    /**
     * Obtiene datos de cines, películas y horarios desde la fuente externa
     * @return Lista de Showtime obtenidos
     */
    List<Showtime> fetchShowtimes();
}
```

**ShowtimeSerializer.java**:
```java
public interface ShowtimeSerializer {
    /**
     * Persiste un Showtime en la BD
     * @param showtime el showtime a guardar
     */
    void serialize(Showtime showtime);
    
    /**
     * Persiste una lista de Showtimes
     * @param showtimes lista de showtimes a guardar
     */
    void serializeAll(List<Showtime> showtimes);
}
```

### Módulo FilmAffinity - Crear estos archivos:

```
src/main/java/com/cinema/filmaffinity/feeder/
└── ReviewFeeder.java (interfaz)

src/main/java/com/cinema/filmaffinity/serializer/
└── ReviewSerializer.java (interfaz)
```

Similar a ShowtimeFeeder y ShowtimeSerializer, pero para reviews.

---

## Paso 3: Crear las Implementaciones Concretas (Feeders)

**Objetivo**: Implementar el código que realmente obtiene datos.

### Módulo Yelmo:

```
src/main/java/com/cinema/yelmo/feeder/
└── YelmoShowtimeFeeder.java (implementa ShowtimeFeeder)
```

**YelmoShowtimeFeeder.java** (versión básica, sin conectar a fuente real aún):
```java
public class YelmoShowtimeFeeder implements ShowtimeFeeder {
    
    public YelmoShowtimeFeeder() {
        // Inicializar cliente HTTP, credenciales, etc.
    }
    
    @Override
    public List<Showtime> fetchShowtimes() {
        // TODO: Consumir API o hacer scraping de Yelmo
        // Por ahora retornar lista vacía o datos de prueba
        return new ArrayList<>();
    }
}
```

### Módulo FilmAffinity:

```
src/main/java/com/cinema/filmaffinity/feeder/
└── FilmAffinityReviewFeeder.java (implementa ReviewFeeder)
```

---

## Paso 4: Crear los Transformadores (Transformers)

**Objetivo**: Convertir datos crudos (JSON, HTML) a objetos del modelo interno.

### Módulo Yelmo:

```
src/main/java/com/cinema/yelmo/transformer/
└── ShowtimeTransformer.java
```

**ShowtimeTransformer.java**:
```java
public class ShowtimeTransformer {
    
    /**
     * Transforma un JSON (como String o JsonElement) en objetos Showtime
     */
    public List<Showtime> transform(String jsonResponse) {
        // Parsear JSON
        // Extraer solo los campos que nos interesan
        // Crear objetos Showtime
        // Retornar lista
        return new ArrayList<>();
    }
}
```

### Módulo FilmAffinity:

```
src/main/java/com/cinema/filmaffinity/transformer/
└── ReviewTransformer.java
```

---

## Paso 5: Crear los Serializers (Persistencia en BD)

**Objetivo**: Guardar datos en SQLite de forma incremental.

### Módulo Yelmo:

```
src/main/java/com/cinema/yelmo/serializer/
└── DatabaseShowtimeSerializer.java (implementa ShowtimeSerializer)
```

**DatabaseShowtimeSerializer.java**:
```java
public class DatabaseShowtimeSerializer implements ShowtimeSerializer {
    
    private static final String DB_PATH = "yelmo.db";
    
    public DatabaseShowtimeSerializer() {
        initializeDatabase();
    }
    
    private void initializeDatabase() {
        // Crear tablas si no existen (CINEMAS, MOVIES, SHOWTIMES)
    }
    
    @Override
    public void serialize(Showtime showtime) {
        // Insertar en SHOWTIMES
    }
    
    @Override
    public void serializeAll(List<Showtime> showtimes) {
        // Insertar todos en SHOWTIMES
    }
}
```

### Módulo FilmAffinity:

```
src/main/java/com/cinema/filmaffinity/serializer/
└── DatabaseReviewSerializer.java (implementa ReviewSerializer)
```

---

## Paso 6: Crear el Controlador (Controller)

**Objetivo**: Orquestar el flujo: obtener → transformar → persistir.

### Módulo Yelmo:

```
src/main/java/com/cinema/yelmo/control/
└── YelmoController.java
```

**YelmoController.java**:
```java
public class YelmoController {
    
    private ShowtimeFeeder feeder;
    private ShowtimeSerializer serializer;
    private ShowtimeTransformer transformer;
    
    public YelmoController(ShowtimeFeeder feeder, 
                          ShowtimeSerializer serializer,
                          ShowtimeTransformer transformer) {
        this.feeder = feeder;
        this.serializer = serializer;
        this.transformer = transformer;
    }
    
    public void run() {
        // 1. Obtener datos
        List<Showtime> showtimes = feeder.fetchShowtimes();
        
        // 2. Transformar (si es necesario)
        List<Showtime> transformed = transformer.transform(showtimes);
        
        // 3. Persistir
        serializer.serializeAll(transformed);
        
        System.out.println("Datos guardados exitosamente");
    }
}
```

### Módulo FilmAffinity:

```
src/main/java/com/cinema/filmaffinity/control/
└── FilmAffinityController.java
```

---

## Paso 7: Crear el Main (Punto de Entrada)

**Objetivo**: Punto de entrada que inicializa todo y ejecuta el flujo.

### Módulo Yelmo:

```
src/main/java/com/cinema/yelmo/
└── Main.java
```

**Main.java**:
```java
public class Main {
    public static void main(String[] args) {
        // 1. Crear las instancias
        ShowtimeFeeder feeder = new YelmoShowtimeFeeder();
        ShowtimeSerializer serializer = new DatabaseShowtimeSerializer();
        ShowtimeTransformer transformer = new ShowtimeTransformer();
        
        // 2. Crear el controlador
        YelmoController controller = new YelmoController(feeder, serializer, transformer);
        
        // 3. Ejecutar
        controller.run();
    }
}
```

### Módulo FilmAffinity:

```
src/main/java/com/cinema/filmaffinity/
└── Main.java
```

---

## Resumen del Orden de Implementación

```
1. Modelos (Cinema, Movie, Showtime, etc.)
   ↓
2. Interfaces (Feeders, Serializers)
   ↓
3. Transformadores (ShowtimeTransformer, ReviewTransformer)
   ↓
4. Serializers (DatabaseShowtimeSerializer, DatabaseReviewSerializer)
   ↓
5. Feeders (YelmoShowtimeFeeder, FilmAffinityReviewFeeder)
   ↓
6. Controladores (YelmoController, FilmAffinityController)
   ↓
7. Main (punto de entrada para cada módulo)
```

---

## Archivo de Propiedades (Configuración)

Crear en cada módulo:
```
src/main/resources/config.properties
```

Con configuraciones como:
```properties
# Yelmo Module
yelmo.api.url=https://api.yelmo.es
yelmo.db.path=yelmo.db

# FilmAffinity Module
filmaffinity.api.url=https://api.filmaffinity.com
filmaffinity.db.path=filmaffinity.db
```

---

## Checklist antes de empezar a programar

- [ ] Estructura Maven montada ✅
- [ ] POM padre y módulos configurados ✅
- [ ] Dependencias definidas ✅
- [ ] ¿Necesitas que agregue algo a la estructura?
- [ ] ¿Necesitas que aclaremos algo antes de programar?

---

## Siguientes acciones

**Cuando digas "adelante", comenzaremos con**:

1. Crear los modelos de datos
2. Crear las interfaces
3. Implementar paso a paso

**Avísame cuando estés listo para programar.**
