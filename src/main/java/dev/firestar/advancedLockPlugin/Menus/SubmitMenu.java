package dev.firestar.advancedLockPlugin.Menus;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import dev.firestar.advancedLockPlugin.utils.LocationtoString;
import dev.firestar.advancedLockPlugin.utils.itemStack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SubmitMenu {

    private final AdvancedLockPlugin plugin;
    private final LockDataManager lockDataManager;
    private final ClassManager classManager;
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;
    private final LocationtoString locationtoString;
    public SubmitMenu(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.configManager = classManager.getConfigManager();
        this.playerDataManager = classManager.getPlayerDataManager();
        this.locationtoString = classManager.getLocationtoString();
    }

    public void openInv(Player player, Location location){
        Inventory inventory = Bukkit.createInventory(player, 27, "Confirm delete");
        ItemStack success2;
        success2 = itemStack.createSimple(Material.EMERALD_BLOCK, ChatColor.GREEN.toString() + ChatColor.BOLD + "Confirm", Arrays.asList(ChatColor.translateAlternateColorCodes('&', " "), ChatColor.translateAlternateColorCodes('&', "&7You can't undo this!")), false, 1);
        ItemStack success = itemStack.addNameSpaceKey(success2, "successbutton", "location", locationtoString.convert(location));

        for (int i = 0; i < 3; i++){
            inventory.setItem(i, success);
        }
        for (int i = 9; i < 12; i++){
            inventory.setItem(i, success);
        }
        for (int i = 18; i < 21; i++){
            inventory.setItem(i, success);
        }
        ItemStack unsuccess;
        unsuccess = itemStack.createSimple(Material.REDSTONE_BLOCK, ChatColor.RED.toString() + ChatColor.BOLD + "Cancel", Arrays.asList(ChatColor.translateAlternateColorCodes('&', " "), ChatColor.translateAlternateColorCodes('&', "&7Click to cancel")), false, 1);
        for (int i = 6; i < 9; i++){
            inventory.setItem(i, unsuccess);
        }
        for (int i = 15; i < 18; i++){
            inventory.setItem(i, unsuccess);
        }
        for (int i = 24; i < 27; i++){
            inventory.setItem(i, unsuccess);
        }

        ItemStack item = itemStack.createSimple(lockDataManager.getBlock(location), Color.format("&cLock"), Arrays.asList(" ", Color.format("&7Owner: &e" + Bukkit.getPlayer(lockDataManager.getOwnerUUID(location)).getName()),
                Color.format("&7World: &e" + lockDataManager.getWorldName(location)), Color.format("&7X: &e" + location.getX()), Color.format("&7Y: &e" + location.getY()), Color.format("&7Z: &e" + location.getZ()), " ", Color.format("&cConfirm delete")), false, 1);
        inventory.setItem(13, item);

        player.openInventory(inventory);
    }

}
