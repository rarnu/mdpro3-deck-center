val kotlinVersion: String by project
val guavaVersion: String by project
val commonJvmVersion: String by project
val ktormVersion: String by project
val druidVersion: String by project
val mysqlVersion: String by project


plugins {
    application
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.rarnu.mdpro3"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.rarnu.mdpro3.MainKt")
}

dependencies {
    implementation("com.google.guava:guava:$guavaVersion")
    implementation("com.github.isyscore:common-jvm:$commonJvmVersion")
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("org.ktorm:ktorm-jackson:$ktormVersion")
    implementation("com.alibaba:druid:$druidVersion")
    implementation("org.ktorm:ktorm-support-mysql:$ktormVersion")
    implementation("mysql:mysql-connector-java:$mysqlVersion")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}