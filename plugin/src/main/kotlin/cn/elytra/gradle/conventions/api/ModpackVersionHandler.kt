package cn.elytra.gradle.conventions.api

import org.jetbrains.annotations.UnmodifiableView

public interface ModpackVersionHandler : Map<String, String> {

    /**
     * Get the version of the mod.
     *
     * Groovy magic function: you can use indexing syntax like Kotlin does to get the value by this function.
     * (e.g. `modpackVersion["Forgelin"]`)
     */
    public fun getAt(name: String): String? = get(name)

    /**
     * Get a copy of all mod versions, keyed by mod name.
     */
    public fun getModVersions(): @UnmodifiableView Map<String, String> = toMap()

    /**
     * Construct a dependency notation in `com.github.GTNewHorizons:$NAME:$VERSION[:$CLASSIFIER]`.
     */
    // @JvmOverloads // to generate function with only one argument.
    public fun gtnh(name: String, classifier: String? = null): String = buildString {
        append("com.github.GTNewHorizons")
        append(":").append(name)
        append(":").append(get(name))
        if (classifier != null) {
            append(":").append(classifier)
        }
    }

    /**
     * Construct a dependency notation in `com.github.GTNewHorizons:$NAME:$VERSION[:$CLASSIFIER]`.
     */
    public fun gtnh(name: String): String = gtnh(name, null)

    /**
     * Construct a dependency notation in `com.github.GTNewHorizons:$NAME:$VERSION:dev`.
     */
    public fun gtnhdev(name: String): String = gtnh(name, "dev")

}