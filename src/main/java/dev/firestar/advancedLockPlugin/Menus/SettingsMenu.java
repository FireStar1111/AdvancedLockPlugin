package dev.firestar.advancedLockPlugin.Menus;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import dev.firestar.advancedLockPlugin.utils.itemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class SettingsMenu {

    private final AdvancedLockPlugin plugin;
    private final LockDataManager lockDataManager;

    public SettingsMenu(AdvancedLockPlugin plugin, LockDataManager lockDataManager) {
        this.plugin = plugin;
        this.lockDataManager = lockDataManager;
    }

    public void openMenu(Player player, Location location){
        Inventory inventory = Bukkit.createInventory(player, 27, "Lock-settings");
        Material material = lockDataManager.getBlock(location);

        String[] lore2 = new String[2];
        lore2[0] = " ";
        lore2[1] = "&7Click to delete this lock";
        ItemStack deleteButton = itemStack.create("&cDelete", Material.BARRIER, 1, lore2, true, false, false, false, null, 0);
        inventory.setItem(11, deleteButton);



        String[] lore1 = new String[3];
        lore1[0] = Color.format("&7Click to unlock the chest");
        lore1[1] = " ";
        if (lockDataManager.isLocked(location)){
            lore1[2] = Color.format("&cLocked");
        } else {
            lore1[2] = Color.format("&aUnlocked");
        }
        ItemStack lockSettingsButton = itemStack.create("&aLock Settings", material, 1, lore1, true, false, true, false, null, 0);
        inventory.setItem(13, lockSettingsButton);

        String[] lore3 = new String[2];
        lore3[0] = " ";
        lore3[1] = Color.format("&7Click to view admin and users");


        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(lockDataManager.getOwnerUUID(location));
        ItemStack item = itemStack.getPlayerHead(offlinePlayer, lore3);
        inventory.setItem(15, item);
    }

}
