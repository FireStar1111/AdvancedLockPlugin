package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class onSignChange implements Listener {

    private final AdvancedLockPlugin advancedLockPlugin;
    private final ClassManager classManager;
    private final LockDataManager lockDataManager;
    private final ConfigManager configManager;
    private final PlayerDataManager playerDataManager;
    public onSignChange(AdvancedLockPlugin advancedLockPlugin) {
        this.advancedLockPlugin = advancedLockPlugin;
        this.classManager = advancedLockPlugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.configManager = classManager.getConfigManager();
        this.playerDataManager = classManager.getPlayerDataManager();
    }

    @EventHandler
    public void onChange(SignChangeEvent event){
        Player player = event.getPlayer();
        if (!lockDataManager.getOnSignEdit().containsKey(player)){
            return;
        }
        Location location = lockDataManager.getOnSignEdit().get(player);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(event.getLine(0));
        stringBuilder.append(" ");
        stringBuilder.append(event.getLine(1));


        String message = stringBuilder.toString().trim();
        Location blockLocation = player.getLocation().add(0, 1, 0);
        Block sign = blockLocation.getBlock();
        if (message.isBlank()){
            sign.setType(Material.AIR);
            removePlayer(player);
            classManager.getAdminMenu().openInv(player, location, 1);
            removePlayer(player);
            return;
        } else if (Bukkit.getOfflinePlayer(message) == null) {
            sign.setType(Material.AIR);
            player.sendMessage(Color.format(configManager.getPlayerNotFoundMessage()));
            classManager.getAdminMenu().openInv(player, location, 1);
            removePlayer(player);
            return;
        } else {

            OfflinePlayer target = Bukkit.getOfflinePlayer(message);
            if (!playerDataManager.getAllPlayers().contains(target)){
                sign.setType(Material.AIR);
                player.sendMessage(Color.format(configManager.getPlayerNotFoundMessage()));
                classManager.getAdminMenu().openInv(player, location, 1);
                removePlayer(player);
                return;
            }
            if (lockDataManager.getUsers(location).contains(target)){
                sign.setType(Material.AIR);
                player.sendMessage(Color.format(configManager.getPlayerIsAlreadyAnUserMessage()));
                classManager.getAdminMenu().openInv(player, location, 1);
                removePlayer(player);
                return;
            }
            if (playerDataManager.getinUseAmount(target.getUniqueId()) >= playerDataManager.getMaxInUseAmount(target.getUniqueId())){
                sign.setType(Material.AIR);
                player.sendMessage(Color.format(configManager.getPlayerHasMaxUsesMessage()));
                classManager.getAdminMenu().openInv(player, location, 1);
                removePlayer(player);
                return;
            }
            sign.setType(Material.AIR);
            lockDataManager.addUser(location, target, playerDataManager);
            classManager.getAdminMenu().openInv(player, location, 1);
            player.sendMessage(Color.format(configManager.getAddingUserSuccessfulMessage()));
            removePlayer(player);
        }

    }

    protected void removePlayer(Player player){
        Map<Player, Location> map = lockDataManager.getOnSignEdit();
        map.remove(player);
        lockDataManager.setOnSignEdit(map);
    }


}
