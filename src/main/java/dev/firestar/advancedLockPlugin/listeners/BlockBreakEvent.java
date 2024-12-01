package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
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
    private final ClassManager classManager;
    public BlockBreakEvent(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.playerDataManager = classManager.getPlayerDataManager();
        this.configManager = classManager.getConfigManager();
    }

    @EventHandler
    public void onBreak(org.bukkit.event.block.BlockBreakEvent event){
        Location location = event.getBlock().getLocation();
        Player player = event.getPlayer();
        if (lockDataManager.locationExists(location)){
            event.setCancelled(true);
            if (lockDataManager.getBezig().contains(player)){
                event.setCancelled(true);
            }
            if (lockDataManager.getAdmins(location).contains(player)){
                player.sendMessage(Color.format(configManager.getBlockBreakMessageAdmin()));
            } else if (lockDataManager.getUsers(location).contains(player)) {
                player.sendMessage(Color.format(configManager.getBlockBreakMessageUser()));
            } else {
                player.sendMessage(Color.format(configManager.getBlockBreakMessagePlayer()));
            }
        } else if (lockDataManager.getBezig().contains(player)){
            event.setCancelled(true);
        }


    }

}
