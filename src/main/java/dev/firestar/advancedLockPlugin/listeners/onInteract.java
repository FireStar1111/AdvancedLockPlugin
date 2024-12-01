package dev.firestar.advancedLockPlugin.listeners;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.Menus.SettingsMenu;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import dev.firestar.advancedLockPlugin.utils.LockPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
        LockPlayer lockPlayer = new LockPlayer(event.getPlayer(), playerDataManager.getLockAmount(bukkitPlayer), playerDataManager.getMaxLockAmount(bukkitPlayer));
        Location location = event.getClickedBlock().getLocation();
        Material material = event.getClickedBlock().getType();
        if (lockDataManager.locationExists(location)){
            if (lockDataManager.getBezig().contains(bukkitPlayer)){
                bukkitPlayer.sendMessage(Color.format(configManager.getAlreadyLockedMessage()));
                return;
            }
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if (lockDataManager.getAdmins(location).contains(bukkitPlayer)){
                    return;
                } else if (lockDataManager.getUsers(location).contains(bukkitPlayer)) {
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
                if (lockDataManager.getAdmins(location).contains(bukkitPlayer) || lockDataManager.getOwnerUUID(location).equals(bukkitPlayer.getUniqueId())){
                    advancedLockPlugin.getClassManager().getSettingsMenu().openMenu(bukkitPlayer, location);
                    return;
                }
            }


        } else {
            if (!lockDataManager.getBezig().contains(bukkitPlayer)){
                return;
            }
            if (!getAllowedMaterial().contains(material)){
                bukkitPlayer.sendMessage(Color.format(configManager.getInvalidBlockMessage()));
                return;
            }
            event.setCancelled(true);
            handleCreate(event, lockPlayer, location, material);
        }



    }

    public void handleCreate(PlayerInteractEvent event, LockPlayer lockPlayer, Location location, Material material){
        List<Player> bezig = lockDataManager.getBezig();
        bezig.remove(event.getPlayer());
        lockDataManager.setBezig(bezig);
        playerDataManager.setLockAmount(lockPlayer.getPlayer(), playerDataManager.getLockAmount(lockPlayer.getPlayer()) + 1);
        lockDataManager.setupLock(material, location, lockPlayer.getUUID(), true);
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
        list.add(Material.HOPPER_MINECART);
        list.add(Material.HOPPER);
        list.add(Material.TRAPPED_CHEST);
        list.add(Material.HOPPER_MINECART);
        list.add(Material.BLAST_FURNACE);
        list.add(Material.BARREL);
        list.add((Material.SHULKER_BOX));
        return list;
    }

}
