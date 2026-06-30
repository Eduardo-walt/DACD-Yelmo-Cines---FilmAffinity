# Estructura del Proyecto en Visual Studio Code

## 📁 Ubicación en tu Sistema

```
C:\Users\[TuUsuario]\
└── DACD\                          ← Carpeta principal de la materia
    └── cinema-data-integration\   ← Proyecto (aquí trabajas en VS Code)
        ├── pom.xml               ← Padre
        ├── README.md
        ├── ESTRUCTURA.md
        ├── PROXIMOS_PASOS.md
        ├── RESUMEN_EJECUTIVO.md
        │
        ├── yelmo-module\          ← Módulo 1
        │   ├── pom.xml
        │   └── src\
        │       ├── main\
        │       │   ├── java\
        │       │   │   └── com\cinema\yelmo\
        │       │   │       ├── Main.java
        │       │   │       ├── feeder\
        │       │   │       │   ├── ShowtimeFeeder.java
        │       │   │       │   └── YelmoShowtimeFeeder.java
        │       │   │       ├── serializer\
        │       │   │       │   ├── ShowtimeSerializer.java
        │       │   │       │   └── DatabaseShowtimeSerializer.java
        │       │   │       ├── transformer\
        │       │   │       │   └── ShowtimeTransformer.java
        │       │   │       ├── model\
        │       │   │       │   ├── Cinema.java
        │       │   │       │   ├── Movie.java
        │       │   │       │   └── Showtime.java
        │       │   │       └── control\
        │       │   │           └── YelmoController.java
        │       │   └── resources\
        │       │       └── config.properties
        │       └── test\
        │           └── java\com\cinema\yelmo\
        │
        └── filmaffinity-module\   ← Módulo 2
            ├── pom.xml
            └── src\
                ├── main\
                │   ├── java\
                │   │   └── com\cinema\filmaffinity\
                │   │       ├── Main.java
                │   │       ├── feeder\
                │   │       │   ├── ReviewFeeder.java
                │   │       │   └── FilmAffinityReviewFeeder.java
                │   │       ├── serializer\
                │   │       │   ├── ReviewSerializer.java
                │   │       │   └── DatabaseReviewSerializer.java
                │   │       ├── transformer\
                │   │       │   └── ReviewTransformer.java
                │   │       ├── model\
                │   │       │   ├── Film.java
                │   │       │   ├── FilmReview.java
                │   │       │   └── Rating.java
                │   │       └── control\
                │   │           └── FilmAffinityController.java
                │   └── resources\
                │       └── config.properties
                └── test\
                    └── java\com\cinema\filmaffinity\
```

---

## 🔧 Cómo abrir el proyecto en VS Code

### Paso 1: Abrir VS Code
1. Abre Visual Studio Code
2. `File` → `Open Folder`
3. Navega a: `C:\Users\[TuUsuario]\DACD\cinema-data-integration`
4. Click `Select Folder`

### Paso 2: Confiar en el Workspace
VS Code te pedirá que confíes en el workspace. Click en "Trust" o "Yes".

### Paso 3: Instalaciones Necesarias

Deberías ver extensiones recomendadas. Instala:

- **Extension Pack for Java** (Microsoft)
  - Incluye Language Support for Java, Debugger for Java, Test Runner for Java
  
- **Maven for Java** (Microsoft)

- **Project Manager for Java** (Microsoft) - Opcional pero recomendado

Puedes instalarlas desde la pestaña `Extensions` (Ctrl+Shift+X).

---

## 📂 Cómo se ve en VS Code (Explorer)

Cuando abras la carpeta, verás algo así en el Explorer (izquierda):

