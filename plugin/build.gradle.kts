plugins {
	`java-gradle-plugin`
	`maven-publish`
}

group = "com.github.ElytraServers"
version = "1.0.0-SNAPSHOT"

evaluationDependsOnChildren()

repositories {
	mavenCentral()
}

dependencies {
	compileOnly(localGroovy())
	compileOnly(gradleApi())

	implementation("com.google.code.gson:gson:2.13.1")
	implementation("com.squareup.okhttp3:okhttp:5.1.0")
}

gradlePlugin {
	plugins {
		website = "https://github.com/ElytraServers"
		create("main") {
			id = if(System.getenv("JITPACK") == "true") {
				println("I love you JitPack!")
				"com.github.ElytraServers.elytra-conventions"
			} else {
				"cn.elytra.gradle.conventions"
			}
			implementationClass = "cn.elytra.gradle.conventions.ElytraConventionsPlugin"
			displayName = "Elytra Conventions"
			description = "Nothing, just a conventions."
		}
	}
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
	withJavadocJar()
	withSourcesJar()
}
