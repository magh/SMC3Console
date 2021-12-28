import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val projectVersion: String by project
val kotlinVersion: String by project
val jvmTargetVersion: String by project

plugins {
    kotlin("jvm")
    application
}

group = "simracing"
version = projectVersion

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("dev.iimetra:assetto-corsa-telemetry-4j:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("io.github.microutils:kotlin-logging:2.1.0")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("io.netty:netty-all:4.1.5.Final")
    implementation("io.netty:netty-transport-rxtx:4.1.5.Final") {
        exclude(group = "org.rxtx")
    }
    implementation("org.bidib.jbidib:jbidibc-rxtx-2.2:1.6.0")
    implementation("org.bidib.jbidib:bidib-rxtx-binaries:2.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}

application {
    mainClass.set("com.mathias.simracing.AssettoCorsaKt")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = jvmTargetVersion
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = jvmTargetVersion
}
