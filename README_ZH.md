# Elytra Conventions

给 GregTech: New Horizons 附属使用的 Gradle 工具插件。

## 安装

插件分为 Settings 插件和 Project 插件。

Settings 插件需要安装在 `settings.gradle(.kts)`，作用于整个项目。

```kotlin
plugins {
    id("cn.elytra.gradle.conventions.settings")
}
```

Project 插件需要安装在 `build.gradle(.kts)`，作用于单个子项目。

```kotlin
plugins {
    id("cn.elytra.gradle.conventions")
}
```

## 功能

### 清单化版本管理

为了适配模组包，附属模组通常需要依赖多个模组包里的模组。而模组包的版本更新会让查找使用的模组版本非常麻烦，不过好在 GTNH
提供了每个模组包版本的模组清单，包含版本信息，可以直接使用。

Elytra Conventions 提供了两种提取版本的方法：[(a) 注入 Version Catalogs](#注入-version-catalogs)
和 [(b) 直接读取版本信息](#直接读取版本信息)。

注意：`nightly`，`daily`，`experimental` 这三个版本永远不会使用缓存。

#### 注入 Version Catalogs

需要使用 Settings 插件。

在 `settings.gradle(.kts)` 里配置 Version Catalog 的名称和版本，支持添加多个清单。

```kotlin
elytra {
    // 在 versionCatalogs 块里声明目录（catalog）
    versionCatalogs {
        // 声明一个 2.7.4 的目录
        create("gtnh274") {
            version = "2.7.4"
        }
        // 声明一个 2.8.4 的目录
        create("gtnh284") {
            version = "2.8.4"
            useCache = false
        }
        // 你可以和任何其他版本目录一样在 build.gradle(.kts) 里使用这些。
        // 例如：使用 `gtnh274.versions.ae2stuff` 获取一个 `Provider<String>` 实例，存有 AE2Stuff 的版本号。
        // 扩展阅读：https://docs.gradle.org/current/userguide/version_catalogs.html
    }
}
```

#### 直接读取版本信息

需要在对应的项目使用 Project 插件。

在 `build.gradle(.kts)` 里配置版本信息。

```kotlin
elytraModpackVersion {
    // 模组包版本
    gtnhVersion = "2.8.0-beta-4"
    // 是否使用缓存
    manifestNoCache = true
}
```

或者直接使用 `gradle.properties`。

```properties
elytra.manifest.version=2.7.0
elytra.manifest.no-cache=false
```

再读取版本信息。
注意，下面这些方法通常需要在 Project Scope 下才能访问（不能是 _static_）。

```kotlin
println("GT5U 的版本是 " + elytraModpackVersion["GT5-Unofficial"])
```

同时这个插件还为 _com.github.GTNewHorizons_ 包下的依赖提供了快捷方法。

```kotlin
dependencies {
    // 等价于 implementation("com.github.GTNewHorizons:CodeChickenCore:${elytraModpackVersion["CodeChickenCore"]}")
    implementation(elytraModpackVersion.gtnh("CodeChickenCore"))
    // 等价于 implementation("com.github.GTNewHorizons:CodeChickenCore:${elytraModpackVersion["CodeChickenCore"]}:dev")
    implementation(elytraModpackVersion.gtnhdev("CodeChickenCore"))
}
```

以及提供了给输出的 Jar 添加模组包版本的功能。

在 `build.gradle(.kts)` 里添加。

```kotlin
elytraModpackVersion {
    attachManifestVersionToJar = true
}
```

或在 `gradle.properties` 里添加。

```properties
elytra.manifest.attach-version-to-jar=true
```
