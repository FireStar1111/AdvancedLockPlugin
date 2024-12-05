package dev.firestar.advancedLockPlugin.commands;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LockCommand implements CommandExecutor, TabCompleter {

    private final AdvancedLockPlugin plugin;
    private final LockDataManager lockDataManager;
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;
    private final ClassManager classManager;
    private final String FORCE_PERMISSION;
    private final String INSPECT_PERMISSION;
    private final String ADMIN_PERMISSION;
    private final String RELOAD_PERMISSION;
    private final String PLAYERINFO_PERMISSION;
    public LockCommand(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.ADMIN_PERMISSION = "Advancedlock.Admin";
        this.lockDataManager = classManager.getLockDataManager();
        this.playerDataManager = classManager.getPlayerDataManager();
        this.configManager = classManager.getConfigManager();
        this.FORCE_PERMISSION = "Advancedlock.Force";
        this.INSPECT_PERMISSION = "Advancedlock.Inspect";
        this.RELOAD_PERMISSION = "Advancedlock.Reload";
        this.PLAYERINFO_PERMISSION = "Advancedlock.PlayerInfo";

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                if (configManager.getEnableHelpMessage()) {
                    player.sendMessage(Color.format("&eWelcome to advanced-lock plugin! Use /lock to begin."));
                    return true;
                }
                if (configManager.getCommandNotFound()) {
                    player.sendMessage(Color.format(configManager.getCommandNotFoundMessage()));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("force")) {
                if (!player.hasPermission(FORCE_PERMISSION) && !player.hasPermission(ADMIN_PERMISSION)) {
                    if (configManager.getCommandNotFound()) {
                        player.sendMessage(Color.format(configManager.getCommandNotFoundMessage()));
                    }
                    return true;
                }
                List<Player> force = lockDataManager.getForce();
                if (force.contains(player)) {
                    force.remove(player);
                    player.sendMessage(Color.format(configManager.getForceDisabledSuccesMessage()));
                } else {
                    force.add(player);
                    player.sendMessage(Color.format(configManager.getForceEnabledSuccesMessage()));
                }
            } else if (args[0].equalsIgnoreCase("cancel")) {
                if (lockDataManager.getBezig().contains(player)) {
                    List<Player> bezig = lockDataManager.getBezig();
                    bezig.remove(player);
                    lockDataManager.setBezig(bezig);
                    player.sendMessage(Color.format(configManager.getCancelselectingSuccesMessage()));

                } else if (lockDataManager.getInspect().contains(player)) {
                    List<Player> inspect = lockDataManager.getInspect();
                    inspect.remove(player);
                    lockDataManager.setInspect(inspect);
                    player.sendMessage(Color.format(configManager.getCancelselectingSuccesMessage()));
                    return true;
                }
                player.sendMessage(Color.format(configManager.getPlayerIsNotSelectingMessage()));
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission(RELOAD_PERMISSION) && !player.hasPermission(ADMIN_PERMISSION)) {
                    if (configManager.getCommandNotFound()) {
                        player.sendMessage(Color.format(configManager.getCommandNotFoundMessage()));
                    }
                    return true;
                }
                lockDataManager.loadDataFile();
                playerDataManager.loadPlayerDataFile();
                configManager.loadConfig();
                player.sendMessage(Color.format("&aPlugin successful reloaded!"));

            } else if (args[0].equalsIgnoreCase("inspect")) {
                if (player.hasPermission(ADMIN_PERMISSION) || player.hasPermission(INSPECT_PERMISSION)) {
                    if (lockDataManager.getInspect().contains(player)) {
                        player.sendMessage(Color.format(configManager.getAlreadyInspectingMessage()));
                        return true;
                    }
                    List<Player> inspect = lockDataManager.getInspect();
                    inspect.add(player);
                    lockDataManager.setInspect(inspect);
                    player.sendMessage(Color.format(configManager.getSelectBlockToInspectMessage()));
                    return true;
                } else {
                    if (configManager.getCommandNotFound()) {
                        player.sendMessage(Color.format(configManager.getCommandNotFoundMessage()));
                    }
                    return true;
                }

            } else if (args[0].equalsIgnoreCase("playerinfo")) {
                if (!player.hasPermission(PLAYERINFO_PERMISSION) && !player.hasPermission(ADMIN_PERMISSION)){
                    if (configManager.getCommandNotFound()) {
                        player.sendMessage(Color.format(configManager.getCommandNotFoundMessage()));
                    }
                    return true;
                }
                if (args.length <= 1){
                    player.sendMessage(Color.format("&cInvalid usage! Use /lock playerinfo <player>"));
                    return true;
                }
                String targetName = args[1];
                OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
                if (target == null || !playerDataManager.playerExists(target.getUniqueId())){
                    player.sendMessage(Color.format(configManager.getPlayerNotFoundMessage()));
                    return true;
                }
                player.sendMessage(Color.format("&a&lPlayer info:"));
                player.sendMessage(Color.format("&e-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+"));
                player.sendMessage(Color.format("Name: &e" + target.getName()));
                player.sendMessage(Color.format("Id: &e" + target.getUniqueId().toString()));
                player.sendMessage(Color.format("LockAmount: &a" + playerDataManager.getLockAmount(target.getUniqueId())));
                player.sendMessage(Color.format("UseAmount: &a" + playerDataManager.getinUseAmount(target.getUniqueId())));
                player.sendMessage(Color.format("MaxLockAmount: &c" + playerDataManager.getMaxLockAmount(target.getUniqueId())));
                player.sendMessage(Color.format("MaxUseAmount: &c" + playerDataManager.getMaxInUseAmount(target.getUniqueId())));
                player.sendMessage(Color.format("&e-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+"));
            } else if (configManager.getCommandNotFound()) {
                player.sendMessage(Color.format(configManager.getCommandNotFoundMessage()));
            }
            return true;
        }
        if (lockDataManager.getInspect().contains(player)){
            player.sendMessage(Color.format(configManager.getAlreadyInspectingMessage()));
            return true;
        }
        if (lockDataManager.getBezig().contains(player)){
            player.sendMessage(Color.format(configManager.getAlreadySelectingMessage()));
            return true;
        }

        if (playerDataManager.getMaxLockAmount(player.getUniqueId()) <= playerDataManager.getLockAmount(player.getUniqueId())){
            player.sendMessage(Color.format(configManager.getLimitLockMessage()));
            return true;
        }
        List<Player> bezig = lockDataManager.getBezig();
        bezig.add(player);
        lockDataManager.setBezig(bezig);
        player.sendMessage(Color.format(configManager.getSelectBlockMessage()));

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> cmd = new ArrayList<>();
        Player player = (Player) sender;
        if (args.length == 1){
            if (lockDataManager.getBezig().contains(player) || lockDataManager.getInspect().contains(player)){
                cmd.add("cancel");
            }
            if (configManager.getEnableHelpMessage()){
                cmd.add("help");
            }
            if (player.hasPermission(RELOAD_PERMISSION)){
                cmd.add("reload");
            }
            if (player.hasPermission(FORCE_PERMISSION)){
                cmd.add("force");
            }
            if(player.hasPermission(INSPECT_PERMISSION)){
                cmd.add("inspect");
            }
            if (player.hasPermission(PLAYERINFO_PERMISSION)){
                cmd.add("playerinfo");
            }
            if (player.hasPermission(ADMIN_PERMISSION)){
                if (!cmd.contains("reload")){
                    cmd.add("reload");
                }
                if (!cmd.contains("force")){
                    cmd.add("force");
                }
                if (!cmd.contains("inspect")){
                    cmd.add("inspect");
                }
                if (!cmd.contains("playerinfo")){
                    cmd.add("playerinfo");
                }
            }
        } if (args.length == 2){
            if (args[0].equalsIgnoreCase("playerinfo")){
                for (OfflinePlayer players : playerDataManager.getAllPlayers()){
                    cmd.add(players.getName());
                }
            }
        }

        return cmd;
    }
}
