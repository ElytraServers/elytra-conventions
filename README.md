# Elytra Conventions

A Gradle convention plugin specifically designed for GTNH projects.\
一个专门为 GTNH 项目设计的 Gradle 预设插件。

## Setup 配置

[![](https://jitpack.io/v/ElytraServers/elytra-conventions.svg)](https://jitpack.io/#ElytraServers/elytra-conventions)

You can get the plugin in JitPack:\
你可以从 JitPack 获取插件：

```kotlin
// settings.gradle.kts
pluginManagement {
	repositories {
		// ...
		maven {
			url = uri("https://jitpack.io")
		}
	}
}
```

Then, you can import the plugin to your project:\
然后你可以把插件添加到你的项目：

```kotlin
plugins {
	// ...
	id("com.github.ElytraServers.elytra-conventions") version "{VERSION_HERE}"
}
```

### Version Management with Manifests 使用清单进行版本管理

The GTNH team provides a modpack manifest which contains version information for the mods. This manifest significantly
reduces the time you spend looking for dependency versions.\
GTNH 团队会提供模组包清单（Manifest），其中包含模组的版本信息。通过这个清单，你可以省去大量寻找依赖版本的时间。

To use this feature, you'll need to include this plugin first. Then, define your desired modpack version in
gradle.properties using the key `elytra.manifest.version`.\
使用这个功能，你需要先引入这个插件，然后在 gradle.properties 里用键 `elytra.manifest.version` 定义你希望的模组包版本。

You can also disable manifest caching with elytra.manifest.no-cache. Caching is disabled by default for
nightly, daily, and experimental versions.\
你还可以使用 `elytra.manifest.no-cache` 来禁用清单文件的缓存。默认 nightly，daily，experimental 这些版本的缓存是禁用的。

For example:\
例如：

```properties
elytra.manifest.version=2.7.4 # Modpack version 2.7.4
elytra.manifest.no-cache=true # Don't use caches
```
