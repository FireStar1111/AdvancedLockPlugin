package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
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
    private final ClassManager classManager;
    public onJoinEvent(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.playerDataManager = classManager.getPlayerDataManager();
        this.configManager = classManager.getConfigManager();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (!playerDataManager.playerExists(player)){
            playerDataManager.setupPlayer(player, 0, configManager.getPlayerMaxLockAmount(), 0,configManager.getPlayerMaxUseAmount());
        }



    }

}
