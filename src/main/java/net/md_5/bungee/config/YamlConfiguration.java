package net.md_5.bungee.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class YamlConfiguration extends ConfigurationProvider {
    private final ThreadLocal<Yaml> yaml = new ThreadLocal<Yaml>() {
        protected Yaml initialValue() {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            return new Yaml(options);
        }
    };

    public void save(Configuration config, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            save(config, writer);
        }
    }

    public void save(Configuration config, Writer writer) {
        ((Yaml)this.yaml.get()).dump(config.self, writer);
    }

    public Configuration load(File file) throws IOException {
        return load(file, (Configuration)null);
    }

    public Configuration load(File file, Configuration defaults) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            return load(reader, defaults);
        }
    }

    public Configuration load(Reader reader) {
        return load(reader, (Configuration)null);
    }

    public Configuration load(Reader reader, Configuration defaults) {
        Map<String, Object> map = (Map<String, Object>)((Yaml)this.yaml.get()).loadAs(reader, LinkedHashMap.class);
        if (map == null)
            map = new LinkedHashMap<>();
        return new Configuration(map, defaults);
    }

    public Configuration load(InputStream is) {
        return load(is, (Configuration)null);
    }

    public Configuration load(InputStream is, Configuration defaults) {
        Map<String, Object> map = (Map<String, Object>)((Yaml)this.yaml.get()).loadAs(is, LinkedHashMap.class);
        if (map == null)
            map = new LinkedHashMap<>();
        return new Configuration(map, defaults);
    }

    public Configuration load(String string) {
        return load(string, (Configuration)null);
    }

    public Configuration load(String string, Configuration defaults) {
        Map<String, Object> map = (Map<String, Object>)((Yaml)this.yaml.get()).loadAs(string, LinkedHashMap.class);
        if (map == null)
            map = new LinkedHashMap<>();
        return new Configuration(map, defaults);
    }
}
