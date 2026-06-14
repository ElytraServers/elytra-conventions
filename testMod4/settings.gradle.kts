pluginManagement {
	includeBuild("../plugin")
}

plugins {
	id("cn.elytra.gradle.conventions.settings")
}

elytra {
	// declare catalogs in the versionCatalogs block
	// 在 versionCatalogs 块里声明目录（catalog）
	versionCatalogs {
		// declare a catalog for 2.7.4
		// 声明一个 2.7.4 的目录
		create("gtnh274") {
			version = "2.7.4"
		}
		// declare a catalog for 2.8.4
		// 声明一个 2.8.4 的目录
		create("gtnh284") {
			version = "2.8.4"
		}
		// you can declare catalogs as much as you wanted.
		// 你可以声明任意多的目录

		// you can use them like any other version catalogs in build.gradle(.kts).
		// e.g., `gtnh274.versions.ae2stuff` to get a `Provider<String>` instance with the version of AE2Stuff inside.
		// see also: https://docs.gradle.org/current/userguide/version_catalogs.html
		// 你可以和任何其他版本目录一样在 build.gradle(.kts) 里使用这些。
		// 例如：使用 `gtnh274.versions.ae2stuff` 获取一个 `Provider<String>` 实例，存有 AE2Stuff 的版本号。
		// 扩展阅读：https://docs.gradle.org/current/userguide/version_catalogs.html
	}
}
