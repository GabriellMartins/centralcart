package com.br.bukkit.configuration.file;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemoryConfigurationOptions;

public class FileConfigurationOptions extends MemoryConfigurationOptions {
    private String header = null;

    private boolean copyHeader = true;

    protected FileConfigurationOptions(MemoryConfiguration configuration) {
        super(configuration);
    }

    public FileConfiguration configuration() {
        return (FileConfiguration)super.configuration();
    }

    public FileConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    public FileConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }

    public String header() {
        return this.header;
    }

    public FileConfigurationOptions header(String value) {
        this.header = value;
        return this;
    }

    public boolean copyHeader() {
        return this.copyHeader;
    }

    public FileConfigurationOptions copyHeader(boolean value) {
        this.copyHeader = value;
        return this;
    }
}
