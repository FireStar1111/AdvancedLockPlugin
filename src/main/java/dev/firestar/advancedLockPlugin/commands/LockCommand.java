package dev.firestar.advancedLockPlugin.commands;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ClassManager;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
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
    private final String ADMIN_PERMISSION;
    public LockCommand(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
        this.lockDataManager = classManager.getLockDataManager();
        this.playerDataManager = classManager.getPlayerDataManager();
        this.configManager = classManager.getConfigManager();
        this.ADMIN_PERMISSION = "Advancedlock.Admin";
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length > 0){
            if (args[0].equalsIgnoreCase("help")){
                player.sendMessage(Color.format(configManager.getHelpMessage()));
            } else if (args[0].equalsIgnoreCase("force")){
                if (!player.hasPermission(ADMIN_PERMISSION)){
                    return true;
                }
                List<Player> force = lockDataManager.getForce();
                if (force.contains(player)){
                    force.remove(player);
                    player.sendMessage(Color.format(configManager.getForceDisabledSuccesMessage()));
                } else {
                    force.add(player);
                    player.sendMessage(Color.format(configManager.getForceEnabledSuccesMessage()));
                }
            } else if (args[0].equalsIgnoreCase("cancel")) {
                if (!lockDataManager.getBezig().contains(player)){
                    player.sendMessage(Color.format(configManager.getPlayerIsNotSelectingMessage()));
                    return true;
                }
                List<Player> bezig = lockDataManager.getBezig();
                bezig.remove(player);
                lockDataManager.setBezig(bezig);
                player.sendMessage(Color.format(configManager.getCancelselectingSuccesMessage()));
            }
            return true;
        }
        if (lockDataManager.getBezig().contains(player)){
            player.sendMessage(Color.format(configManager.getAlreadySelectingMessage()));
            return true;
        }

        if (playerDataManager.getMaxLockAmount(player) <= playerDataManager.getLockAmount(player)){
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
            if (lockDataManager.getBezig().contains(player)){
                cmd.add("cancel");
            }
            cmd.add("help");
            if (player.hasPermission(ADMIN_PERMISSION)){
                cmd.add("force");
            }
        }

        return cmd;
    }
}
