package dev.firestar.advancedLockPlugin.utils;

import dev.firestar.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public static ItemStack getPlayerHead(OfflinePlayer player, String[] lore, String displayName) {
        // Maak een nieuwe skull item
        ItemStack skull = SkullBuilder.getSkullByPlayer(player);
        ItemMeta meta = skull.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(List.of(lore));
        skull.setItemMeta(meta);
        return skull;
    }

    public static ItemStack addNameSpaceKey(ItemStack itemStack, String nameSpace, String key, String value){
        ItemMeta meta = itemStack.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(nameSpace, key);
        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, value);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static String getNameSpaceKey(ItemStack itemStack, String nameSpace, String key){
        ItemMeta meta = itemStack.getItemMeta();
        NamespacedKey namespacedKey = new NamespacedKey(nameSpace, key);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(namespacedKey, PersistentDataType.STRING)){
            return container.get(namespacedKey, PersistentDataType.STRING);
        } else {
            return null;
        }

    }

    public static ItemStack createSimple(Material material, String displayName, List lore, Boolean enchantment, int count) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if  (enchantment == true){
            meta.addEnchant(Enchantment.SHARPNESS, 1, false);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(displayName);
        if (lore != Collections.emptyList()){
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

}
