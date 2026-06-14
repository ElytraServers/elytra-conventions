package cn.elytra.gradle.conventions.extension

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.model.ObjectFactory
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
	 * The version catalogs.
	 *
	 * @see VersionCatalog
	 */
	public val versionCatalogs: NamedDomainObjectContainer<VersionCatalog> =
		objectFactory.domainObjectContainer(VersionCatalog::class.java)

}