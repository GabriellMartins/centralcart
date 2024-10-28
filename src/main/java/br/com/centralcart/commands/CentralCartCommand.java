package br.com.centralcart.commands;

import br.com.centralcart.BukkitPlugin;
import br.com.centralcart.config.Properties;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CentralCartCommand extends BukkitCommand {

  public CentralCartCommand() {
    super("centralcart");
  }

  private void sendHelp(CommandSender sender) {
    sender.sendMessage("");
    sender.sendMessage("§aCentralCart - Ajuda");
    sender.sendMessage("§a/centralcart token <seu-token> §f- Vincula a sua loja com servidor.");
    sender.sendMessage("");
  }

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    if (!(sender instanceof ConsoleCommandSender)) {
      sender.sendMessage("Este comando deve ser executado no console.");
      return true;
    }

    if (args.length == 0) {
      sendHelp(sender);
      return true;
    }

    String command = args[0].toUpperCase();
    switch (command) {
      case "TOKEN":
        if (args.length < 2) {
          sender.sendMessage("§cInforme o token da sua loja.");
          return true;
        }

        Properties.setSecret(args[1]);

        BukkitPlugin.getInstance().setupCentralCart();

        break;

      default:
        sendHelp(sender);
        break;
    }

    return false;
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
    List<String> defaultList = super.tabComplete(sender, alias, args);

    if (!(sender instanceof ConsoleCommandSender)) return defaultList;

    List<String> match = new ArrayList<>();
    if (args.length == 1) {

      List<String> availableArgs = Arrays.asList("token");

      StringUtil.copyPartialMatches(args[0], availableArgs, match);
      Collections.sort(match);

      return match;
    }

    return defaultList;
  }
}
