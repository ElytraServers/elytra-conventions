
pluginManagement {
    repositories {
        maven {
            // RetroFuturaGradle
            name = "GTNH Maven"
            url = uri("https://nexus.gtnewhorizons.com/repository/public/")
            mavenContent {
                includeGroup("com.gtnewhorizons")
                includeGroupByRegex("com\\.gtnewhorizons\\..+")
            }
        }
        maven {
            name = "Jitpack"
            url = uri("https://jitpack.io")
            mavenContent {
                includeGroupByRegex("com\\.github\\..+")
            }
        }
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }

    includeBuild("../plugin")
}

plugins {
    id("com.gtnewhorizons.gtnhsettingsconvention") version("1.0.38")
    id("cn.elytra.gradle.conventions.settings")
}

rootProject.name = "ElytraConventionsExampleMod"
