package br.com.centralcart;

import br.com.centralcart.commands.CentralCartCommand;
import br.com.centralcart.config.Properties;
import br.com.centralcart.events.PlayerJoin;
import br.com.centralcart.models.Application;
import br.com.centralcart.models.QueuedCommand;
import br.com.centralcart.updater.Updater;
import br.com.centralcart.utils.Logger;
import br.com.centralcart.utils.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;


public class BukkitPlugin extends JavaPlugin {

  private static BukkitPlugin instance;

  private static CentralCart centralCart;

  public static Set<String> processing = new HashSet<>();
  //public static boolean processing = false;

  @Override
  public void onEnable() {
    instance = this;

    new Metrics(this, 20185);

    saveDefaultConfig();

    Properties.setup(this);

    registerCommand();

    if (Properties.getSecret().equalsIgnoreCase("token-da-sua-loja")) {
      Logger.info("Parece que esta é a primeira vez que o plugin é carregado. Use /centralcart token <seu-token> " +
              "para vincular a loja com o seu servidor");
    }else {
      setupCentralCart();
    }

    getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
  }

  @Override
  public void onDisable() {
    centralCart.disableSocket();
    new Updater(this).check();
  }

  private void registerCommand() {
    try {
      final Server server = Bukkit.getServer();
      final Method mapMethod = server.getClass().getMethod("getCommandMap");

      CommandMap bukkitCommandMap = (CommandMap) mapMethod.invoke(server);
      bukkitCommandMap.register(getName(), new CentralCartCommand());

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
      throw new CommandException();
    }
  }

  private void showBanner(Application application) {
    String base64String = "CiAgIF9fX19fXyAgICAgICAgICAgICAgICAgX18gICAgICAgICAgICAgICAgICAgICBfXyAgIF9fX19fXyAgICAgICAgICAgICAgICAgICBfXyAKICAvIF9fX18vICBfX18gICAgX19fXyAgIC8gL18gICBfX19fXyAgX19fXyBfICAgLyAvICAvIF9fX18vICBfX19fIF8gICBfX19fXyAgLyAvXwogLyAvICAgICAgLyBfIFwgIC8gX18gXCAvIF9fLyAgLyBfX18vIC8gX18gYC8gIC8gLyAgLyAvICAgICAgLyBfXyBgLyAgLyBfX18vIC8gX18vCi8gL19fXyAgIC8gIF9fLyAvIC8gLyAvLyAvXyAgIC8gLyAgICAvIC9fLyAvICAvIC8gIC8gL19fXyAgIC8gL18vIC8gIC8gLyAgICAvIC9fICAKXF9fX18vICAgXF9fXy8gL18vIC9fLyBcX18vICAvXy8gICAgIFxfXyxfLyAgL18vICAgXF9fX18vICAgXF9fLF8vICAvXy8gICAgIFxfXy8gIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICB3d3cuY2VudHJhbGNhcnQuY29tLmJyIC0ge3ZlcnNpb259CiAgICAgICAgICAgICAgICAgICAgICAgTW9uZXRpemUgbyBzZXUgc2Vydmlkb3IgZGUgTWluZWNyYWZ0IQ==";

    byte[] decodedBytes = Base64.getDecoder().decode(base64String);

    String decodedString = new String(decodedBytes);

    String finalString = "§a" + decodedString.replace("{version}", "v" + getDescription().getVersion());

    Bukkit.getConsoleSender().sendMessage(finalString);
    Logger.info("Plano: §a" + application.getPlan() + " §f- Expira em: §a" + Utils.dateFormat(application.getOverdueDate()));
  }

  public void setupCentralCart() {
    Bukkit.getScheduler().cancelTasks(this);

    centralCart = new CentralCart(Properties.getSecret());

    Application application = centralCart.getApplication();

    if (application == null) return;

    showBanner(application);

    centralCart.initSocket(this::processCommands);
  }

  public void processCommands(QueuedCommand[] queuedCommands) {
    if (queuedCommands == null) return;

    for (QueuedCommand queuedCommand : queuedCommands) {
      Player player = Bukkit.getPlayer(queuedCommand.getUserId());

      if (player == null && !queuedCommand.getOfflineExecute()) continue;

      if (processing.contains(queuedCommand.getCommand())) continue;

      Bukkit.getScheduler().runTask(getInstance(), () -> {
        Logger.debug("Dispatching command -> " + queuedCommand.getCommand());

        getInstance().getServer().dispatchCommand(getInstance().getServer().getConsoleSender(), queuedCommand.getCommand());
      });

      centralCart.setCommandDispatched(queuedCommand);

      processing.remove(queuedCommand.getCommand());
    }
  }

  public static BukkitPlugin getInstance() {
    return instance;
  }

  public static CentralCart getCentralCart() {
    return centralCart;
  }
}