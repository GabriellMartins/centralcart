package br.com.centralcart.events;

import br.com.centralcart.BukkitPlugin;
import br.com.centralcart.models.QueuedCommand;
import br.com.centralcart.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p =  event.getPlayer();

        if (p.getName() == null) return;

        Logger.debug("Process commands of " + p.getName());

        Bukkit.getScheduler().runTaskAsynchronously(BukkitPlugin.getInstance(), () -> {
            QueuedCommand[] playerCommands = BukkitPlugin.getCentralCart().getPlayerQueuedCommands(p.getName());
            BukkitPlugin.getInstance().processCommands(playerCommands);
        });
    }
}
