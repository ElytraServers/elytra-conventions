plugins {
    id("java")
    id("cn.elytra.gradle.conventions")
}

repositories {
    mavenCentral()
    maven {
        name = "GTNH Maven"
        url = uri("https://nexus.gtnewhorizons.com/repository/public/")
    }
}

elytraModpackVersion {
    gtnhVersion = "2.8.0-beta-4"
    manifestNoCache = false
    attachManifestVersionToJar = true
}

dependencies {
    println("GtnhVersion = ${elytraModpackVersion.gtnhVersion.get()}")
    println("GT5 = ${elytraModpackVersion["GT5-Unofficial"]}")
    println("GTNHLib = ${elytraModpackVersion["GTNHLib"]}")
    println(elytraModpackVersion.gtnhdev("CodeChickenCore"))
}
