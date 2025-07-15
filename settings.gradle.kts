rootProject.name = "elytra-conventions"

pluginManagement {
	includeBuild("plugin") {
		name = "elytra-conventions"
	}
}

// ignore this when publishing to JitPack
if(System.getenv("JITPACK") != "true") {
	include("testMod")
}
