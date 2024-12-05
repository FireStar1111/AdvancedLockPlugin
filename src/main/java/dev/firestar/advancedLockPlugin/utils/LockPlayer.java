package dev.firestar.advancedLockPlugin.utils;

import org.bukkit.entity.Player;

import java.util.UUID;

public class LockPlayer{

    private final Player player;
    private int lockAmount;
    private int maxLockAmount;

    public LockPlayer(Player player, int lockAmount, int maxLockAmount){
        this.player = player;
        this.lockAmount = lockAmount;
        this.maxLockAmount = maxLockAmount;
    }
    public void getName(){
        player.getName();
    }
    public void sendMessage(String message){
        player.sendMessage(Color.format(message));
    }
    public Player getPlayer(){
        return player;
    }
    public UUID getUUID(){
        return player.getUniqueId();
    }
    public int getLockAmount(){
        return lockAmount;
    }

    public int getMaxLockAmount() {
        return maxLockAmount;
    }
}
