package dev.firestar.advancedLockPlugin.managers;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerDataManager {

    private final AdvancedLockPlugin plugin;
    private File playerDataFile;
    private YamlConfiguration playerDataConfig;

    public PlayerDataManager(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
    }
    public void loadPlayerDataFile(){
        playerDataFile = new File(plugin.getDataFolder(), "playerData.yml");
        if (!playerDataFile.exists()){
            try {
                playerDataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

    }
    public void setupPlayer(Player player, int lockAmount, int maxLockAmount, int inUseAmount, int maxInUseAmount){
        String key = player.getName();
        playerDataConfig.set(key + ".uuid", player.getUniqueId().toString());
        playerDataConfig.set(key + ".lockAmount", lockAmount);
        playerDataConfig.set(key + ".maxLockAmount", maxLockAmount);
        playerDataConfig.set(key + ".inUseAmount", inUseAmount);
        playerDataConfig.set(key + ".maxInUseAmount", maxInUseAmount);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean playerExists(Player player){
        return playerDataConfig.contains(player.getName());
    }
    public int getMaxLockAmount(Player player){
        String key = player.getName();
        return playerDataConfig.getInt(key + ".maxLockAmount");
    }
    public int getLockAmount(Player player){
        String key = player.getName();
        return playerDataConfig.getInt(key + ".lockAmount");
    }
    public int getinUseAmount(Player player){
        String key = player.getName();
        return playerDataConfig.getInt(key + ".inUseAmount");
    }
    public int getMaxInUseAmount(Player player){
        String key = player.getName();
        return playerDataConfig.getInt(key + ".maxInUseAmount");
    }
    public void setMaxLockAmount(Player player, int amount){
        String key = player.getName();
        playerDataConfig.set(key + ".maxLockAmount", amount);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setLockAmount(Player player, int amount){
        String key = player.getName();
        playerDataConfig.set(key + ".lockAmount", amount);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setInUseAmount(Player player, int amount){
        String key = player.getName();
        playerDataConfig.set(key + ".inUseAmount", amount);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setMaxInUseAmount(Player player, int amount){
        String key = player.getName();
        playerDataConfig.set(key + ".maxInUseAmount", amount);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
