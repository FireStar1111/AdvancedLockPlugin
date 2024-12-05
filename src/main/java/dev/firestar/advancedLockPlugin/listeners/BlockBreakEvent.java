package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Block;
import dev.firestar.advancedLockPlugin.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockBreakEvent implements Listener {

    private final AdvancedLockPlugin plugin;
    private final LockDataManager lockDataManager;
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;
    private final ClassManager classManager;
    public BlockBreakEvent(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.playerDataManager = classManager.getPlayerDataManager();
        this.configManager = classManager.getConfigManager();
    }

    @EventHandler
    public void onBreak(org.bukkit.event.block.BlockBreakEvent event){
        Location location = event.getBlock().getLocation();
        Player player = event.getPlayer();
        Location linkedLocation = dev.firestar.advancedLockPlugin.utils.Block.isChestLinked(location) ? Block.getLinkedChestLocation(location) : null;
        if (lockDataManager.getBezig().contains(player)){
            event.setCancelled(true);
            return;
        }
        if (lockDataManager.getInspect().contains(player)){
            event.setCancelled(true);
            return;
        }
        if (!lockDataManager.locationExists(location)) {
            if (linkedLocation != null && lockDataManager.locationExists(linkedLocation)) {
                handle(event, player, linkedLocation);
            }
            return;

        }
        handle(event, player, location);

    }

    private void handle(org.bukkit.event.block.BlockBreakEvent event, Player player, Location location){
        event.setCancelled(true);
        if (lockDataManager.getBezig().contains(player)) {
            event.setCancelled(true);
        }
        if (lockDataManager.getAdmins(location).contains(Bukkit.getOfflinePlayer(player.getUniqueId()))) {
            player.sendMessage(Color.format(configManager.getBlockBreakMessageAdmin()));
        } else if (lockDataManager.getUsers(location).contains(Bukkit.getOfflinePlayer(player.getName()))) {
            player.sendMessage(Color.format(configManager.getBlockBreakMessageUser()));
        } else {
            player.sendMessage(Color.format(configManager.getBlockBreakMessagePlayer()));
        }
    }

    private boolean isProtected(Location location) {
        return lockDataManager.getAllLockLocations().contains(location);
    }
    @EventHandler
    public void explodeBlock(BlockExplodeEvent event){
        for (org.bukkit.block.Block block : event.blockList()){
            Location linkedLocation = Block.isChestLinked(block.getLocation()) ? Block.getLinkedChestLocation(block.getLocation()) : null;
            if (isProtected(block.getLocation())){
                event.blockList().remove(block);
            } else if (linkedLocation != null && lockDataManager.locationExists(linkedLocation)){
                event.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (org.bukkit.block.Block block : event.blockList()){
            Location linkedLocation = Block.isChestLinked(block.getLocation()) ? Block.getLinkedChestLocation(block.getLocation()) : null;
            if (isProtected(block.getLocation())){
                event.blockList().remove(block);
            } else if (linkedLocation != null && lockDataManager.locationExists(linkedLocation)){
                event.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Location location : event.getBlocks().stream().map(block -> block.getLocation()).toList()) {
            Location linkedLocation = Block.isChestLinked(location) ? Block.getLinkedChestLocation(location) : null;
            if (isProtected(location)) {
                event.setCancelled(true);
                return;
            } else if (linkedLocation != null && lockDataManager.locationExists(linkedLocation)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Location location : event.getBlocks().stream().map(block -> block.getLocation()).toList()) {
            Location linkedLocation = Block.isChestLinked(location) ? Block.getLinkedChestLocation(location) : null;
            if (isProtected(location)) {
                event.setCancelled(true);
                return;
            } else if (linkedLocation != null && lockDataManager.locationExists(linkedLocation)) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
