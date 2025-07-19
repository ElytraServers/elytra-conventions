import cn.elytra.gradle.conventions.ModpackVersion

plugins {
    id("java")
    id("cn.elytra.gradle.conventions")
}

println("GT5 = ${ModpackVersion.get()["GT5-Unofficial"]}")
println("GTNHLib = ${ModpackVersion.getVersion("GTNHLib")}")

apply(from = "addon.gradle")

repositories {
    mavenCentral()
    maven {
        name = "GTNH Maven"
        url = uri("https://nexus.gtnewhorizons.com/repository/public/")
    }
}

dependencies {
    implementation(ModpackVersion.gtnh("CodeChickenCore", "dev"))
}
