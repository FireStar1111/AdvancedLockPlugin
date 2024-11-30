package dev.firestar.advancedLockPlugin.utils;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class itemStack {
    public static ItemStack create(String displayname, Material material, int amount, String[] description, boolean hideAtributes, boolean enchanted, boolean hide_enchants,
                            boolean unbreakable, Enchantment enchantment, int enchantmentlvl){

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (hideAtributes){
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        if (hide_enchants){
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (enchanted){
            meta.addEnchant(enchantment, enchantmentlvl, true);
        }
        if (unbreakable){
            meta.setUnbreakable(true);
        }
        if (description != null){
            meta.setLore(Arrays.stream(description).toList());
        }

        meta.setDisplayName(Color.format(displayname));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack getPlayerHead(OfflinePlayer player, String[] lore) {
        // Maak een nieuwe skull item
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        // Pas de metadata aan
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            skullMeta.setLore(Arrays.stream(lore).toList());
            // Stel de speler in
            skull.setItemMeta(skullMeta);
        }

        return skull;
    }

}
