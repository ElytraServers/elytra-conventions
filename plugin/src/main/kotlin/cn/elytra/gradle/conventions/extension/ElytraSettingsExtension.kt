package cn.elytra.gradle.conventions.extension

import cn.elytra.gradle.conventions.internal.ConventionsConst
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property
import org.jetbrains.annotations.ApiStatus
import javax.inject.Inject

/**
 * The extension used for elytra conventions settings plugin.
 */
@ApiStatus.AvailableSince("1.2.0")
public abstract class ElytraSettingsExtension {

	@get:Inject
	internal abstract val objectFactory: ObjectFactory

	/**
	 * The modpack version to create the version catalogs.
	 */
	public val modpackVersion: Property<String> =
		objectFactory.property<String>().convention(ConventionsConst.DEFAULT_MODPACK_MANIFEST_VERSION)

	/**
	 * The name of the version catalog. `gtNewHorizons` by default.
	 */
	public val versionCatalogName: Property<String> = objectFactory.property<String>().convention("gtNewHorizons")

}