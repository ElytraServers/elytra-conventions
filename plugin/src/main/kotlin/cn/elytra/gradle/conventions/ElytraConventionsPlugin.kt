package cn.elytra.gradle.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The entrypoint class of Elytra Conventions Gradle Plugin.
 */
public class ElytraConventionsPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		val modpackVersionExt = project.extensions.create("elytraModpackVersion", ElytraModpackVersionExtension::class.java)

		@Suppress("DEPRECATION")
		let { // run deprecated initialization here
			ModpackVersion.init(modpackVersionExt)
			project.extensions.extraProperties.set("elytraManifest", ModpackVersion)
		}
	}

}
