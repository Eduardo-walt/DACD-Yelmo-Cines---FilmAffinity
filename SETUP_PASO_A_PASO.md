# Guía Paso a Paso: Configurar el Proyecto en DACD

## 📥 Paso 1: Descargar la estructura

1. **Descarga la carpeta `cinema-data-integration`** desde los outputs
   - Esta carpeta contiene toda la estructura Maven

2. **Copia la carpeta** a tu ubicación de DACD:
   ```
   C:\Users\[TuUsuario]\DACD\cinema-data-integration\
   ```

   Resultado:
   ```
   DACD/
   └── cinema-data-integration/    ← Tu proyecto aquí
       ├── pom.xml
       ├── yelmo-module/
       ├── filmaffinity-module/
       └── ... (más archivos)
   ```

---

## 🚀 Paso 2: Abrir en Visual Studio Code

### Opción A: Desde VS Code

1. Abre Visual Studio Code
2. Click en `File` → `Open Folder`
3. Navega a: `C:\Users\[TuUsuario]\DACD\cinema-data-integration`
4. Click en `Select Folder`
5. Confía en el workspace (si te pide)

### Opción B: Desde Línea de Comandos

1. Abre PowerShell o CMD
2. Navega a la carpeta:
   ```bash
   cd C:\Users\[TuUsuario]\DACD\cinema-data-integration
   code .
   ```
3. Esto abre VS Code en esa carpeta

---

## 🔧 Paso 3: Instalar Extensiones Necesarias

VS Code debería sugerir extensiones automáticamente. Si no:

1. Press `Ctrl+Shift+X` (Extensiones)
2. Busca e instala:

   - **Extension Pack for Java** (Microsoft)
     - Proporciona Language Support, Debugger, Test Runner
   
   - **Maven for Java** (Microsoft)
     - Para gestionar Maven desde VS Code
   
   - **(Opcional) Project Manager for Java**
     - Útil para proyectos multimódulo

3. Después de instalar, VS Code pedirá reiniciar. Click en `Reload`

---

## ✅ Paso 4: Verificar que todo está bien

### Terminal en VS Code (`` Ctrl+` ``)

Ejecuta este comando para verificar que Maven se reconoce:

```bash
mvn -version
```

Deberías ver algo como:
```
Apache Maven 3.8.1
Maven home: C:\Program Files\apache-maven-3.8.1
Java version: 11.0.11, ...
```

Si ves error, instala Maven o Java (ver troubleshooting abajo).

---

## 📁 Paso 5: Ver la Estructura en VS Code

1. Abre el **Explorer** (icono de carpeta en la izquierda o `Ctrl+Shift+E`)
2. Deberías ver la estructura completa:

```
CINEMA-DATA-INTEGRATION
├── pom.xml
├── README.md
├── yelmo-module
│   ├── pom.xml
│   └── src/main/java/com/cinema/yelmo/
│       ├── Main.java
│       ├── control/
│       ├── feeder/
│       ├── model/
│       ├── serializer/
│       └── transformer/
└── filmaffinity-module
    ├── pom.xml
    └── src/main/java/com/cinema/filmaffinity/
        ├── Main.java
        ├── control/
        ├── feeder/
        ├── model/
        ├── serializer/
        └── transformer/
```

Haz clic en los triángulos para expandir las carpetas.

---

## 🔨 Paso 6: Compilar el Proyecto

