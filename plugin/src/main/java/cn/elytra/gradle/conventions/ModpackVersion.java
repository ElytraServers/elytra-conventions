package cn.elytra.gradle.conventions;

import com.google.gson.Gson;
import org.gradle.api.Project;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The Manifest Version Management Util.
 * <p>
 * You can get the corresponding version of dependent mods used in the modpack from the given manifest.
 * <p>
 * To specify the modpack version, you need to assign the property {@code elytra.manifest.version} in somewhere like
 * <i>gradle.properties</i>. You can also assign the property {@code elytra.manifest.no-cache} to control if caching is
 * used.
 * <p>
 * You can get the version of the dependant mods by {@link #getVersion(String)} or {@link #getAt(String)} in Groovy
 * special way {@code ModpackVersion[DEP]}.
 */
public class ModpackVersion extends AbstractMap<String, String> {

    private static final Gson GSON = new Gson();
    private static final String MANIFEST_URL_TEMPLATE = "https://raw.githubusercontent.com/GTNewHorizons/DreamAssemblerXXL/refs/heads/master/releases/manifests/%s.json";
    private static final List<String> FORCE_REFRESH_VERSIONS = Arrays.asList("daily", "nightly", "experimental");

    private static ModpackVersion singleton;

    private final Map<String, String> modVersionMap = new HashMap<>();
    private final HttpClient httpClient = HttpClient.newBuilder()
        .build();

    /**
     * Don't directly instantiate this class.
     */
    private ModpackVersion() {
        singleton = this;
    }

    /**
     * Internal method. Initialize the ModpackVersion.
     *
     * @param version      the version of manifest to be loaded
     * @param forceRefresh {@code true} to disable caching
     */
    @ApiStatus.Internal
    public static void init(String version, boolean forceRefresh) {
        Project project = ElytraConventionsPlugin.PROJECT.get();
        Path path = project.getLayout()
            .getBuildDirectory()
            .dir("elytra_conventions")
            .get()
            .file(version + ".json")
            .getAsFile()
            .toPath();
        // forcibly enable force refresh if the versions are in the predefined list.
        forceRefresh |= FORCE_REFRESH_VERSIONS.stream()
            .anyMatch(version::equals);
        project.getLogger()
            .lifecycle(
                "Loading ModpackVersion with version {}, caching at {}, force-fresh {}",
                version,
                path,
                forceRefresh);
        new ModpackVersion().init(version, path, forceRefresh);
    }

    private void init(String version, Path cache, boolean forceRefresh) {
        Project project = ElytraConventionsPlugin.PROJECT.get();

        try {
            if (!forceRefresh && Files.exists(cache)) {
                project.getLogger()
                    .info("Manifest cache hit at {}", cache);
                try {
                    Manifest manifest = getManifestFromFile(cache);
                    readManifest(manifest);
                    return;
                } catch (Exception e) {
                    project.getLogger()
                        .info("Failed to read cached manifest", e);
                    Files.delete(cache);
                    // ignore the exception and continue as if there's no cache.
                }
            }

            project.getLogger()
                .info("Try downloading manifest from Github");
            Manifest manifest = getManifestFromGithub(version);
            readManifest(manifest);
            saveManifestToFile(cache, manifest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Manifest getManifestFromGithub(String version) {
        String urlString = String.format(MANIFEST_URL_TEMPLATE, version);
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI(urlString))
                .build();
            HttpResponse<InputStream> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return GSON.fromJson(new InputStreamReader(resp.body()), Manifest.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load the Manifest version " + version + " at " + urlString + ".", e);
        }
    }

    private Manifest getManifestFromFile(Path manifestPath) {
        try {
            return GSON.fromJson(Files.newBufferedReader(manifestPath), Manifest.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the Manifest at path " + manifestPath + ".", e);
        }
    }

    private void saveManifestToFile(Path manifestPath, Manifest manifest) {
        try {
            Files.createDirectories(manifestPath.getParent());
            Files.writeString(manifestPath, GSON.toJson(manifest), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readManifest(Manifest manifest) {
        Project project = ElytraConventionsPlugin.PROJECT.get();
        manifest.github_mods.forEach((name, info) -> {
            project.getLogger()
                .debug("Manifest Entry: Github Mod {} version {}", name, info.version);
            modVersionMap.put(name, info.version);
        });
        manifest.external_mods.forEach((name, info) -> {
            project.getLogger()
                .debug("Manifest Entry: External Mod {} version {}", name, info.version);
            modVersionMap.put(name, info.version);
        });
    }

    /**
     * @return the singleton instance of {@link ModpackVersion}.
     */
    public static ModpackVersion get() {
        return Objects.requireNonNull(singleton, "ModpackVersion isn't initialized.");
    }

    /**
     * @param name the mod name, like {@code GT5-Unofficial}.
     * @return the version of the given dependant mod.
     */
    @Nullable
    public static String getVersion(String name) {
        return get().modVersionMap.get(name);
    }

    /**
     * @param propertyName the mod name, like {@code GT5-Unofficial}.
     * @return the version of the given dependant mod.
     */
    @Nullable
    public static String getAt(String propertyName) { // Groovy magic function
        return getVersion(propertyName);
    }

    /**
     * @return the map of all loaded dependant mods with their version.
     */
    @NotNull
    @UnmodifiableView
    public static Map<String, String> getModVersions() {
        return Collections.unmodifiableMap(get().modVersionMap);
    }

    @NotNull
    @Override
    public Set<Entry<String, String>> entrySet() {
        return modVersionMap.entrySet();
    }

    /**
     * @param name the dependant mod name
     * @return a dependency notation for dependencies under group {@code com.github.GTNewHorizons}.
     */
    public static String gtnh(String name) {
        return "com.github.GTNewHorizons:" + name + ":" + getAt(name);
    }

}

@SuppressWarnings("unused")
class Manifest {

    String version;
    String last_version; // previous version
    String last_updated;
    String config;
    Map<String, Mod> github_mods;
    Map<String, Mod> external_mods;
}

@SuppressWarnings("unused")
class Mod {

    String version;
    String side;
}
