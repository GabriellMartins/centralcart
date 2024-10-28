package br.com.centralcart.updater;

import br.com.centralcart.utils.Logger;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Updater {

  private final String META_URL = "https://cdn.centralcart.com.br/plugins/minecraft/meta.json";
  private final String PLUGIN_URL = "https://cdn.centralcart.com.br/plugins/minecraft/CentralCart.jar";

  private final JavaPlugin plugin;
  private final File savePath;

  OkHttpClient client = new OkHttpClient();

  public Updater(JavaPlugin plugin) {
    this.plugin = plugin;
    this.savePath = new File(plugin.getDataFolder().getParentFile(), "CentralCart.jar");
  }

  public void check() {
    Request request = new Request.Builder()
            .url(META_URL)
            .build();

    String currentVersion = null;
    try (okhttp3.Response response = client.newCall(request).execute()) {
      assert response.body() != null;
      JsonElement pluginData = JsonParser.parseString(response.body().string()).getAsJsonArray().get(0);
      String version = pluginData.getAsJsonObject().get("version").getAsString();
      String build = pluginData.getAsJsonObject().get("build").getAsString();

      currentVersion = version + "-" + build;
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (!plugin.getDescription().getVersion().equals(currentVersion)) {
      Logger.info("A new version is available: " + currentVersion);
      download(PLUGIN_URL);
    }
  }

  public void download(String pluginUrl) {
    Logger.info("Downloading new plugin version...");
    try {
      URL url = new URL(pluginUrl);

      URLConnection connection = url.openConnection();
      int fileSize = connection.getContentLength();

      try (InputStream in = connection.getInputStream();
           FileOutputStream out = new FileOutputStream(this.savePath)) {

        byte[] buffer = new byte[4096];
        int bytesRead;
        long totalBytesRead = 0;

        while ((bytesRead = in.read(buffer)) != -1) {
          out.write(buffer, 0, bytesRead);
          totalBytesRead += bytesRead;
        }
      }
      Logger.info("Download completed successfully!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
