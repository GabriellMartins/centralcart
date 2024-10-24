//package br.com.centralcart.events;
//
//import br.com.centralcart.BungeePlugin;
//import br.com.centralcart.models.QueuedCommand;
//import br.com.centralcart.utils.Logger;
//import org.bukkit.Bukkit;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerJoinEvent;
//
//public class PlayerJoin implements Listener {
//  @EventHandler
//  public void onPlayerJoin(PlayerJoinEvent event) {
//    Player p =  event.getPlayer();
//
//    if (p.getName() == null) return;
//
//    Logger.debug("Process commands of " + p.getName());
//
//    Bukkit.getScheduler().runTaskAsynchronously(BungeePlugin.getInstance(), () -> {
//      QueuedCommand[] playerCommands = BungeePlugin.getCentralCart().getPlayerQueuedCommands(p.getName());
//      BungeePlugin.getInstance().processCommands(playerCommands);
//    });
//  }
//}
