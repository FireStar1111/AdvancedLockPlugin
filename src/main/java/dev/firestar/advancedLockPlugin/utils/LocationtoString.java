package dev.firestar.advancedLockPlugin.utils;

import org.bukkit.Location;

public class LocationtoString {
    public String convert(Location locatie) {
        return locatie.getWorld().getName() + "_" + locatie.getBlockX() + "_" + locatie.getBlockY() + "_" + locatie.getBlockZ();
    }
}
