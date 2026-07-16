plugins {
	java
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://nexus.gtnewhorizons.com/repository/public/")
	}
}

dependencies {
	println("COFHCore (274) = ${gtnh274.versions.coFhCore.get()}")
	println("COFHCore (284) = ${gtnh284.versions.coFhCore.get()}")
	println("GT5U (274) = ${gtnh274.versions.gt5Unofficial.get()}")
	println("GT5U (284) = ${gtnh284.versions.gt5Unofficial.get()}")
	implementation(gtnh284.notEnoughItems) {
		artifact {
			classifier = "dev"
		}
	}
}
