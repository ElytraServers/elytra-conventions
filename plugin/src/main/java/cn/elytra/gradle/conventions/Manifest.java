package cn.elytra.gradle.conventions;

import java.util.Map;

@SuppressWarnings("unused")
public class Manifest {

    public String version;
    public String last_version; // previous version
    public String last_updated;
    public String config;
    public Map<String, Mod> github_mods;
    public Map<String, Mod> external_mods;
}
