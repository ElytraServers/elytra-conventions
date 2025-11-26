package cn.elytra.gradle.conventions.extension

import cn.elytra.gradle.conventions.internal.ConventionsConst
import cn.elytra.gradle.conventions.internal.ManifestUtils
import cn.elytra.gradle.conventions.internal.Util.findPropertyAndCast
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import org.jetbrains.annotations.UnmodifiableView
import javax.inject.Inject

public abstract class ModpackVersionExtension : AbstractMap<String, String>() {

	@get:Inject
	internal abstract val project: Project

	@get:Inject
	internal abstract val objectFactory: ObjectFactory

	/**
	 *  Obviously, setting your gtnh version, if you don't set it will default to
	 *  [ConventionsConst.DEFAULT_MODPACK_MANIFEST_VERSION]
	 */
	public val gtnhVersion: Property<String> = objectFactory
		.property<String>()
		.convention(project.provider {
			project.findPropertyAndCast<String>(ConventionsConst.PROP_KEY_MANIFEST_VERSION)
				?: ConventionsConst.DEFAULT_MODPACK_MANIFEST_VERSION
		})
		.apply { finalizeValueOnRead() }

	/**
	 *  Whether to cache the manifest,
	 *  if [gtnhVersion] is in [ManifestUtils.FORCE_REFRESH_VERSIONS],
	 *  caching will be disabled.
	 *
	 *  The default is `false`
	 */
	public val manifestNoCache: Property<Boolean> = objectFactory
		.property<Boolean>()
		.convention(project.provider {
			project.findPropertyAndCast<Boolean>(ConventionsConst.PROP_KEY_MANIFEST_VERSION) ?: false
		})
		.apply { finalizeValueOnRead() }

	/**
	 *  A little feature that automatically adds the [gtnhVersion] suffix to your artifact.
	 *
	 *  The default is `false`.
	 */
	public val attachManifestVersionToJar: Property<Boolean> = objectFactory
		.property<Boolean>()
		.convention(project.provider {
			project.findPropertyAndCast<Boolean>(ConventionsConst.PROP_KEY_ATTACH_MANIFEST_VERSION_TO_JAR) ?: false
		})
		.apply { finalizeValueOnRead() }

	public val autoConfigureProxy: Property<Boolean> = objectFactory
		.property<Boolean>()
		.convention(true)
		.apply { finalizeValueOnRead() }

	private val delegate: Map<String, String> by lazy {
		if(autoConfigureProxy.get()) {
			System.setProperty("java.net.useSystemProxies", "true")
			project.logger.info("Configuring java.net.useSystemProxies = true")
		}
		ManifestUtils.getManifest(project, gtnhVersion.get(), manifestNoCache.get())
			.let(ManifestUtils::extractManifestToMap)
	}

	/**
	 * Get the version of the mod.
	 *
	 * Groovy magic function: you can use indexing syntax like Kotlin does to get the value by this function.
	 * (e.g. `modpackVersion["Forgelin"]`)
	 */
	public fun getAt(name: String): String? {
		return get(name)
	}

	/**
	 * Get a copy of all mod versions, keyed by mod name.
	 */
	public fun getModVersions(): @UnmodifiableView Map<String, String> {
		return delegate.toMap()
	}

	/**
	 * Construct a dependency notation in `com.github.GTNewHorizons:$NAME:$VERSION[:$CLASSIFIER]`.
	 */
	@JvmOverloads
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

	// used for groovy...
	public fun getGtnhVersion(): String = gtnhVersion.get()
	public fun setGtnhVersion(version: String): Unit = gtnhVersion.set(version)
	public fun getManifestNoCache(): Boolean = manifestNoCache.get()
	public fun setManifestNoCache(boolean: Boolean): Unit = manifestNoCache.set(boolean)
	public fun getAttachManifestVersionToJar(): Boolean = attachManifestVersionToJar.get()
	public fun setAttachManifestVersionToJar(boolean: Boolean): Unit = attachManifestVersionToJar.set(boolean)

	override val entries: Set<Map.Entry<String, String>> get() = delegate.entries
}
