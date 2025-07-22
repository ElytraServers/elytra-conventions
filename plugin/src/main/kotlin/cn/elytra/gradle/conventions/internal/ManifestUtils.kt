package cn.elytra.gradle.conventions.internal

import cn.elytra.gradle.conventions.Manifest
import cn.elytra.gradle.conventions.internal.ManifestUtils.FORCE_REFRESH_VERSIONS
import cn.elytra.gradle.conventions.internal.Util.getOrThrow
import cn.elytra.gradle.conventions.internal.Util.suppressException
import com.google.gson.GsonBuilder
import org.gradle.api.Project
import java.io.File
import java.io.FileNotFoundException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

internal object ManifestUtils {

	private const val MANIFEST_URL_TEMPLATE =
		"https://raw.githubusercontent.com/GTNewHorizons/DreamAssemblerXXL/refs/heads/master/releases/manifests/%s.json"
	private val FORCE_REFRESH_VERSIONS = mutableListOf("daily", "nightly", "experimental")

	private val httpClient = HttpClient.newBuilder().build()
	private val gson = GsonBuilder().setPrettyPrinting().create()

	private fun loadManifestFromGithub(manifestVersion: String): Result<Manifest> = runCatching {
		val url = MANIFEST_URL_TEMPLATE.format(manifestVersion)
		val request = HttpRequest.newBuilder(URI(url)).build()
		val response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream())
		gson.fromJson(response.body().bufferedReader(), Manifest::class.java)
	}

	private fun getManifestCacheFile(project: Project, manifestVersion: String): File {
		return project.layout.buildDirectory.dir("elytra_conventions").get().file("${manifestVersion}.json").asFile
	}

	private fun loadManifestFromCache(project: Project, manifestVersion: String): Result<Manifest> = runCatching {
		val file = getManifestCacheFile(project, manifestVersion)
		if(!file.exists()) {
			throw FileNotFoundException("Manifest file wasn't found")
		}

		gson.fromJson(file.bufferedReader(Charsets.UTF_8), Manifest::class.java)
	}

	private fun tryRemoveManifestCache(project: Project, manifestVersion: String) = suppressException {
		getManifestCacheFile(project, manifestVersion).delete()
	}

	private fun trySaveManifestCache(project: Project, manifestVersion: String, manifest: Manifest) =
		suppressException {
			val file = getManifestCacheFile(project, manifestVersion)
			file.parentFile.mkdirs()
			file.createNewFile()
			file.writeText(gson.toJson(manifest))
		}

	/**
	 * Load the [Manifest].
	 *
	 * If [allowFromCaching] is true, and the caching exists, the cached JSON is parsed.
	 * Otherwise, it is loaded from Github.
	 */
	internal fun getManifestInternal(
		project: Project,
		manifestVersion: String,
		allowFromCaching: Boolean,
	): Manifest {
		if(allowFromCaching) {
			val fromCache = loadManifestFromCache(project, manifestVersion)
			if(fromCache.isSuccess) {
				return fromCache.getOrThrow()
			} else {
				tryRemoveManifestCache(project, manifestVersion)
			}
		}

		val manifest = loadManifestFromGithub(manifestVersion)
			.getOrThrow { IllegalStateException("Failed to load the manifest from Github", it) }
		trySaveManifestCache(project, manifestVersion, manifest)
		return manifest
	}

	/**
	 * Load the [Manifest] with smart caching strategy.
	 *
	 * If [allowFromCaching] is true, and the caching exists, the cached JSON is parsed,
	 * except the ones in [FORCE_REFRESH_VERSIONS].
	 * Otherwise, it is loaded from Github.
	 */
	internal fun getManifest(project: Project, manifestVersion: String, allowFromCaching: Boolean = true): Manifest {
		val allowFromCaching = allowFromCaching && manifestVersion !in FORCE_REFRESH_VERSIONS
		return getManifestInternal(project, manifestVersion, allowFromCaching)
	}

	/**
	 * Extract the mod version info to a map keyed by the mod name.
	 */
	internal fun extractManifestToMap(manifest: Manifest): Map<String, String> {
		return sequenceOf(manifest.github_mods, manifest.external_mods)
			.flatMap { it.entries }
			.map { (name, mod) -> name to mod.version }
			.toMap()
	}

}
