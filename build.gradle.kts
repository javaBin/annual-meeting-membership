val kotlin_version: String by project
val logback_version: String by project
val logstash_version: String by project


plugins {
    kotlin("jvm") version "2.0.21"
    id("io.ktor.plugin") version "3.0.0"
}

group = "javabin.no"
version = "0.0.1"

application {
    mainClass.set("javabin.no.member_lookup.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(21)
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

tasks.test {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    // Server
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    testImplementation("io.ktor:ktor-server-test-host-jvm")

    // Http Client
    implementation("io.ktor:ktor-client-cio")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")
    runtimeOnly("net.logstash.logback:logstash-logback-encoder:$logstash_version")
    implementation("io.ktor:ktor-client-logging")

    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
