@file:JvmName("ModpackVersion")

package cn.elytra.gradle.conventions

import org.jetbrains.annotations.UnmodifiableView

/**
 * A backward-compatibility class.
 *
 * Because of the classloader isolation of Gradle, scripts loaded by another Gradle plugin (e.g. GTNHGradle) are not
 * able to access this class, causing compilation error or runtime error. Thus we switched to extensions, which is
 * accessible in these cases.
 */
@Deprecated("Use ElytraModpackVersionExtension instead.")
public class ModpackVersionLegacy(private val delegate: Map<String, String>) : Map<String, String> by delegate {

	@Suppress("DEPRECATION")
	@Deprecated("Use ElytraModpackVersionExtension instead.")
	public companion object {

		private var singleton: ModpackVersionLegacy? = null

		internal fun init(mv: cn.elytra.gradle.conventions.objects.ModpackVersion) {
			singleton = ModpackVersionLegacy(mv)
		}

		@JvmStatic
		public fun get(): ModpackVersionLegacy {
			return requireNotNull(singleton)
		}

		@JvmStatic
		public fun getVersion(name: String): String? {
			return get()[name]
		}

		@JvmStatic
		public fun getAt(propertyName: String): String? = getVersion(propertyName)

		@JvmStatic
		public fun getModVersions(): @UnmodifiableView Map<String, String> {
			return get().toMap()
		}

		@JvmStatic
		@JvmOverloads
		public fun gtnh(name: String, classifier: String? = null): String {
			return buildString {
				append("com.github.GTNewHorizons:")
				append(name)
				append(':')
				append(getVersion(name))
				if(classifier != null) {
					append(":")
					append(classifier)
				}
			}
		}

	}

}
