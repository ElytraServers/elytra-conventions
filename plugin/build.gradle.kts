plugins {
	`java-gradle-plugin`
	`kotlin-dsl`
	`maven-publish`
	signing

	id("com.palantir.git-version") version "5.0.0"
	id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.18.1"

	id("com.gradle.plugin-publish") version "2.1.1"
	id("com.vanniktech.maven.publish") version "0.37.0"
}

@Suppress("UNCHECKED_CAST")
val gitVersion = extra.get("gitVersion") as groovy.lang.Closure<String>

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

	implementation("com.google.code.gson:gson:2.14.0")
}

gradlePlugin {
	plugins.register("main") {
		id = "cn.elytra.gradle.conventions"
		implementationClass = "cn.elytra.gradle.conventions.ElytraConventionsPlugin"
		displayName = "Elytra Conventions"
		description = "Nothing, just a conventions."
		tags = listOf("Minecraft")
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

tasks.withType<Javadoc> {
	val opt = options as StandardJavadocDocletOptions
	// don't panic with missing docs.
	opt.addStringOption("Xdoclint:all,-missing", "-quiet")
}

mavenPublishing {
	coordinates("cn.elytra", "elytra-conventions", project.version.toString().removePrefix("v"))

	pom {
		name.set("Elytra Conventions Plugins")
		description.set("A convention Gradle plugin for GTNewHorizons specific region.")
		inceptionYear.set("2026")
		url.set("https://github.com/ElytraServers/elytra-conventions")
		licenses {
			license {
				name.set("MIT License")
				url.set("https://github.com/ElytraServers/elytra-conventions/blob/master/LICENSE")
			}
		}
		developers {
			developer {
				id.set("taskeren")
				name.set("Taskeren")
				email.set("r0yalist@outlook.com")
			}
		}
		scm {
			connection.set("scm:git:git://github.com/ElytraServers/elytra-conventions.git")
			developerConnection.set("scm:git:ssh://github.com/ElytraServers/elytra-conventions.git")
			url.set("https://github.com/ElytraServers/elytra-conventions")
		}
	}

	publishToMavenCentral()
	signAllPublications()
}
