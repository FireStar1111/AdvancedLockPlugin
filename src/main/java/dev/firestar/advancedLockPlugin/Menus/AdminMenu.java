package dev.firestar.advancedLockPlugin.Menus;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import dev.firestar.advancedLockPlugin.utils.CustomPlayerHead;
import dev.firestar.advancedLockPlugin.utils.LocationtoString;
import dev.firestar.advancedLockPlugin.utils.itemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AdminMenu {

    private final AdvancedLockPlugin plugin;
    private final ClassManager classManager;
    private final LockDataManager lockDataManager;
    private final LocationtoString locationtoString;
    private final PlayerDataManager playerDataManager;

    public AdminMenu(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.locationtoString = classManager.getLocationtoString();
        this.playerDataManager = classManager.getPlayerDataManager();
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
        List<Player> users = lockDataManager.getUsers(location);
        List<Player> owners = users.stream()
                .filter(user -> lockDataManager.getOwnerUUID(location).equals(user.getUniqueId()))
                .toList();
        List<Player> admins = users.stream()
                .filter(user -> lockDataManager.getAdmins(location).contains(user))
                .filter(user -> !lockDataManager.getOwnerUUID(location).equals(user.getUniqueId()))
                .toList();
        List<Player> regularUsers = users.stream()
                .filter(user -> !lockDataManager.getAdmins(location).contains(user))
                .filter(user -> !lockDataManager.getOwnerUUID(location).equals(user.getUniqueId()))
                .toList();

        // Combineer de lijsten in de juiste volgorde
        List<Player> sortedUsers = new ArrayList<>();
        sortedUsers.addAll(owners);
        sortedUsers.addAll(admins);
        sortedUsers.addAll(regularUsers);

        // Paginering
        int startIndex = (page - 1) * 26; // Startindex voor deze pagina
        int endIndex = Math.min(startIndex + 26, sortedUsers.size()); // Eindindex binnen de lijst
        int slot = 18; // Startslot voor hoofden

        for (int i = startIndex; i < endIndex; i++) {
            Player user = sortedUsers.get(i);
            if (user != null) {
                if (owners.contains(user)) {
                    inventory.setItem(slot, itemStack.getPlayerHead(Bukkit.getOfflinePlayer(user.getUniqueId()), new String[]{Color.format("&7Owner: " + user.getName())}, Color.format("&5" + user.getName())));
                } else if (admins.contains(user)) {
                    if (!owners.contains(player)){
                        inventory.setItem(slot, itemStack.getPlayerHead(Bukkit.getOfflinePlayer(user.getUniqueId()), new String[]{Color.format("&7Admin: " + user.getName()), " ", Color.format("&7Right click to demote")}, Color.format("&a" + user.getName())));
                    } else {
                        inventory.setItem(slot, itemStack.getPlayerHead(Bukkit.getOfflinePlayer(user.getUniqueId()), new String[]{Color.format("&7Admin: " + user.getName()), " ", Color.format("&7Right click to demote"), Color.format("&cShift-right click to delete")}, Color.format("&a" + user.getName())));
                    }
                } else {
                    inventory.setItem(slot, itemStack.getPlayerHead(Bukkit.getOfflinePlayer(user.getUniqueId()), new String[]{Color.format("&7User: " + user.getName()), " ", Color.format("&7Right click to promote"), Color.format("&cShift-right click to delete")}, Color.format("&e" + user.getName())));
                }
            }
            slot++;
        }

        // Voeg "Add admin"-optie toe
        if (slot < 44) {
            inventory.setItem(slot, itemStack.createSimple(Material.OAK_SIGN, Color.format("&aAdd user"), null, false, 1));
        }

        // Voeg navigatieknoppen toe
        if (page > 1) {
            CustomPlayerHead previousPage = new CustomPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODM5OWU1ZGE4MmVmNzc2NWZkNWU0NzJmMzE0N2VkMTE4ZDk4MTg4NzczMGVhN2JiODBkN2ExYmVkOThkNWJhIn19fQ=="); // Texture voor vorige pagina
            inventory.setItem(48, previousPage.getHead());
        }
        if (endIndex < sortedUsers.size()) {
            CustomPlayerHead nextPage = new CustomPlayerHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZlYmFhNDFkMWQ0MDVlYjZiNjA4NDViYjlhYzcyNGFmNzBlODVlYWM4YTk2YTU1NDRiOWUyM2FkNmM5NmM2MiJ9fX0="); // Texture voor volgende pagina
            inventory.setItem(50, nextPage.getHead());
        }

        // Pagina-indicator
        inventory.setItem(49, itemStack.createSimple(Material.SUNFLOWER, "Page " + page, null, false, 1));

        // Open de inventory voor de speler
        player.openInventory(inventory);
    }
}