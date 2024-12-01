package dev.firestar.advancedLockPlugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationtoString {
    public String convert(Location locatie) {
        return locatie.getWorld().getName() + "_" + locatie.getBlockX() + "_" + locatie.getBlockY() + "_" + locatie.getBlockZ();
    }



    public Location convertToLocation(String convertedLocation){
        String[] args = convertedLocation.split("_");
        World world = Bukkit.getWorld(args[0]);

        Location location = new Location(world, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));

        return location;
    }

}
