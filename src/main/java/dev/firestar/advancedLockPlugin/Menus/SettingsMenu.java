package dev.firestar.advancedLockPlugin.Menus;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import dev.firestar.advancedLockPlugin.utils.LocationtoString;
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
    private final ClassManager classManager;
    private final LocationtoString locationtoString;
    public SettingsMenu(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.locationtoString = classManager.getLocationtoString();
    }

    public void openMenu(Player player, Location location){
        Inventory inventory = Bukkit.createInventory(player, 27, "Lock-settings");
        Material material = lockDataManager.getBlock(location);

        String[] lore2 = new String[2];
        lore2[0] = " ";
        lore2[1] = Color.format("&7Click to delete this lock");
        ItemStack deleteButton = itemStack.create("&cDelete", Material.BARRIER, 1, lore2, true, false, false, false, null, 0);
        ItemStack deleteButton2 = itemStack.addNameSpaceKey(deleteButton, "delbutton", "location", locationtoString.convert(location));
        inventory.setItem(11, deleteButton2);



        String[] lore1 = new String[3];
        lore1[0] = Color.format("&7Click to unlock");
        lore1[1] = " ";
        if (lockDataManager.isLocked(location)){
            lore1[0] = Color.format("&7Click to unlock");
            lore1[1] = " ";
            lore1[2] = Color.format("&cLocked");
        } else {
            lore1[0] = Color.format("&7Click to lock");
            lore1[1] = " ";
            lore1[2] = Color.format("&aUnlocked");
        }
        ItemStack lockSettingsButton = itemStack.create("&aLock Settings", material, 1, lore1, true, false, true, false, null, 0);
        ItemStack lockSettingsButton2 = itemStack.addNameSpaceKey(lockSettingsButton, "locksettingsbutton", "location", locationtoString.convert(location));
        inventory.setItem(13, lockSettingsButton2);

        String[] lore3 = new String[2];
        lore3[0] = " ";
        lore3[1] = Color.format("&7Click to view admin and users");


        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(lockDataManager.getOwnerUUID(location));
        ItemStack item2 = itemStack.getPlayerHead(offlinePlayer, lore3, Color.format("&aManage admins"));
        ItemStack item = itemStack.addNameSpaceKey(item2, "adminbutton", "location", locationtoString.convert(location));
        inventory.setItem(15, item);
        player.openInventory(inventory);
    }

}
