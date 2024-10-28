package com.br.bukkit.configuration;

import org.bukkit.configuration.ConfigurationOptions;

import java.util.Map;

public interface Configuration extends ConfigurationSection {
    void addDefault(String paramString, Object paramObject);

    void addDefaults(Map<String, Object> paramMap);

    void addDefaults(Configuration paramConfiguration);

    void setDefaults(Configuration paramConfiguration);

    Configuration getDefaults();

    ConfigurationOptions options();
}
