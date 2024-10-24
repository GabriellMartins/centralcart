package br.com.centralcart.config;

import br.com.centralcart.BungeePlugin;
import br.com.centralcart.utils.Logger;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Properties {
  private static Configuration fileConfiguration;
  private static File propertiesFile;
  private static final String fileName = "properties.yml";

  public static void setup(BungeePlugin plugin) {
    propertiesFile = new File(plugin.getDataFolder(), fileName);

    if (!propertiesFile.exists()) {
      try {
        plugin.getDataFolder().mkdirs();
        propertiesFile.createNewFile();
        Logger.info("Created new configuration file: " + propertiesFile.getPath());

        fileConfiguration = new Configuration();
        fileConfiguration.set("secret", "token-da-sua-loja");
        save();
      } catch (IOException e) {
        Logger.error("Could not create " + fileName + ": " + e.getMessage());
      }
    } else {
      load();
    }
  }

  public static void load() {
    try {
      if (propertiesFile == null) {
        Logger.error("The properties file is not initialized.");
        return;
      }

      fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class)
              .load(propertiesFile);

      if (fileConfiguration == null) {
        Logger.error("Failed to load the configuration file.");
      } else {
        Logger.info("Configuration file loaded successfully.");
        Logger.info("Secret: " + fileConfiguration.getString("secret"));
      }
    } catch (IOException e) {
      Logger.error("Could not load " + fileName + ": " + e.getMessage());
    }
  }

  public static void save() {
    try {
      if (fileConfiguration != null) {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(fileConfiguration, propertiesFile);
        Logger.info("Configuration saved successfully.");
      } else {
        Logger.error("Cannot save configuration because fileConfiguration is null.");
      }
    } catch (IOException e) {
      Logger.error("Couldn't save " + fileName + ": " + e.getMessage());
    }
  }

  public static void setValue(String key, String value) {
    if (fileConfiguration == null) {
      Logger.error("Configuration file is not loaded! Cannot set value.");
      return;
    }
    fileConfiguration.set(key, value);
    save();
  }

  public static String getValue(String key) {
    if (fileConfiguration == null) {
      Logger.error("Configuration file is not loaded! Cannot get value.");
      return null;
    }
    String value = fileConfiguration.getString(key, "default-value"); // Valor padrão se a chave não existir
    if (value.equals("default-value")) {
      Logger.error("Key '" + key + "' not found in configuration. Returning default value.");
    }
    return value;
  }

  public static void setSecret(String value) {
    setValue("secret", value);
  }

  public static String getSecret() {
    String secret = getValue("secret");
    if (secret == null || secret.isEmpty()) {
      Logger.error("The secret key is missing or empty in the configuration.");
    }
    return secret;
  }
}