```
CINEMA-DATA-INTEGRATION
├── 📄 pom.xml
├── 📄 README.md
├── 📄 ESTRUCTURA.md
├── 📄 PROXIMOS_PASOS.md
├── 📄 RESUMEN_EJECUTIVO.md
│
├── 📁 yelmo-module
│   ├── 📄 pom.xml
│   ├── 📁 src
│   │   ├── 📁 main
│   │   │   ├── 📁 java
│   │   │   │   └── 📁 com
│   │   │   │       └── 📁 cinema
│   │   │   │           └── 📁 yelmo
│   │   │   │               ├── 📄 Main.java
│   │   │   │               ├── 📁 control
│   │   │   │               ├── 📁 feeder
│   │   │   │               ├── 📁 model
│   │   │   │               ├── 📁 serializer
│   │   │   │               └── 📁 transformer
│   │   │   └── 📁 resources
│   │   │       └── 📄 config.properties
│   │   └── 📁 test
│   │       └── 📁 java
│   └── 📁 target (se crea al compilar)
│       ├── 📁 classes
│       └── 📁 lib
│
└── 📁 filmaffinity-module
    ├── 📄 pom.xml
    ├── 📁 src
    │   ├── 📁 main
    │   │   ├── 📁 java
    │   │   │   └── 📁 com
    │   │   │       └── 📁 cinema
    │   │   │           └── 📁 filmaffinity
    │   │   │               ├── 📄 Main.java
    │   │   │               ├── 📁 control
    │   │   │               ├── 📁 feeder
    │   │   │               ├── 📁 model
    │   │   │               ├── 📁 serializer
    │   │   │               └── 📁 transformer
    │   │   └── 📁 resources
    │   │       └── 📄 config.properties
    │   └── 📁 test
    │       └── 📁 java
    └── 📁 target (se crea al compilar)
        ├── 📁 classes
        └── 📁 lib
```

---

## ⚙️ Configuración de VS Code para este Proyecto

### Crear `.vscode/settings.json`

En la raíz del proyecto (cinema-data-integration), crea una carpeta `.vscode` y dentro un archivo `settings.json`:

```json
{
    "java.project.sourcePaths": [
        "yelmo-module/src/main/java",
        "filmaffinity-module/src/main/java"
    ],
    "java.project.outputPath": "target",
    "java.configuration.runtimes": [
        {
            "name": "JavaSE-11",
            "path": "C:\\Program Files\\Java\\jdk-11.0.x",
            "default": true
        }
    ],
    "maven.executable.preferMavenFromPath": true,
    "editor.formatOnSave": true,
    "editor.defaultFormatter": "redhat.java",
    "[java]": {
        "editor.defaultFormatter": "redhat.java",
        "editor.formatOnSave": true
    }
}
```

### Crear `.vscode/launch.json` (opcional, para debugging)

```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch Yelmo",
            "request": "launch",
            "mainClass": "com.cinema.yelmo.Main",
            "projectName": "yelmo-module",
            "cwd": "${workspaceFolder}"
        },
        {
            "type": "java",
            "name": "Launch FilmAffinity",
            "request": "launch",
            "mainClass": "com.cinema.filmaffinity.Main",
            "projectName": "filmaffinity-module",
            "cwd": "${workspaceFolder}"
        }
    ]
}
```

---

## 🚀 Comandos Útiles en VS Code

### Terminal (Ctrl+`)

Una vez abras la terminal en VS Code, estás dentro de `cinema-data-integration/`:

```bash
# Ver la estructura actual
tree /F

# Compilar TODO el proyecto
mvn clean install

# Compilar solo Yelmo
mvn clean install -pl yelmo-module

# Compilar solo FilmAffinity
mvn clean install -pl filmaffinity-module

# Ejecutar Yelmo
java -cp yelmo-module\target\yelmo-module-1.0-SNAPSHOT.jar com.cinema.yelmo.Main

# Ejecutar FilmAffinity
java -cp filmaffinity-module\target\filmaffinity-module-1.0-SNAPSHOT.jar com.cinema.filmaffinity.Main

# Limpiar archivos compilados
mvn clean
```

---

## 📍 Vista de Carpetas Clave

### Cuando haces clic en cada carpeta:

