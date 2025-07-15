package cn.elytra.gradle.conventions;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * The entrypoint class of Elytra Conventions Gradle Plugin.
 */
public class ElytraConventionsPlugin implements Plugin<Project> {

    @NotNull
    private static final String MODPACK_VERSION_DEFAULT = "2.7.4";

    /**
     * A thread-local reference to the project.
     * Internal usage only!
     */
    @NotNull
    @ApiStatus.Internal
    public static final ThreadLocal<Project> PROJECT = new ThreadLocal<>();

    @Override
    public void apply(@NotNull Project project) {
        PROJECT.set(project);

        String manifestVersion = getStringProperty("elytra.manifest.version").orElse(MODPACK_VERSION_DEFAULT);
        boolean forceRefresh = getBooleanProperty("elytra.manifest.no-cache").orElse(false);

        ModpackVersion.init(manifestVersion, forceRefresh);

        PROJECT.remove();
    }

    private static Optional<String> getStringProperty(String propertyName) {
        Object propValue = PROJECT.get().findProperty(propertyName);
        if (propValue instanceof String) {
            return Optional.of((String) propValue);
        }
        return Optional.empty();
    }

    private static Optional<Boolean> getBooleanProperty(String propertyName) {
        Object propValue = PROJECT.get().findProperty(propertyName);
        if (propValue instanceof Boolean) {
            return Optional.of((Boolean) propValue);
        } else if (propValue instanceof String) {
            return Optional.of(Boolean.parseBoolean((String) propValue));
        }
        return Optional.empty();
    }

}
