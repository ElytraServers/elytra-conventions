package cn.elytra.gradle.conventions.extension

import cn.elytra.gradle.conventions.api.ModpackVersionHandler
import cn.elytra.gradle.conventions.internal.ConventionsConst
import cn.elytra.gradle.conventions.internal.ManifestUtils
import cn.elytra.gradle.conventions.internal.Util.findPropertyAndCast
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import java.util.function.BiConsumer
import javax.inject.Inject

public abstract class ModpackVersionExtension : ModpackVersionHandler {

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

    private val delegate: Map<String, String> by lazy {
        ManifestUtils.getManifest(project, gtnhVersion.get(), manifestNoCache.get())
            .let(ManifestUtils::extractManifestToMap)
    }

    // used for groovy...
    public fun getGtnhVersion(): String = gtnhVersion.get()
    public fun setGtnhVersion(version: String): Unit = gtnhVersion.set(version)
    public fun getManifestNoCache(): Boolean = manifestNoCache.get()
    public fun setManifestNoCache(boolean: Boolean): Unit = manifestNoCache.set(boolean)
    public fun getAttachManifestVersionToJar(): Boolean = attachManifestVersionToJar.get()
    public fun setAttachManifestVersionToJar(boolean: Boolean): Unit = attachManifestVersionToJar.set(boolean)

    /////////////////////////////////////////////////////////////////
    ////            Manually delegate implementation            /////
    /////////////////////////////////////////////////////////////////
    override fun containsKey(key: String): Boolean = delegate.containsKey(key)

    override fun containsValue(value: String): Boolean = delegate.containsValue(value)

    override fun get(key: String): String? = delegate[key]

    override fun getOrDefault(key: String, defaultValue: String): String = delegate.getOrDefault(key, defaultValue)

    override fun isEmpty(): Boolean = delegate.isEmpty()

    override val entries: Set<Map.Entry<String, String>>
        get() = delegate.entries
    override val keys: Set<String>
        get() = delegate.keys
    override val size: Int
        get() = delegate.size
    override val values: Collection<String>
        get() = delegate.values

    override fun forEach(action: BiConsumer<in String, in String>): Unit = delegate.forEach(action)
    /////////////////////////////////////////////////////////////////

}