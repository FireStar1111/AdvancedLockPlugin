package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.Menus.SettingsMenu;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Block;
import dev.firestar.advancedLockPlugin.utils.Color;
import dev.firestar.advancedLockPlugin.utils.LockPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class onInteract implements Listener {

    private final AdvancedLockPlugin advancedLockPlugin;
    private final LockDataManager lockDataManager;
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;
    private final ClassManager classManager;
    public onInteract(AdvancedLockPlugin advancedLockPlugin) {
        this.advancedLockPlugin = advancedLockPlugin;
        this.classManager = advancedLockPlugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.playerDataManager = classManager.getPlayerDataManager();
        this.configManager = classManager.getConfigManager();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if (event.getClickedBlock() == null) return;
        Player bukkitPlayer = event.getPlayer();
        LockPlayer lockPlayer = new LockPlayer(event.getPlayer(), playerDataManager.getLockAmount(bukkitPlayer.getUniqueId()), playerDataManager.getMaxLockAmount(bukkitPlayer.getUniqueId()));
        Location location = event.getClickedBlock().getLocation();
        Material material = event.getClickedBlock().getType();
        Location linkedLocation = Block.isChestLinked(location) ? Block.getLinkedChestLocation(location) : null;

        if (lockDataManager.locationExists(location)) {
            handle(event, location, lockPlayer);
        } else {
            if (linkedLocation != null && lockDataManager.locationExists(linkedLocation)){
                handle(event, linkedLocation, lockPlayer);
                return;
            }
            if (lockDataManager.getBezig().contains(bukkitPlayer)){
                if (!getAllowedMaterial().contains(material)){
                    bukkitPlayer.sendMessage(Color.format(configManager.getInvalidBlockMessage()));
                    return;
                }
                event.setCancelled(true);
                handleCreate(event, lockPlayer, location, material);
                return;
            }

        }



    }

    public void handle(PlayerInteractEvent event, Location location, LockPlayer lockPlayer) {
        Player bukkitPlayer = event.getPlayer();
        Material material = event.getClickedBlock().getType();
        if (lockDataManager.getBezig().contains(bukkitPlayer)){
            bukkitPlayer.sendMessage(Color.format(configManager.getAlreadyLockedMessage()));
            event.setCancelled(true);
            return;
        }
        if (lockDataManager.getInspect().contains(bukkitPlayer)){
            List<Player> inspect = lockDataManager.getInspect();
            inspect.remove(bukkitPlayer);
            lockDataManager.setInspect(inspect);
            boolean bool = lockDataManager.isLocked(location);
            lockPlayer.sendMessage("&a&lLock info:");
            lockPlayer.sendMessage("&e-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            lockPlayer.sendMessage("Owner: &e" + Bukkit.getOfflinePlayer(lockDataManager.getOwnerUUID(location)).getName());
            lockPlayer.sendMessage("Location World: &e" + location.getWorld().getName());
            lockPlayer.sendMessage("Location X: &e" + location.getX());
            lockPlayer.sendMessage("Location Y: &e" + location.getY());
            lockPlayer.sendMessage("Location Z: &e" + location.getZ());
            lockPlayer.sendMessage("Block: &e" + lockDataManager.getBlock(location));
            if (bool){
                lockPlayer.sendMessage("Locked: &ayes");
            } else {
                lockPlayer.sendMessage("Locked: &cno");
            }
            lockPlayer.sendMessage(" ");
            lockPlayer.sendMessage("&eAdmins:");
            for (OfflinePlayer admins : lockDataManager.getAdmins(location)){
                if (admins.getUniqueId().equals(lockDataManager.getOwnerUUID(location))) continue;
                lockPlayer.sendMessage(" - &a" + admins.getName());
            }
            lockPlayer.sendMessage(" ");
            lockPlayer.sendMessage("&eUsers:");
            for (OfflinePlayer users : lockDataManager.getAdmins(location)){
                if (lockDataManager.getAdmins(location).contains(users)) continue;
                lockPlayer.sendMessage(" - &e" + users.getName());
            }
            lockPlayer.sendMessage("&e-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if (lockDataManager.getAdmins(location) != null && lockDataManager.getAdmins(location).contains(Bukkit.getOfflinePlayer(bukkitPlayer.getUniqueId()))){
                return;
            } else if (lockDataManager.getUsers(location) != null && lockDataManager.getUsers(location).contains(Bukkit.getOfflinePlayer(bukkitPlayer.getUniqueId()))) {
                return;
            } else {
                if (lockDataManager.getForce().contains(bukkitPlayer)){
                    return;
                }
                if (!lockDataManager.isLocked(location)){
                    return;
                }
                event.setCancelled(true);
                if (configManager.isActionBarForLockedMessage()){
                    bukkitPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Color.format(configManager.getActionBarMessage())));
                }
                bukkitPlayer.sendMessage(Color.format(configManager.getBlockIsLockedMessage()));
                return;
            }
        } else {
            if (lockDataManager.getAdmins(location).contains(Bukkit.getOfflinePlayer(bukkitPlayer.getUniqueId())) || lockDataManager.getOwnerUUID(location).equals(bukkitPlayer.getUniqueId())){
                advancedLockPlugin.getClassManager().getSettingsMenu().openMenu(bukkitPlayer, location);
                return;
            }
        }
        event.setCancelled(true);
    }


    public void handleCreate(PlayerInteractEvent event, LockPlayer lockPlayer, Location location, Material material){
        List<Player> bezig = lockDataManager.getBezig();
        bezig.remove(event.getPlayer());
        lockDataManager.setBezig(bezig);
        playerDataManager.setLockAmount(lockPlayer.getUUID(), playerDataManager.getLockAmount(lockPlayer.getUUID()) + 1);
        lockDataManager.setupLock(material, location, lockPlayer.getUUID(), true, playerDataManager);
        lockPlayer.sendMessage(Color.format(configManager.getSuccesMessageCreate()));
        advancedLockPlugin.getClassManager().getSettingsMenu().openMenu(event.getPlayer(), location);


    }


    public List<Material> getAllowedMaterial(){
        List<Material> list = new ArrayList<>();
        list.add(Material.CHEST);
        list.add(Material.FURNACE);
        list.add(Material.ENCHANTING_TABLE);
        list.add(Material.ANVIL);
        list.add(Material.CHIPPED_ANVIL);
        list.add(Material.SMOKER);
        list.add(Material.CHEST_MINECART);
        list.add(Material.CRAFTING_TABLE);
        list.add(Material.SMITHING_TABLE);
        list.add(Material.CARTOGRAPHY_TABLE);
        list.add(Material.HOPPER);
        list.add(Material.TRAPPED_CHEST);
        list.add(Material.HOPPER_MINECART);
        list.add(Material.BLAST_FURNACE);
        list.add(Material.BARREL);
        list.add((Material.SHULKER_BOX));
        return list;
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        org.bukkit.block.Block placedBlock = event.getBlockPlaced();
        Player player = event.getPlayer();
        if (lockDataManager.getBezig().contains(player)){
            return;
        }
        Location placedLocation = placedBlock.getLocation();

        if (placedBlock.getType() == Material.CHEST || placedBlock.getType() == Material.TRAPPED_CHEST) {

            for (Location adjacentLocation : getAdjacentLocations(placedLocation)) {

                if (lockDataManager.locationExists(adjacentLocation)) {
                    if (Block.isChestLinked(adjacentLocation)){
                        return;
                    }
                    if (lockDataManager.getAdmins(adjacentLocation).contains(Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId()))){
                        return;
                    }
                    event.setCancelled(true);
                    player.sendMessage(Color.format(configManager.getCantCombineMessage()));
                    return;
                }
            }

        }
    }

    // Methode om de naburige locaties van de geplaatste kist te krijgen
    private List<Location> getAdjacentLocations(Location location) {
        List<Location> adjacentLocations = new ArrayList<>();

        // Voeg de 6 naburige locaties toe
        adjacentLocations.add(location.clone().add(1, 0, 0)); // Rechts
        adjacentLocations.add(location.clone().add(-1, 0, 0)); // Links
        adjacentLocations.add(location.clone().add(0, 1, 0)); // Boven
        adjacentLocations.add(location.clone().add(0, -1, 0)); // Beneden
        adjacentLocations.add(location.clone().add(0, 0, 1)); // Voor
        adjacentLocations.add(location.clone().add(0, 0, -1)); // Achter

        return adjacentLocations;
    }
}
