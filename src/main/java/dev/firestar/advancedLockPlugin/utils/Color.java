package dev.firestar.advancedLockPlugin.utils;

import org.bukkit.ChatColor;

public class Color {

    public static String format(String input){
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}
