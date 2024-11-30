package dev.firestar.advancedLockPlugin.commands;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.managers.ConfigManager;
import dev.firestar.advancedLockPlugin.managers.LockDataManager;
import dev.firestar.advancedLockPlugin.managers.PlayerDataManager;
import dev.firestar.advancedLockPlugin.utils.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;

public class LockCommand implements CommandExecutor {

    private final AdvancedLockPlugin plugin;
    private final LockDataManager lockDataManager;
    private final PlayerDataManager playerDataManager;
    private final ConfigManager configManager;
    public LockCommand(AdvancedLockPlugin plugin, LockDataManager lockDataManager, PlayerDataManager playerDataManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.lockDataManager = lockDataManager;
        this.playerDataManager = playerDataManager;
        this.configManager = configManager;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length > 0){
            if (args[0].equalsIgnoreCase("help")){
                player.sendMessage(Color.format(configManager.getHelpMessage()));
            } else if (args[0].equalsIgnoreCase("force")){
                List<Player> force = lockDataManager.getForce();
                if (force.contains(player)){
                    force.remove(player);
                    player.sendMessage(Color.format(configManager.getForceDisabledSuccesMessage()));
                } else {
                    force.add(player);
                    player.sendMessage(Color.format(configManager.getForceEnabledSuccesMessage()));
                }
            }
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
}
