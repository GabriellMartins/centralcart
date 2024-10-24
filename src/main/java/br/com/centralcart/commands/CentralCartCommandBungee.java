package br.com.centralcart.commands;

import br.com.centralcart.BungeePlugin;
import br.com.centralcart.config.BungeeProperties;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CentralCartCommandBungee extends Command {
  public CentralCartCommandBungee() {
    super("centralcart");
  }

  private void sendHelp(CommandSender sender) {
    sender.sendMessage("");
    sender.sendMessage("§aCentralCart - Ajuda");
    sender.sendMessage("§a/centralcart token <seu-token> §f- Vincula a sua loja com servidor.");
    sender.sendMessage("");
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (sender instanceof ProxiedPlayer) {
      sender.sendMessage("Este comando deve ser executado no console.");
      return;
    }

    if (args.length == 0) {
      sendHelp(sender);
      return;
    }

    String command = args[0].toUpperCase();
    switch (command) {
      case "TOKEN":
        if (args.length < 2) {
          sender.sendMessage("§cInforme o token da sua loja.");
          return;
        }

        BungeeProperties.setSecret(args[1]);

        BungeePlugin.getInstance().setupCentralCart();

        break;

      default:
        sendHelp(sender);
        break;
    }
  }
}
