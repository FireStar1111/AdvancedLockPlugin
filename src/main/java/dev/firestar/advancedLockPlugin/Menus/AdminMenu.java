package dev.firestar.advancedLockPlugin.Menus;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import dev.firestar.advancedLockPlugin.utils.CustomPlayerHead;
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

public class AdminMenu {

    private final AdvancedLockPlugin plugin;
    private final ClassManager classManager;
    private final LockDataManager lockDataManager;
    private final LocationtoString locationtoString;
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;

    public AdminMenu(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.locationtoString = classManager.getLocationtoString();
        this.playerDataManager = classManager.getPlayerDataManager();
        this.configManager = classManager.getConfigManager();
    }

    public void openInv(Player player, Location location, int page) {
        Inventory inventory = Bukkit.createInventory(player, 54, "Manage admins");
        // Voeg glasplaten toe als decoratie
        for (int i = 9; i <= 17; i++) {
            inventory.setItem(i, itemStack.createSimple(Material.GRAY_STAINED_GLASS_PANE, " ", null, false, 1));
        }
        for (int i = 36; i <= 44; i++) {
            inventory.setItem(i, itemStack.createSimple(Material.BLACK_STAINED_GLASS_PANE, " ", null, false, 1));
        }

        // Admins en users ophalen en sorteren
        List<OfflinePlayer> users = lockDataManager.getUsers(location);
        List<OfflinePlayer> owners = users.stream()
                .filter(user -> lockDataManager.getOwnerUUID(location).equals(user.getUniqueId()))
                .toList();
        List<OfflinePlayer> admins = users.stream()
                .filter(user -> lockDataManager.getAdmins(location).contains(user))
                .filter(user -> !lockDataManager.getOwnerUUID(location).equals(user.getUniqueId()))
                .toList();
        List<OfflinePlayer> regularUsers = users.stream()
                .filter(user -> !lockDataManager.getAdmins(location).contains(user))
                .filter(user -> !lockDataManager.getOwnerUUID(location).equals(user.getUniqueId()))
                .toList();

        // Combineer de lijsten in de juiste volgorde
        List<OfflinePlayer> sortedUsers = new ArrayList<>();
        sortedUsers.addAll(owners);
        sortedUsers.addAll(admins);
        sortedUsers.addAll(regularUsers);

        // Paginering
        int startIndex = (page - 1) * 26; // Startindex voor deze pagina
        int endIndex = Math.min(startIndex + 26, sortedUsers.size()); // Eindindex binnen de lijst
        int slot = 18; // Startslot voor hoofden

        for (int i = startIndex; i < endIndex; i++) {
            OfflinePlayer user = sortedUsers.get(i);
            if (user != null) {
                if (owners.contains(user)) {
                    ItemStack item = itemStack.getPlayerHead(user, new String[]{Color.format("&7Rank: &5Owner")}, Color.format("&5" + user.getName()));
                    inventory.setItem(slot, itemStack.addNameSpaceKey(item, "player", "id",user.getUniqueId().toString()));
                } else if (admins.contains(user)) {
                    if (!owners.contains(player)){
                        ItemStack item = itemStack.getPlayerHead(user, new String[]{Color.format("&7Rank: &aAdmin"), " ", Color.format("&7Right click to demote")}, Color.format("&a" + user.getName()));
                        inventory.setItem(slot, itemStack.addNameSpaceKey(item, "player", "id", user.getUniqueId().toString()));
                    } else {
                        ItemStack item = itemStack.getPlayerHead(user, new String[]{Color.format("&7Rank: &aAdmin"), " ", Color.format("&7Left click to demote"), Color.format("&cShift-right click to delete")}, Color.format("&a" + user.getName()));
                        inventory.setItem(slot, itemStack.addNameSpaceKey(item, "player", "id", user.getUniqueId().toString()));
                    }
                } else {
                    ItemStack item = itemStack.getPlayerHead(user, new String[]{Color.format("&7Rank: &eUser"), " ", Color.format("&7Right click to promote"), Color.format("&cShift-right click to delete")}, Color.format("&e" + user.getName()));
                    inventory.setItem(slot, itemStack.addNameSpaceKey(item, "player", "id", user.getUniqueId().toString()));
                }
            }
            slot++;
        }

        // Voeg "Add admin"-optie toe
        if (slot < configManager.getDefaultUserLimit() + 17) {
            inventory.setItem(slot, itemStack.createSimple(Material.OAK_SIGN, Color.format("&aAdd user"), null, false, 1));
        }

        // Voeg navigatieknoppen toe
        if (page > 1) {
            ItemStack previousPage = itemStack.createSimple(Material.ARROW, Color.format("&7<< Previous page"), null, false, 1);
            inventory.setItem(48, previousPage);
        }
        if (endIndex < sortedUsers.size()) {
            ItemStack nextPage = itemStack.createSimple(Material.ARROW, Color.format("&7Next page >>"), null, false, 1);
            inventory.setItem(50, nextPage);
        }

        // Pagina-indicator
        ItemStack item = itemStack.createSimple(Material.SUNFLOWER, "Page " + page, null, false, 1);
        inventory.setItem(49, itemStack.addNameSpaceKey(item, "pagebutton", "location", locationtoString.convert(location)));

        ItemStack returnButton = itemStack.createSimple(Material.ARROW, Color.format("&cReturn"), null, false, 1);
        inventory.setItem(45, returnButton);
        player.openInventory(inventory);
    }
}