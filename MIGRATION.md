# Migration Guide

### v1.0.x -> v1.1.x

1. Renamed `cn.elytra.gradle.conventions.ModpackVersion` to `cn.elytra.gradle.conventions.ModpackVersionLegacy`. And it
   is deprecated. You should use `ext.elytraModpackVersion` instead, with the same functions provided.
   `getVersion(String)` is removed, see below.
2. `cn.elytra.gradle.conventions.ModpackVersion#getVersion(String)` is removed, you should use
   `cn.elytra.gradle.conventions.ModpackVersion#get(String)` instead.
