package cn.elytra.gradle.conventions

import cn.elytra.gradle.conventions.internal.Util.findPropertyAndCast
import cn.elytra.gradle.conventions.internal.ConventionsConst
import cn.elytra.gradle.conventions.internal.ManifestUtils
import org.gradle.api.Project
import java.util.*
import javax.inject.Inject

public abstract class ElytraModpackVersionExtension @Inject constructor(internal val project: Project) {

	private val propManifestVersion =
		project.findPropertyAndCast<String>(ConventionsConst.PROP_KEY_MANIFEST_VERSION)
			?: ConventionsConst.DEFAULT_MODPACK_MANIFEST_VERSION
	private val propManifestNoCache =
		project.findPropertyAndCast<Boolean>(ConventionsConst.PROP_KEY_MANIFEST_NO_CACHE) ?: false

	private val versionMap: Map<String, String>

	init {
		project.logger.lifecycle("Loading ModpackVersion at $propManifestVersion ${if(propManifestNoCache) "no-cache" else "allow-using-cache"} for $project")
		val manifest = ManifestUtils.getManifest(project, propManifestVersion, propManifestNoCache)
		versionMap = ManifestUtils.extractManifestToMap(manifest)
	}

	public operator fun get(key: String): String? = versionMap[key]
	public fun getAt(key: String): String? = versionMap[key] // special method for Groovy

	public fun getAllVersions(): Map<String, String> = Collections.unmodifiableMap(versionMap)

	/**
	 * Construct a dependency notation in `com.github.GTNewHorizons:$NAME:$VERSION[:$CLASSIFIER]`.
	 */
	public fun gtnh(name: String, classifier: String? = null): String = buildString {
		append("com.github.GTNewHorizons")
		append(":").append(name)
		append(":").append(get(name))
		if(classifier != null) {
			append(":").append(classifier)
		}
	}

	/**
	 * Construct a dependency notation in `com.github.GTNewHorizons:$NAME:$VERSION:dev`.
	 */
	public fun gtnhdev(name: String): String = gtnh(name, "dev")

}
