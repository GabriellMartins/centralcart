package net.md_5.bungee.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigurationSection {

    private final Map<String, Object> values = new HashMap<>();

    public Object get(String key) {
        return values.get(key);
    }

    public void set(String key, Object value) {
        values.put(key, value);
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public Set<String> getKeys() {
        return values.keySet();
    }

    public ConfigurationSection getSection(String path) {
        Object section = values.get(path);
        if (section instanceof ConfigurationSection) {
            return (ConfigurationSection) section;
        }
        ConfigurationSection newSection = new ConfigurationSection();
        values.put(path, newSection);
        return newSection;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}
