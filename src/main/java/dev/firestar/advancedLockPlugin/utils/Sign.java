package dev.firestar.advancedLockPlugin.utils;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Sign {

    public static void openCustomSign(Player player, AdvancedLockPlugin plugin) {
        Location location = player.getLocation().add(0, 1, 0);
        Block block = location.getBlock();
        block.setType(Material.OAK_SIGN);

        // Wacht 1 tick voordat je het bord opent
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (block.getState() instanceof org.bukkit.block.Sign sign) {
                sign.setLine(0, "");
                sign.setLine(1, "");
                sign.setLine(2, "^^^^^^^^^");
                sign.setLine(3, plugin.getClassManager().getConfigManager().getSignText());
                sign.update(true);
                player.openSign(sign);
            }
        }, 1L); // 1 tick delay
    }

}
