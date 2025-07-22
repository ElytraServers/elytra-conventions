import cn.elytra.gradle.conventions.objects.ModpackVersion

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

val elytraModpackVersion: ModpackVersion by extra

dependencies {
    println("GT5 = ${elytraModpackVersion["GT5-Unofficial"]}")
    println("GTNHLib = ${elytraModpackVersion["GTNHLib"]}")
    println(elytraModpackVersion.gtnhdev("CodeChickenCore"))
}
