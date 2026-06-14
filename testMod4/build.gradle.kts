plugins {
	java
}

repositories {
	mavenCentral()
}

dependencies {
	println("COFHCore (274) = ${gtnh274.versions.coFhCore.get()}")
	println("COFHCore (284) = ${gtnh284.versions.coFhCore.get()}")
	println("GT5U (274) = ${gtnh274.versions.gt5Unofficial.get()}")
	println("GT5U (284) = ${gtnh284.versions.gt5Unofficial.get()}")
}
