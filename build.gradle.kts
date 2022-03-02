import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "me.rahulrawat"
version = "1.0"

repositories {
    google()
    mavenCentral()
    mavenLocal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("me.rahulrawat:AddressLibrary:1.0-SNAPSHOT")
    implementation("com.arkivanov.decompose:decompose:0.5.0")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.5.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Address"
            packageVersion = "1.0.0"
            macOS {
                iconFile.set(File("icon.png"))
            }
            windows {
                iconFile.set(File("icon.png"))
            }
            linux {
                iconFile.set(File("icon.png"))
            }
        }
    }
}