**yelmo-module/src/main/java/com/cinema/yelmo/**
```
yelmo/
├── Main.java ← Punto de entrada
├── control/
│   └── YelmoController.java ← Orquesta el flujo
├── feeder/
│   ├── ShowtimeFeeder.java ← Interfaz
│   └── YelmoShowtimeFeeder.java ← Implementación
├── serializer/
│   ├── ShowtimeSerializer.java ← Interfaz
│   └── DatabaseShowtimeSerializer.java ← Implementación
├── transformer/
│   └── ShowtimeTransformer.java ← Normaliza datos
└── model/
    ├── Cinema.java
    ├── Movie.java
    └── Showtime.java
```

Exactamente igual para `filmaffinity-module`.

---

## 🔍 Cómo aparecen los paquetes en VS Code

Cuando abres un archivo Java, VS Code muestra el package en la cabecera:

```java
package com.cinema.yelmo.model;

public class Cinema {
    // código
}
```

Y en el Explorer, la estructura de carpetas se correponde:

```
com/
└── cinema/
    └── yelmo/
        └── model/
            └── Cinema.java
```

---

## 📊 Vista de Problemas y Ejecución

### Pestaña "Problems" (si hay errores)
- Mostrará errores de compilación
- Faltas de imports
- Errores de sintaxis

### Pestaña "Output"
- Muestra logs de Maven
- Salida de compilación
- Errores de ejecución

### Run and Debug (Ctrl+Shift+D)
- Ejecutar con debugging
- Breakpoints
- Variables locales

---

## 🎯 Flujo de Trabajo Típico en VS Code

### 1. Crear un archivo nuevo
- Click derecho en la carpeta (ej: `model/`)
- `New File`
- Nombre: `Cinema.java`

### 2. Escribir código
```java
package com.cinema.yelmo.model;

public class Cinema {
    private int id;
    private String name;
    
    // constructor, getters, setters...
}
```

### 3. Guardar (Ctrl+S)
VS Code auto-formatea (si está configurado)

### 4. Compilar
Terminal → `mvn clean install -pl yelmo-module`

### 5. Ver errores
Si los hay, aparecen en la pestaña "Problems"

### 6. Ejecutar
Terminal → `java -cp yelmo-module\target\yelmo-module-1.0-SNAPSHOT.jar com.cinema.yelmo.Main`

---

## ✨ Tips para VS Code

### Atajos útiles:
- `Ctrl+Shift+P`: Command Palette
- `Ctrl+D`: Seleccionar palabra siguiente
- `Alt+Shift+↑/↓`: Mover línea arriba/abajo
- `Ctrl+/`: Comentar línea
- `F1`: Help
- `Ctrl+Shift+X`: Extensions

### Para búsquedas:
- `Ctrl+F`: Buscar en archivo actual
- `Ctrl+Shift+F`: Buscar en todo el proyecto

### Para refactoring:
- Click derecho en variable → `Rename Symbol`
- Click derecho en clase → `Go to Definition`

---

## 🐛 Troubleshooting

### Problema: "No se compila"
**Solución**: 
1. Verifica que Java 11+ esté instalado: `java -version`
2. Verifica que Maven esté instalado: `mvn -version`
3. Ejecuta: `mvn clean install`

### Problema: "No ve el proyecto como Maven"
**Solución**:
1. `Ctrl+Shift+P` → `Maven: Generate from archetype`
2. O: Instala la extensión "Maven for Java"

### Problema: "Los imports no funcionan"
**Solución**:
1. Asegúrate que el `package` es correcto
2. Ejecuta: `mvn clean compile`
3. Recarga VS Code: `Ctrl+Shift+P` → `Reload Window`

### Problema: "Target/classes no aparece"
**Solución**:
1. Es normal, se crea al compilar
2. Ejecuta: `mvn clean install`
3. Verás la carpeta `target/` después

---

## 📋 Estructura Final (Con código compilado)

Después de ejecutar `mvn clean install`, verás:

```
cinema-data-integration/
├── pom.xml
├── yelmo-module/
│   ├── pom.xml
│   ├── src/           ← Código fuente
│   │   ├── main/
│   │   └── test/
│   └── target/        ← Código compilado (generado)
│       ├── classes/   ← .class files
│       ├── lib/       ← JAR files
│       └── yelmo-module-1.0-SNAPSHOT.jar
│
└── filmaffinity-module/
    ├── pom.xml
    ├── src/
    │   ├── main/
    │   └── test/
    └── target/        ← Código compilado (generado)
        ├── classes/
        ├── lib/
        └── filmaffinity-module-1.0-SNAPSHOT.jar
```

---

## 🎓 Resumen para VS Code

| Acción | Cómo hacerlo |
|---|---|
| Abrir proyecto | `File → Open Folder` → `cinema-data-integration` |
| Ver estructura | Panel Explorer (izquierda) |
| Crear archivo | Click derecho → `New File` |
| Compilar | Terminal: `mvn clean install` |
| Ejecutar | Terminal: `java -cp ...` |
| Debugar | `F5` o `Ctrl+Shift+D` |
| Buscar | `Ctrl+Shift+F` |
| Terminal | `` Ctrl+` `` |

---

## 👉 Próximo Paso

1. Descarga la estructura `cinema-data-integration` desde outputs
2. Coloca la carpeta dentro de `DACD`
3. Abre en VS Code
4. Instala las extensiones Java de Microsoft
5. Escribe "adelante" cuando esté listo para crear el Paso 1 (Modelos)
