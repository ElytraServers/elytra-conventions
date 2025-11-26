package cn.elytra.gradle.conventions.task

import cn.elytra.gradle.conventions.internal.ManifestUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

public abstract class ListAvailableManifestsTask : DefaultTask() {

	@TaskAction
	public fun execute() {
		val manifests = ManifestUtils.listManifests().mapKeys { it.key.removeSuffix(".json") }
		val longestVersionLength = manifests.maxOf { it.key.length }
		manifests.forEach { (filename, fileUrl) ->
			println(filename.padEnd(longestVersionLength, ' ') + " => " + fileUrl)
		}
	}

}