// Define las versiones de las bibliotecas
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    // Plugin para el desarrollo en JVM con Kotlin
    kotlin("jvm") version "1.9.21"

    // Plugin de Ktor para simplificar la configuración del servidor web
    id("io.ktor.plugin") version "2.3.7"

    // Plugin para la serialización de Kotlin
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
}

// Configuración del proyecto
group = "com.example"
version = "0.0.1"

application {
    // Configuración del punto de entrada principal de la aplicación
    mainClass.set("io.ktor.server.netty.EngineMain")

    // Configuración para el desarrollo (habilita/deshabilita características específicas)
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    // Repositorio Maven Central
    mavenCentral()
}

dependencies {
    // Dependencias de implementación
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("org.jetbrains.exposed:exposed-core:0.36.2")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    // Dependencias de pruebas
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
