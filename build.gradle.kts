plugins {
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.serialization") version "2.0.20"
	application
}

group = "com.geistindersh.adventOfCode"
version = "2023"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(kotlin("test"))
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
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