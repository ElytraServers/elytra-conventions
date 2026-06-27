# Elytra Conventions

A Gradle tool plugin designed for GregTech: New Horizons (GTNH) addon development.

## Installation

The plugin is divided into a **Settings plugin** and a **Project plugin**.

The **Settings plugin** should be applied in `settings.gradle(.kts)` and affects the entire project.

```kotlin
plugins {
    id("cn.elytra.gradle.conventions.settings")
}
```

The **Project plugin** should be applied in `build.gradle(.kts)` and affects an individual subproject.

```kotlin
plugins {
    id("cn.elytra.gradle.conventions")
}
```

## Features

### Manifest-based Version Management

To ensure compatibility with the modpack, addon mods often depend on multiple mods included within the pack. Manually tracking mod versions across modpack updates can be tedious. Fortunately, GTNH provides mod manifests for each version, which include version details that can be utilized directly.

Elytra Conventions provides two methods for extracting versions: [(a) Injected Version Catalogs](#injected-version-catalogs) and [(b) Direct Version Reading](#direct-version-reading).

**Note:** The versions `nightly`, `daily`, and `experimental` will never use the cache.

#### Injected Version Catalogs

*Requires the Settings plugin.*

Configure the name and version of the Version Catalog in `settings.gradle(.kts)`. Multiple catalogs are supported.

```kotlin
elytra {
    // Declare catalogs within the versionCatalogs block
    versionCatalogs {
        // Declare a catalog for version 2.7.4
        create("gtnh274") {
            version = "2.7.4"
        }
        // Declare a catalog for version 2.8.4
        create("gtnh284") {
            version = "2.8.4"
            useCache = false
        }
        // You can use these in build.gradle(.kts) just like any other version catalog.
        // Example: Use `gtnh274.versions.ae2stuff` to get a `Provider<String>` instance containing the version of AE2Stuff.
        // Further reading: https://docs.gradle.org/current/userguide/version_catalogs.html
    }
}
```

#### Direct Version Reading

*Requires the Project plugin applied to the respective subproject.*

Configure the version information in `build.gradle(.kts)`:

```kotlin
elytraModpackVersion {
    // Modpack version
    gtnhVersion = "2.8.0-beta-4"
    // Whether to use cache
    manifestNoCache = true
}
```

Alternatively, configure it directly via `gradle.properties`:

```properties
elytra.manifest.version=2.7.0
elytra.manifest.no-cache=false
```

To read the version information:
**Note:** The following methods are generally only accessible within the **Project Scope** (cannot be *static*).

```kotlin
println("GT5U version is: " + elytraModpackVersion["GT5-Unofficial"])
```

The plugin also provides helper methods for dependencies under the `com.github.GTNewHorizons` group:

```kotlin
dependencies {
    // Equivalent to: implementation("com.github.GTNewHorizons:CodeChickenCore:${elytraModpackVersion["CodeChickenCore"]}")
    implementation(elytraModpackVersion.gtnh("CodeChickenCore"))

    // Equivalent to: implementation("com.github.GTNewHorizons:CodeChickenCore:${elytraModpackVersion["CodeChickenCore"]}:dev")
    implementation(elytraModpackVersion.gtnhdev("CodeChickenCore"))
}
```

Additionally, it provides the functionality to attach the modpack version to the output JAR.

Add the following to `build.gradle(.kts)`:

```kotlin
elytraModpackVersion {
    attachManifestVersionToJar = true
}
```

Or add to `gradle.properties`:

```properties
elytra.manifest.attach-version-to-jar=true
```