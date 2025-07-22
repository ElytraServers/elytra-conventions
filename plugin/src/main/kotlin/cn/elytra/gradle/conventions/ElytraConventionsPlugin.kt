package cn.elytra.gradle.conventions

import cn.elytra.gradle.conventions.internal.ConventionsConst
import cn.elytra.gradle.conventions.internal.ManifestUtils
import cn.elytra.gradle.conventions.internal.Util.findPropertyAndCast
import cn.elytra.gradle.conventions.objects.ModpackVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

/**
 * The entrypoint class of Elytra Conventions Gradle Plugin.
 */
public abstract class ElytraConventionsPlugin : Plugin<Project> {

	override fun apply(project: Project) {
		// ModpackVersion
		val modpackVersion = constructModpackVersion(project)
		project.extensions.extraProperties.set("elytraModpackVersion", modpackVersion)

		@Suppress("DEPRECATION")
		let { // run deprecated initialization here
			ModpackVersionLegacy.init(modpackVersion)
			project.extensions.extraProperties.set("elytraManifest", ModpackVersionLegacy)
		}

		// Attach manifest version to JAR
		if(project.findPropertyAndCast<Boolean>(ConventionsConst.PROP_KEY_ATTACH_MANIFEST_VERSION_TO_JAR) == true) {
			val jarTask = project.tasks.findByName("jar")
			if(jarTask is Jar) {
				jarTask.archiveVersion.set(project.provider {
					project.version.toString() +
							"+" + project.findPropertyAndCast<String>(ConventionsConst.PROP_KEY_MANIFEST_VERSION)
				})
			}
		}
	}

	// internal abstract val objects: ObjectFactory @Inject get

	private fun constructModpackVersion(project: Project): ModpackVersion {
		val propManifestVersion =
			project.findPropertyAndCast<String>(ConventionsConst.PROP_KEY_MANIFEST_VERSION)
				?: ConventionsConst.DEFAULT_MODPACK_MANIFEST_VERSION
		val propManifestNoCache =
			project.findPropertyAndCast<Boolean>(ConventionsConst.PROP_KEY_MANIFEST_NO_CACHE) ?: false

		val modVersionMap = ManifestUtils.getManifest(project, propManifestVersion, propManifestNoCache)
			.let(ManifestUtils::extractManifestToMap)
		return ModpackVersion(modVersionMap)
	}

}