Abre la terminal integrada en VS Code (`` Ctrl+` ``):

```bash
# Compilar TODO
mvn clean install

# O solo un módulo
mvn clean install -pl yelmo-module
mvn clean install -pl filmaffinity-module
```

**Resultado esperado:**
```
[INFO] Building Cinema Data Integration 1.0-SNAPSHOT
[INFO] ...
[INFO] BUILD SUCCESS
```

Si ves `BUILD SUCCESS`, ¡todo está bien!

---

## 📝 Paso 7: Crear tu Primer Archivo Java

### Crear Cinema.java (modelo)

1. En el Explorer, navega a: `yelmo-module/src/main/java/com/cinema/yelmo/model/`
2. Click derecho en la carpeta `model`
3. Selecciona `New File`
4. Escribe el nombre: `Cinema.java`
5. Presiona Enter

Verás que el archivo se crea vacío. Ahora:

6. Escribe este código:

```java
package com.cinema.yelmo.model;

import java.time.LocalDateTime;

public class Cinema {
    private int id;
    private String name;
    private String location;
    private LocalDateTime capturedAt;

    // Constructor
    public Cinema(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capturedAt = LocalDateTime.now();
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public LocalDateTime getCapturedAt() { return capturedAt; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }

    // toString
    @Override
    public String toString() {
        return "Cinema{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", capturedAt=" + capturedAt +
                '}';
    }
}
```

7. Guarda el archivo: `Ctrl+S`

### Verificar que compila

En la terminal:
```bash
mvn clean compile
```

Si ves `BUILD SUCCESS`, el archivo se compiló correctamente.

---

## 🎯 Paso 8: Estructura Final Esperada

Después de todos los pasos, tu proyecto debería verse así en el Explorer:

```
CINEMA-DATA-INTEGRATION (VS Code abierto aquí)
│
├── .vscode/ (carpeta de configuración de VS Code)
│   ├── settings.json
│   └── launch.json
│
├── pom.xml (padre)
├── README.md
├── PROXIMOS_PASOS.md
├── VSCODE_SETUP.md
│
├── yelmo-module/
│   ├── pom.xml
│   ├── src/
│   │   └── main/
│   │       ├── java/com/cinema/yelmo/
│   │       │   ├── Main.java (crear después)
│   │       │   ├── model/
│   │       │   │   ├── Cinema.java ✅ (ya creado)
│   │       │   │   ├── Movie.java (crear después)
│   │       │   │   └── Showtime.java (crear después)
│   │       │   ├── feeder/
│   │       │   ├── serializer/
│   │       │   ├── transformer/
│   │       │   └── control/
│   │       └── resources/
│   └── target/ (generado al compilar)
│       ├── classes/
│       └── yelmo-module-1.0-SNAPSHOT.jar
│
└── filmaffinity-module/
    ├── pom.xml
    ├── src/
    │   └── main/
    │       ├── java/com/cinema/filmaffinity/
    │       │   ├── Main.java (crear después)
    │       │   ├── model/
    │       │   ├── feeder/
    │       │   ├── serializer/
    │       │   ├── transformer/
    │       │   └── control/
    │       └── resources/
    └── target/ (generado al compilar)
```

---

## 🐛 Troubleshooting

### Problema: "Maven no se reconoce"
**Solución:**
1. Instala Maven desde: https://maven.apache.org/download.cgi
2. Agrega Maven a PATH:
   - En Windows: `C:\Program Files\apache-maven-3.8.1\bin`
3. Reinicia VS Code

### Problema: "Java no se encontrado"
**Solución:**
1. Instala Java 11+: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
2. Verifica: `java -version` en terminal
3. Si está instalado pero no se reconoce, agrega a PATH

### Problema: "Extension Pack for Java no aparece"
**Solución:**
1. `Ctrl+Shift+X` (Extensiones)
2. Busca: "Extension Pack for Java"
3. Click en "Microsoft" (oficial)
4. Click en "Install"

### Problema: "BUILD FAILURE al compilar"
**Solución:**
1. Verifica que Java 11+ está instalado
2. Ejecuta: `mvn clean install` (completo)
3. Si persiste, revisa los errores en la terminal
4. Generalmente faltan imports o hay errores de sintaxis

### Problema: "El proyecto no aparece en el Explorer"
**Solución:**
1. Cierra VS Code
2. Abre la carpeta correcta: `cinema-data-integration`
3. No abras una subcarpeta como `yelmo-module`
4. La raíz debe ser donde está el `pom.xml` padre

---

## ⌨️ Atajos Útiles

| Atajo | Acción |
|---|---|
| `Ctrl+Shift+P` | Command Palette |
| `Ctrl+Shift+X` | Extensiones |
| `Ctrl+Shift+E` | Explorer |
| `` Ctrl+` `` | Terminal |
| `Ctrl+S` | Guardar |
| `Ctrl+/` | Comentar línea |
| `Ctrl+D` | Seleccionar palabra siguiente |
| `Alt+Up/Down` | Mover línea arriba/abajo |
| `F2` | Renombrar variable |
| `Ctrl+Shift+F` | Buscar en todo el proyecto |

---

## ✨ Paso 9: Preparado para Programar

Una vez completes todos los pasos, estarás listo para:

1. Crear más modelos (Movie, Showtime, etc.)
2. Crear interfaces (Feeders, Serializers)
3. Implementar la lógica
4. Compilar y ejecutar

**Próximo paso**: Escribe "adelante" cuando hayas configurado todo en VS Code y estés listo para comenzar a crear los archivos Java.

---

## 📋 Checklist Final

- [ ] Descargué la carpeta `cinema-data-integration`
- [ ] La coloqué en `DACD/`
- [ ] Abrí la carpeta en VS Code
- [ ] Instalé Extension Pack for Java
- [ ] Instalé Maven for Java
- [ ] Ejecuté `mvn -version` y funcionó
- [ ] Ejecuté `mvn clean install` y compiló
- [ ] Veo la estructura completa en el Explorer
- [ ] Creé el archivo `Cinema.java` de prueba
- [ ] Compilé exitosamente

**Si marcaste todo** ✅ → Estás listo para empezar a programar.

---

## 🚀 Siguientes Pasos

Una vez todo esté configurado:

1. **Escribe "adelante"** para que comencemos con los Modelos de Datos
2. Crearemos los 7 pasos de implementación
3. Tu profesor verá un proyecto bien estructurado y profesional

**¡Esperando que confirmes que todo está listo!**
