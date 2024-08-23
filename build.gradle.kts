plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "com.geistindersh.adventOfCode"
version = "2023"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("com.geistindersh.aoc.MainKt")
}