package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockBreakEvent implements Listener {

    private final AdvancedLockPlugin plugin;
    private final LockDataManager lockDataManager;
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;
    public BlockBreakEvent(AdvancedLockPlugin plugin, LockDataManager lockDataManager, PlayerDataManager playerDataManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.lockDataManager = lockDataManager;
        this.playerDataManager = playerDataManager;
        this.configManager = configManager;
    }

    @EventHandler
    public void onBreak(org.bukkit.event.block.BlockBreakEvent event){
        Location location = event.getBlock().getLocation();
        Player player = event.getPlayer();
        if (lockDataManager.locationExists(location)){
            event.setCancelled(true);
            if (lockDataManager.getAdmins(location).contains(player)){
                player.sendMessage(Color.format(configManager.getBlockBreakMessageAdmin()));
            } else if (lockDataManager.getUsers(location).contains(player)) {
                player.sendMessage(Color.format(configManager.getBlockBreakMessageUser()));
            } else {
                player.sendMessage(Color.format(configManager.getBlockBreakMessagePlayer()));
            }
        }


    }

}
