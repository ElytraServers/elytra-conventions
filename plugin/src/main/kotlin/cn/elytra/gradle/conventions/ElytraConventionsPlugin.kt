package cn.elytra.gradle.conventions

import cn.elytra.gradle.conventions.extension.ModpackVersionExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

/**
 * The entrypoint class of Elytra Conventions Gradle Plugin.
 */
public abstract class ElytraConventionsPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        // Use extension to allow lazy loading
        val extension = project.extensions.create(
            "elytraModpackVersion",
            ModpackVersionExtension::class.java
        )

        @Suppress("DEPRECATION")
        let { // run deprecated initialization here
            ModpackVersionLegacy.init(extension)
            project.extensions.extraProperties.set("elytraManifest", ModpackVersionLegacy)
        }

        // Attach manifest version to JAR
        project.afterEvaluate {
            if (extension.attachManifestVersionToJar.get()) {
                val jarTask = project.tasks.findByName("jar")
                if (jarTask is Jar) {
                    jarTask.archiveVersion.set(project.provider {
                        project.version.toString() + "+" + extension.gtnhVersion.get()
                    })
                }
            }
        }

    }

}
