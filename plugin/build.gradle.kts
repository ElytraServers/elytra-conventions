plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
	`maven-publish`

	id("com.palantir.git-version") version "4.0.0"
	id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.18.1"

	id("com.gradle.plugin-publish") version "2.0.0"
}

val gitVersion: groovy.lang.Closure<String> by extra

group = "cn.elytra.gradle"
version = System.getenv("VERSION") ?: gitVersion()

tasks.wrapper {
	distributionType = Wrapper.DistributionType.ALL
}

evaluationDependsOnChildren()

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(localGroovy())
	compileOnly(gradleApi())

	implementation("com.google.code.gson:gson:2.13.1")
}

gradlePlugin {
	val main by plugins.creating {
		id = if(System.getenv("JITPACK") == "true") {
			println("I love you JitPack!")
			"com.github.ElytraServers.elytra-conventions"
		} else {
			"cn.elytra.gradle.conventions"
		}
		implementationClass = "cn.elytra.gradle.conventions.ElytraConventionsPlugin"
		displayName = "Elytra Conventions"
		description = "Nothing, just a conventions."
		tags = listOf("minecraft")
	}

	website = "https://github.com/ElytraServers/elytra-conventions"
	vcsUrl = "https://github.com/ElytraServers/elytra-conventions"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
	withSourcesJar()
}

kotlin {
	explicitApiWarning()
}

apiValidation {
	ignoredPackages += listOf("cn.elytra.gradle.conventions.internal")
	apiDumpDirectory = "api"
}
