# ORBYS Vote Android

Aplicación diseñada para realizar encuestas y votaciones de forma rápida y sencilla. Esta plataforma permite conectarse a un servicio http para responder a las preguntas lanzadas por el usuario.

Con esta herramienta, los usuarios crear, personalizar y gestionar sus propias encuestas, y llevar el recuento de las votaciones.


## Tecnologías

#### Ktor-server:

build.gradle(project)

```kotlin
plugins {
    id ("io.ktor.plugin") version "2.3.9"
}
```

build.gradle(module)

```kotlin
dependencies{

    implementation ("com.google.zxing:core:3.4.1")

    implementation ("io.ktor:ktor-server-netty-jvm:2.3.9")
    implementation ("ktor-server-core-jvm-jvm:2.3.9")
    implementation ("io.ktor:ktor-server-freemarker-jvm:2.3.9")
}
```

#### Dagger-hilt:

build.gradle(project)

```kotlin
plugins {
    id ("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

buildScript {
    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2:48.1")
    }
}

```

build.gradle(module)
```kotlin
plugins {
    id ("com.google.devtools.ksp")
    id ("dagger.hilt.android.plugin")
}

dependencies{
    implementation ("com.google.dagger:hilt-android:2.48.1")
    ksp ("com.google.dagger:hilt-compiler:2.48.1")
}
```

## Acciones

- Personalizar preguntas y respuestas
- Proporcionar un servicio http cliente-servidor
- Mantener control sobre los clientes
- Visualizar resultados de la votación
- Proporcionar conexión a través de Hotspot
- Obtener los resultados en un fichero


## URLs

- Privacidad https://legal.orbys.eu/privacy
- Terminos de uso https://legal.orbys.eu/terms
- Legal https://legal.orbys.eu/warning
- ORBYS https://orbys.eu
## Documentación

[Dokka](https://github.com/Kotlin/dokka)

build.gradle(project)
```Kotlin
plugins {
    id("org.jetbrains.dokka") version "1.9.20"
}
```

build.gradle(module)
```Kotlin
plugins {
    id("org.jetbrains.dokka")
}
```

Comando Generar
```
gradle dokkaHtml 
```
