package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoinEvent implements Listener {

    private final AdvancedLockPlugin plugin;
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;
    public onJoinEvent(AdvancedLockPlugin plugin, PlayerDataManager playerDataManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
        this.configManager = configManager;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (!playerDataManager.playerExists(player)){
            playerDataManager.setupPlayer(player, 0, configManager.getPlayerMaxLockAmount(), 0,configManager.getPlayerMaxUseAmount());
        }



    }

}
