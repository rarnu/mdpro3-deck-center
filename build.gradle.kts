val kotlinVersion: String by project
val logbackVersion: String by project
val guavaVersion: String by project
val commonJvmVersion: String by project
val commonKtorVersion: String by project
val httpV2Version: String by project
val ktormVersion: String by project
val druidVersion: String by project
val mysqlVersion: String by project
val sqliteVersion: String by project
val kuromojiVersion: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
}

group = "com.rarnu.mdpro3"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

fun String.containsAny(vararg v: String): Boolean = v.any { this.contains(it) }

dependencies {
    implementation("com.google.guava:guava:$guavaVersion")

    implementation("com.github.isyscore:common-jvm:$commonJvmVersion")
    implementation("com.github.isyscore:common-ktor:$commonKtorVersion") {
        exclude("ch.qos.logback:logback-core")
    }

    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-compression-jvm")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-jackson-jvm")
    implementation("io.ktor:ktor-server-partial-content-jvm")
    implementation("io.ktor:ktor-server-rate-limit")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-config-yaml")

    // http/2 支持
    val osName = System.getProperty("os.name").lowercase()
    val osArch = System.getProperty("os.arch").lowercase()
    val classifierArch = when {
        osArch.containsAny("x64", "x64", "x86_64", "x8664", "amd64") -> "x86_64"
        osArch.containsAny("aarch64", "arm64") -> "aarch_64"
        else -> "x86_64"
    }
    val classifierOs = when {
        osName.containsAny("win") -> "windows"
        osName.containsAny("mac", "osx") -> "osx"
        else -> "linux"
    }
    val classifier = "$classifierOs-$classifierArch"
    implementation("io.netty:netty-tcnative-boringssl-static:$httpV2Version:$classifier")

    // ktorm
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("org.ktorm:ktorm-jackson:$ktormVersion")
    implementation("com.alibaba:druid:$druidVersion")

    val dialects = arrayOf("mysql", "postgresql", "sqlite", "oracle", "sqlserver")
    dialects.forEach { dialect ->
        implementation("org.ktorm:ktorm-support-${dialect}:$ktormVersion")
    }

    // mysql
    implementation("mysql:mysql-connector-java:$mysqlVersion")

    // sqlite
    implementation("org.xerial:sqlite-jdbc:$sqliteVersion")

    implementation("com.atilika.kuromoji:kuromoji-ipadic:$kuromojiVersion")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}
