package cn.elytra.gradle.conventions

import cn.elytra.gradle.conventions.extension.ElytraSettingsExtension
import cn.elytra.gradle.conventions.internal.ManifestUtils
import cn.elytra.gradle.conventions.internal.Util
import cn.elytra.gradle.conventions.internal.loadManifest
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.annotations.ApiStatus

@ApiStatus.AvailableSince("1.2.0")
public class ElytraConventionsSettingsPlugin : Plugin<Settings> {
	override fun apply(settings: Settings) {
		// load extension to grab the version
		val elytra = settings.extensions.create("elytra", ElytraSettingsExtension::class.java)

		// load the manifest
		val modpackVersion by elytra.modpackVersion
		val versionCatalogName by elytra.versionCatalogName
		val manifest = settings.loadManifest(modpackVersion).let { ManifestUtils.extractManifestToMap(it) }
		// inject into the version catalogs
		val versionAliasMatcher = Regex("[a-z]([a-zA-Z0-9_.\\-])+")
		settings.dependencyResolutionManagement {
			versionCatalogs {
				// access it like `gtNewHorizons.versions.forgeMultipart`.
				// FIXME: the camelcase converter can't handle multiple uppercase terms well, like 'GT5' is transformed into 'Gt5'.
				create(versionCatalogName) {
					manifest.forEach { (nameStr, version) ->
						val name = Util.intoCamelCase(nameStr)
						if(versionAliasMatcher matches name) {
							version(name, version)
						} else {
							println("Skipping $name, because the name is bad")
						}
					}
				}
			}
		}
	}
}
