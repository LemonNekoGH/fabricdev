import org.gradle.internal.jvm.Jvm
import org.jetbrains.intellij.tasks.BuildSearchableOptionsTask
import org.jetbrains.intellij.tasks.PublishTask
import org.jetbrains.intellij.tasks.RunIdeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.jetbrains.intellij") version "0.4.15"
    kotlin("jvm") version "1.3.41"
    idea
}

group = "moe.lemonneko.fabricdev"
version = "0.0.1-alpha"

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/minecraft-dev/maven")
    maven("https://repo.spongepowered.org/maven")
    maven("https://jetbrains.bintray.com/intellij-third-party-dependencies")
    maven("https://dl.bintray.com/jetbrains/intellij-plugin-service")
}

// configurations
val idea by configurations
val gradleToolingExtension: Configuration by configurations.creating {
    extendsFrom(idea)
}

val gradleToolingExtensionSourceSet = sourceSets.create("gradle-tooling-extension") {
    configurations.named(compileOnlyConfigurationName) {
        extendsFrom(gradleToolingExtension)
    }
}
val gradleToolingExtensionJar = tasks.register<Jar>(gradleToolingExtensionSourceSet.jarTaskName) {
    from(gradleToolingExtensionSourceSet.output)
    archiveClassifier.set("gradle-tooling-extension")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testCompile("junit:junit:4.12")
    implementation(files(Jvm.current().toolsJar))
    implementation(files(gradleToolingExtensionJar))
    implementation("com.jetbrains.intellij.gradle:gradle-tooling-extension:193.5233.102")
}

intellij {
    version = "2019.3"
    downloadSources = true
    setPlugins(
        "java","Groovy","gradle"
    )
}
