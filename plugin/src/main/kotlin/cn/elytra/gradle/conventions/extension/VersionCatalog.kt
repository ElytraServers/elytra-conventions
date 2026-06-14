package cn.elytra.gradle.conventions.extension

import org.gradle.api.provider.Property
import javax.inject.Inject

public abstract class VersionCatalog @Inject constructor(
	/**
	 * The name of the version catalog.
	 */
	public val name: String,
) {
	/**
	 * The manifest version.
	 *
	 * Get the full list of manifests: [DreamAssemblerXXL](https://github.com/GTNewHorizons/DreamAssemblerXXL/blob/master/releases/manifests).
	 * The version is the JSON file name without extensions.
	 * E.g., `2.8.4`, `2.7.4`, `2.8.0-rc-1`, `2.7.0-beta-4`, `daily`.
	 */
	public abstract val version: Property<String>

	/**
	 * `true` to use cache if capable.
	 *
	 * **nightly**, **daily** and **experimental** won't use caches regardless of this option.
	 */
	public abstract val useCache: Property<Boolean>
}
