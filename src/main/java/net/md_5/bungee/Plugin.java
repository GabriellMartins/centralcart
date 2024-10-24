package net.md_5.bungee;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

import java.util.logging.Logger;

public class Plugin {


    private File file;

    private Logger logger;

    private ExecutorService service;


    public File getFile() {
        return this.file;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void onLoad() {}

    public void onEnable() {}

    public void onDisable() {}


    public final InputStream getResourceAsStream(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    public File getDataFolder() {
        return new File("plugins/" + getLogger().getName());
    }
}
