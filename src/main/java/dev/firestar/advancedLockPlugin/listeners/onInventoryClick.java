package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import dev.firestar.advancedLockPlugin.utils.LocationtoString;
import dev.firestar.advancedLockPlugin.utils.itemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class onInventoryClick implements Listener {

    private final AdvancedLockPlugin advancedLockPlugin;
    private ClassManager classManager;
    private PlayerDataManager playerDataManager;
    private ConfigManager configManager;
    private LockDataManager lockDataManager;
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
                        lockDataManager.deleteLock(location);
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
                lockDataManager.deleteLock(location);
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
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack.getType().equals(Material.PLAYER_HEAD) && itemStack.getItemMeta().getDisplayName() != null){



            }


        }


    }

}
