package cn.elytra.gradle.conventions

import cn.elytra.gradle.conventions.extension.ElytraSettingsExtension
import cn.elytra.gradle.conventions.internal.ManifestUtils
import cn.elytra.gradle.conventions.internal.Util
import cn.elytra.gradle.conventions.internal.loadManifest
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.VersionCatalogBuilder
import org.gradle.api.logging.Logging
import org.jetbrains.annotations.ApiStatus

@ApiStatus.AvailableSince("1.2.0")
public class ElytraConventionsSettingsPlugin : Plugin<Settings> {
	override fun apply(settings: Settings) {
		val logger = Logging.getLogger(ElytraConventionsSettingsPlugin::class.java)
		// load extension to grab the version
		val elytra = settings.extensions.create("elytra", ElytraSettingsExtension::class.java)

		settings.gradle.settingsEvaluated { // run the logic later, so that the values can be loaded correctly.
			settings.dependencyResolutionManagement {
				versionCatalogs {
					elytra.versionCatalogs.forEach { versionCatalog ->
						val name = versionCatalog.name
						val version = versionCatalog.version.get()
						val useCache = versionCatalog.useCache.convention(true).get()
						logger.debug("Initializing version catalog {} ({})", name, version)

						try {
							val manifest = settings.loadManifest(version, useCache)
							create(name) { buildVersionCatalog(manifest) }
						} catch(e: Exception) {
							logger.error("Failed to initialize version catalog {} ({})", name, version, e)
						}
					}
				}
			}
		}
	}

	private fun VersionCatalogBuilder.buildVersionCatalog(manifest: Manifest) {
		ManifestUtils.extractManifestToMap(manifest)
			.forEach { (name, version) ->
				val aliasName = Util.intoCamelCase(name)
				version(aliasName, version)
				if (name in manifest.github_mods) {
					library(aliasName, GTNH_GROUP, name).versionRef(aliasName)
				}
			}
	}

	private companion object {
		private const val GTNH_GROUP = "com.github.GTNewHorizons"
	}
}
