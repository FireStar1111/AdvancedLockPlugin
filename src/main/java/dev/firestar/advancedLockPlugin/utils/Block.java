package dev.firestar.advancedLockPlugin.utils;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.InventoryHolder;

public class Block {

    public static Location getLinkedChestLocation(Location location) {
        org.bukkit.block.Block block = location.getBlock();

        // Controleer of het block een chest is
        if (!(block.getState() instanceof Chest chest)) {
            return null; // Geen chest
        }

        // Controleer of de chest een dubbele chest is
        InventoryHolder holder = chest.getInventory().getHolder();
        if (holder instanceof DoubleChest doubleChest) {
            // DoubleChest bevat zowel de linker- als rechterkant
            Chest leftChest = (Chest) doubleChest.getLeftSide();
            Chest rightChest = (Chest) doubleChest.getRightSide();

            // Controleer welke van de twee niet de originele locatie is
            if (!leftChest.getLocation().equals(location)) {
                return leftChest.getLocation();
            }
            if (!rightChest.getLocation().equals(location)) {
                return rightChest.getLocation();
            }
        }

        return null; // Geen gekoppelde chest gevonden
    }
    public static boolean isChestLinked(Location location) {
        org.bukkit.block.Block block = location.getBlock();

        // Controleer of het block een chest is
        if (!(block.getState() instanceof Chest chest)) {
            return false; // Geen chest
        }

        // Controleer of de chest een dubbele chest is
        InventoryHolder holder = chest.getInventory().getHolder();
        if (holder instanceof DoubleChest) {
            return true; // Chest is gekoppeld
        }
        return false; // Geen gekoppelde chest
    }
}
