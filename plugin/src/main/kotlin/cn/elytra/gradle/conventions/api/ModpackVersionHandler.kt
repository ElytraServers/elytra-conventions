package cn.elytra.gradle.conventions.api

import org.jetbrains.annotations.UnmodifiableView
import java.util.function.BiConsumer

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


    /////////////////////////////////////////////////////////////////
    ////            Manually delegate implementation            /////
    /////////////////////////////////////////////////////////////////
    public fun getDelegation(): Map<String, String>

    override fun containsKey(key: String): Boolean = getDelegation().containsKey(key)

    override fun containsValue(value: String): Boolean = getDelegation().containsValue(value)

    override fun get(key: String): String? = getDelegation()[key]

    override fun getOrDefault(key: String, defaultValue: String): String = getDelegation().getOrDefault(key, defaultValue)

    override fun isEmpty(): Boolean = getDelegation().isEmpty()

    override val entries: Set<Map.Entry<String, String>>
        get() = getDelegation().entries
    override val keys: Set<String>
        get() = getDelegation().keys
    override val size: Int
        get() = getDelegation().size
    override val values: Collection<String>
        get() = getDelegation().values

    override fun forEach(action: BiConsumer<in String, in String>): Unit = getDelegation().forEach(action)
    /////////////////////////////////////////////////////////////////
}