package br.com.centralcart;

import br.com.centralcart.commands.CentralCartCommandBungee;
import br.com.centralcart.config.BungeeProperties;
import br.com.centralcart.config.Properties;
import br.com.centralcart.models.Application;
import br.com.centralcart.models.QueuedCommand;
import br.com.centralcart.utils.Logger;
import br.com.centralcart.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class BungeePlugin extends Plugin {

  private static BungeePlugin instance;
  CentralCart centralCart;
  private static Configuration config;
  public static Set<String> processing = new HashSet<>();

  @Override
  public void onEnable() {
    Properties.setup(this);

    instance = this;

    new Metrics(this, 20185);

    if (!getDataFolder().exists()) getDataFolder().mkdir();
    File file = new File(getDataFolder(), "config.yml");
    try {
      if (!file.exists())
        Files.copy(getResourceAsStream("config.yml"), file.toPath());

      config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
    } catch (IOException e) {
      e.printStackTrace();
    }

    BungeeProperties.setup(this);

    getProxy().getPluginManager().registerCommand(this, new CentralCartCommandBungee());

    if (BungeeProperties.getSecret().equalsIgnoreCase("token-da-sua-loja")) {
      Logger.info("Parece que esta é a primeira vez que o plugin é carregado. Use /centralcart token <seu-token> " +
              "para vincular a loja com o seu servidor");
    }else {
      setupCentralCart();
    }
  }

  @Override
  public void onDisable() {
    centralCart.disableSocket();
  }

  private void showBanner(Application application) {
    String base64String = "CiAgIF9fX19fXyAgICAgICAgICAgICAgICAgX18gICAgICAgICAgICAgICAgICAgICBfXyAgIF9fX19fXyAgICAgICAgICAgICAgICAgICBfXyAKICAvIF9fX18vICBfX18gICAgX19fXyAgIC8gL18gICBfX19fXyAgX19fXyBfICAgLyAvICAvIF9fX18vICBfX19fIF8gICBfX19fXyAgLyAvXwogLyAvICAgICAgLyBfIFwgIC8gX18gXCAvIF9fLyAgLyBfX18vIC8gX18gYC8gIC8gLyAgLyAvICAgICAgLyBfXyBgLyAgLyBfX18vIC8gX18vCi8gL19fXyAgIC8gIF9fLyAvIC8gLyAvLyAvXyAgIC8gLyAgICAvIC9fLyAvICAvIC8gIC8gL19fXyAgIC8gL18vIC8gIC8gLyAgICAvIC9fICAKXF9fX18vICAgXF9fXy8gL18vIC9fLyBcX18vICAvXy8gICAgIFxfXyxfLyAgL18vICAgXF9fX18vICAgXF9fLF8vICAvXy8gICAgIFxfXy8gIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICB3d3cuY2VudHJhbGNhcnQuY29tLmJyIC0ge3ZlcnNpb259CiAgICAgICAgICAgICAgICAgICAgICAgTW9uZXRpemUgbyBzZXUgc2Vydmlkb3IgZGUgTWluZWNyYWZ0IQ==";

    byte[] decodedBytes = Base64.getDecoder().decode(base64String);

    String decodedString = new String(decodedBytes);

    String finalString = "§a" + decodedString.replace("{version}", "v" + getDescription().getVersion());

    getLogger().info(finalString);
    Logger.info("Plano: §a" + application.getPlan() + " §f- Expira em: §a" + Utils.dateFormat(application.getOverdueDate()));
  }

  public void setupCentralCart() {
    getProxy().getScheduler().cancel(this);

    centralCart = new CentralCart(BungeeProperties.getSecret());

    Application application = centralCart.getApplication();

    if (application == null) return;

    showBanner(application);

    centralCart.initSocket(this::processCommands);
  }

  public void processCommands(QueuedCommand[] queuedCommands) {
    if (queuedCommands == null) return;

    for (QueuedCommand queuedCommand : queuedCommands) {
      ProxiedPlayer player = getProxy().getPlayer(queuedCommand.getOrder().getClientIdentifier());

      if (player == null && !queuedCommand.getOfflineExecute()) continue;

      if (processing.contains(queuedCommand.getCommand())) continue;

      Logger.debug("Dispatching command -> " + queuedCommand.getCommand());
      getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), queuedCommand.getCommand());

      centralCart.setCommandDispatched(queuedCommand);

      processing.remove(queuedCommand.getCommand());
    }
  }

  public static Configuration getConfig() {
    return config;
  }

  public static BungeePlugin getInstance() {
    return instance;
  }
}
