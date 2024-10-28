package com.br.bukkit.configuration.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfigurationOptions;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

public class YamlConfiguration extends FileConfiguration {
    protected static final String COMMENT_PREFIX = "# ";

    protected static final String BLANK_CONFIG = "{}\n";

    private final DumperOptions yamlOptions;

    private final Representer yamlRepresenter;

    LoaderOptions loaderOptions;

    private final Yaml yaml;

    public YamlConfiguration() {
        this.yamlOptions = new DumperOptions();
        this.yamlRepresenter = new YamlRepresenter();
        this.loaderOptions = new LoaderOptions();
        this.yaml = new Yaml((BaseConstructor)new YamlConstructor(), this.yamlRepresenter, this.yamlOptions, this.loaderOptions);
        this.loaderOptions.setMaxAliasesForCollections(1024);
    }

    public String saveToString() {
        this.yamlOptions.setIndent(options().indent());
        this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlOptions.setAllowUnicode(SYSTEM_UTF);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        String header = buildHeader();
        String dump = this.yaml.dump(getValues(false));
        if (dump.equals("{}\n"))
            dump = "";
        return header + dump;
    }

    public void loadFromString(String contents) throws InvalidConfigurationException {
        Map<?, ?> input;
        Validate.notNull(contents, "Contents cannot be null");
        try {
            input = (Map<?, ?>)this.yaml.load(contents);
        } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException e) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }
        String header = parseHeader(contents);
        if (header.length() > 0)
            options().header(header);
        if (input != null)
            convertMapsToSections(input, (ConfigurationSection)this);
    }

    protected void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof Map) {
                convertMapsToSections((Map<?, ?>)value, section.createSection(key));
                continue;
            }
            section.set(key, value);
        }
    }

    protected String parseHeader(String input) {
        String[] lines = input.split("\r?\n", -1);
        StringBuilder result = new StringBuilder();
        boolean readingHeader = true;
        boolean foundHeader = false;
        for (int i = 0; i < lines.length && readingHeader; i++) {
            String line = lines[i];
            if (line.startsWith("# ")) {
                if (i > 0)
                    result.append("\n");
                if (line.length() > "# ".length())
                    result.append(line.substring("# ".length()));
                foundHeader = true;
            } else if (foundHeader && line.length() == 0) {
                result.append("\n");
            } else if (foundHeader) {
                readingHeader = false;
            }
        }
        return result.toString();
    }

    protected String buildHeader() {
        String header = options().header();
        if (options().copyHeader()) {
            Configuration def = getDefaults();
            if (def != null && def instanceof FileConfiguration) {
                FileConfiguration filedefaults = (FileConfiguration)def;
                String defaultsHeader = filedefaults.buildHeader();
                if (defaultsHeader != null && defaultsHeader.length() > 0)
                    return defaultsHeader;
            }
        }
        if (header == null)
            return "";
        StringBuilder builder = new StringBuilder();
        String[] lines = header.split("\r?\n", -1);
        boolean startedHeader = false;
        for (int i = lines.length - 1; i >= 0; i--) {
            builder.insert(0, "\n");
            if (startedHeader || lines[i].length() != 0) {
                builder.insert(0, lines[i]);
                builder.insert(0, "# ");
                startedHeader = true;
            }
        }
        return builder.toString();
    }

    public YamlConfigurationOptions options() {
        if (this.options == null)
            this.options = new YamlConfigurationOptions(this);
        return (YamlConfigurationOptions)this.options;
    }

    public static YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (FileNotFoundException fileNotFoundException) {

        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, (Throwable)ex);
        }
        return config;
    }

    @Deprecated
    public static YamlConfiguration loadConfiguration(InputStream stream) {
        Validate.notNull(stream, "Stream cannot be null");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(stream);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", (Throwable)ex);
        }
        return config;
    }

    public static YamlConfiguration loadConfiguration(Reader reader) {
        Validate.notNull(reader, "Stream cannot be null");
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(reader);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", (Throwable)ex);
        }
        return config;
    }
}
