package dev.firestar.advancedLockPlugin.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import dev.firestar.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class CustomPlayerHead {

    private final ItemStack head;

    public CustomPlayerHead(String displayName, String base64, ItemStack head) {
        this.head = head;

    }

    public ItemStack getHead() {
        return head;
    }
}