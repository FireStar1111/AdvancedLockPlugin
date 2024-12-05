package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import dev.firestar.advancedLockPlugin.utils.LocationtoString;
import dev.firestar.advancedLockPlugin.utils.Sign;
import dev.firestar.advancedLockPlugin.utils.itemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class onInventoryClick implements Listener {

    private final AdvancedLockPlugin advancedLockPlugin;
    private final ClassManager classManager;
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;
    private final LockDataManager lockDataManager;
    private final LocationtoString locationtoString;
    public onInventoryClick(AdvancedLockPlugin advancedLockPlugin) {
        this.advancedLockPlugin = advancedLockPlugin;
        this.classManager = advancedLockPlugin.getClassManager();
        this.playerDataManager = classManager.getPlayerDataManager();
        this.configManager = classManager.getConfigManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.locationtoString = classManager.getLocationtoString();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (event.getCurrentItem() == null) return;
        if (event.getView().getTitle().equals("Lock-settings")){
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            switch (event.getRawSlot()){

                case 11 -> {
                    ItemStack delButton = event.getCurrentItem();
                    Location location = locationtoString.convertToLocation(itemStack.getNameSpaceKey(delButton, "delbutton", "location"));
                    if (!configManager.getSubmitMenu()){
                        lockDataManager.deleteLock(location, playerDataManager);
                        player.sendMessage(Color.format(configManager.getSuccessLockDeleteMessage()));
                        player.closeInventory();
                        return;
                    }
                    classManager.getSubmitMenu().openInv(player, location);
                    return;
                }
                case 13 -> {
                    ItemStack item = event.getCurrentItem();
                    String locationKey = itemStack.getNameSpaceKey(item, "locksettingsbutton", "location");
                    Location location = locationtoString.convertToLocation(locationKey);
                    if (lockDataManager.isLocked(location)){
                        lockDataManager.setLocked(location, false);
                        player.sendMessage(Color.format(configManager.getDisableLockMessage()));
                        classManager.getSettingsMenu().openMenu(player, location);
                    } else {
                        lockDataManager.setLocked(location, true);
                        player.sendMessage(Color.format(configManager.getEnabledLockMessage()));
                        classManager.getSettingsMenu().openMenu(player, location);
                    }
                }
                case 15 -> {
                    ItemStack item = event.getCurrentItem();
                    String locationKey = itemStack.getNameSpaceKey(item, "adminbutton", "location");
                    Location location = locationtoString.convertToLocation(locationKey);
                    classManager.getAdminMenu().openInv(player, location, 1);

                }

            }


        } else if (event.getView().getTitle().equals("Confirm delete")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if (item.getType().equals(Material.EMERALD_BLOCK)){
                Location location = locationtoString.convertToLocation(itemStack.getNameSpaceKey(item, "successbutton", "location"));
                lockDataManager.deleteLock(location, playerDataManager);
                player.sendMessage(Color.format(configManager.getSuccessLockDeleteMessage()));
                player.closeInventory();
                return;
            } else if (item.getType().equals(Material.REDSTONE_BLOCK)) {
                player.sendMessage(Color.format(configManager.getCancelLockDeleteMessage()));
                player.closeInventory();
                return;
            }


        } else if (event.getView().getTitle().equals("Manage admins")) {
            event.setCancelled(true);
            ItemStack pagebutton = event.getClickedInventory().getItem(49);
            Location location = locationtoString.convertToLocation(itemStack.getNameSpaceKey(pagebutton, "pagebutton", "location"));
            ItemStack itemStack = event.getCurrentItem();
            int currentPage = Integer.parseInt(pagebutton.getItemMeta().getDisplayName().replace("Page", "").trim());
            if (itemStack.getType().equals(Material.OAK_SIGN)){
                Sign.openCustomSign((Player) event.getWhoClicked(), advancedLockPlugin);
                Map<Player, Location> map = lockDataManager.getOnSignEdit();
                map.put((Player) event.getWhoClicked(), location);
                lockDataManager.setOnSignEdit(map);
            } else if (itemStack.getItemMeta().getDisplayName().equals(Color.format("&cReturn")) ||
                itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("Return")){
                classManager.getSettingsMenu().openMenu((Player) event.getWhoClicked(), location);
            }
            if (itemStack.getItemMeta().getDisplayName() != null){
                UUID targetId = UUID.fromString(dev.firestar.advancedLockPlugin.utils.itemStack.getNameSpaceKey(event.getCurrentItem(), "player", "id"));
                if (targetId == null || Bukkit.getOfflinePlayer(targetId) == null){
                    if (itemStack.getItemMeta().getDisplayName().equals(Color.format("&7<< Previous page"))){
                        classManager.getAdminMenu().openInv((Player) event.getWhoClicked(), location, currentPage - 1);

                    } else if (itemStack.getItemMeta().getDisplayName().equals(Color.format("&7Next page >>"))) {
                        classManager.getAdminMenu().openInv((Player) event.getWhoClicked(), location, currentPage + 1);
                    }
                    return;
                }
                Player player = (Player) event.getWhoClicked();
                if (player.getUniqueId().equals(targetId)){
                    player.sendMessage(Color.format(configManager.getManageYourselfMessage()));
                    return;
                }
                if (lockDataManager.getOwnerUUID(location).equals(targetId)) {

                } else if (lockDataManager.getAdmins(location).contains(Bukkit.getOfflinePlayer(targetId))) {
                    if (event.getClick().equals(ClickType.LEFT)){
                        lockDataManager.removeAdmin(location, targetId);
                        player.sendMessage(Color.format(configManager.getDemoteMessage()));
                        classManager.getAdminMenu().openInv(player, location, currentPage);
                    } else if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                        lockDataManager.removeAdmin(location, targetId);
                        lockDataManager.removeUser(location, targetId, playerDataManager);
                        classManager.getAdminMenu().openInv(player, location, currentPage);
                        player.sendMessage(Color.format(configManager.getUserDeleteSuccesMessage()));
                    }


                } else {
                    if (lockDataManager.getAdmins(location).contains(Bukkit.getOfflinePlayer(player.getUniqueId()))){
                        if (event.getClick().equals(ClickType.SHIFT_RIGHT)){
                            lockDataManager.removeUser(location, targetId, playerDataManager);
                            classManager.getAdminMenu().openInv(player, location, currentPage);
                            player.sendMessage(Color.format(configManager.getUserDeleteSuccesMessage()));
                        }
                        if (event.getClick().equals(ClickType.RIGHT)){
                            lockDataManager.addAdmin(location, targetId);
                            player.sendMessage(Color.format(configManager.getPromoteMessage()));
                            classManager.getAdminMenu().openInv(player, location, currentPage);
                        }

                    } else {
                        return;
                    }


                }


            }


        }


    }

}
