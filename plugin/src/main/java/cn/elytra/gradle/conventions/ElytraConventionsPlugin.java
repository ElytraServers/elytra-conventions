package cn.elytra.gradle.conventions;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ElytraConventionsPlugin implements Plugin<Project> {

    @NotNull
    private static final String MODPACK_VERSION_DEFAULT = "2.7.4";

    @Override
    public void apply(@NotNull Project project) {
        String manifestVersion = getStringProperty(project, "elytra.manifest.version").orElse(MODPACK_VERSION_DEFAULT);
        boolean forceRefresh = getBooleanProperty(project, "elytra.manifest.no-cache").orElse(false);

        ModpackVersion.init(project, manifestVersion, forceRefresh);
    }

    private static Optional<String> getStringProperty(Project project, String propertyName) {
        Object propValue = project.findProperty(propertyName);
        if (propValue instanceof String) {
            return Optional.of((String) propValue);
        }
        return Optional.empty();
    }

    private static Optional<Boolean> getBooleanProperty(Project project, String propertyName) {
        Object propValue = project.findProperty(propertyName);
        if (propValue instanceof Boolean) {
            return Optional.of((Boolean) propValue);
        } else if (propValue instanceof String) {
            return Optional.of(Boolean.parseBoolean((String) propValue));
        }
        return Optional.empty();
    }

}
