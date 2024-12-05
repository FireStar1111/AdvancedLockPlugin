package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.List;
import java.util.Map;

public class onMoveEvent implements Listener {

    private final AdvancedLockPlugin advancedLockPlugin;
    public onMoveEvent(AdvancedLockPlugin advancedLockPlugin) {
        this.advancedLockPlugin = advancedLockPlugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if (advancedLockPlugin.getClassManager().getLockDataManager().getOnSignEdit().containsKey(event.getPlayer())){

            advancedLockPlugin.getClassManager().getAdminMenu().openInv(event.getPlayer(), advancedLockPlugin.getClassManager().getLockDataManager().getOnSignEdit().get(event.getPlayer()), 1);
            Map<Player, Location> map = advancedLockPlugin.getClassManager().getLockDataManager().getOnSignEdit();
            map.remove(event.getPlayer());
            advancedLockPlugin.getClassManager().getLockDataManager().setOnSignEdit(map);
        }


    }

}
