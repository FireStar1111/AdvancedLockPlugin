package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.List;
public class onLeaveListener implements Listener {

    private final AdvancedLockPlugin plugin;
    private final LockDataManager lockDataManager;
    private final ClassManager classManager;

    public onLeaveListener(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (lockDataManager.getBezig().contains(player)){
            List<Player> bezig = lockDataManager.getBezig();
            bezig.remove(player);
            lockDataManager.setBezig(bezig);
        }

    }

}
