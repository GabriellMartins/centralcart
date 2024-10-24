package br.com.centralcart.config;

import br.com.centralcart.BungeePlugin;
import br.com.centralcart.utils.Logger;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BungeeProperties {

  private static Configuration fileConfiguration;
  private static File propertiesFile;
  private static final String fileName = "properties.yml";

  public static void setup(BungeePlugin plugin) {
    if (!plugin.getDataFolder().exists())
      plugin.getDataFolder().mkdirs();

    propertiesFile = new File(plugin.getDataFolder(), fileName);

    if (!propertiesFile.exists()) {
      try {
        Files.createFile(propertiesFile.toPath());

        fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class)
                .load(propertiesFile);

        fileConfiguration.set("secret", "token-da-sua-loja");
      }catch (IOException ex) {
        ex.printStackTrace();
      }
    } else {
      try {
        fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class)
                .load(propertiesFile);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }


  }

  private static Configuration get() {
    return fileConfiguration;
  }

  public static void save(){
    try {
        ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).save(get(), propertiesFile);
    } catch (IOException e) {
      Logger.error("Couldn't save " + fileName);
    }
  }

  private static void setValue(String key, String value) {
    get().set(key, value);

    save();
  }

  public static void setSecret(String value) {
    setValue("secret", value);
  }

  public static String getSecret() {
    return get().getString("secret");
  }
}
