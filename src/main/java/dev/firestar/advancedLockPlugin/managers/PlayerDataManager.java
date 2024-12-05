package dev.firestar.advancedLockPlugin.managers;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public boolean playerExists(UUID uuid){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return playerDataConfig.contains(player.getName());
    }
    public int getMaxLockAmount(UUID uuid){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String key = player.getName();
        return playerDataConfig.getInt(key + ".maxLockAmount");
    }
    public int getLockAmount(UUID uuid){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String key = player.getName();
        return playerDataConfig.getInt(key + ".lockAmount");
    }
    public int getinUseAmount(UUID uuid){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String key = player.getName();
        return playerDataConfig.getInt(key + ".inUseAmount");
    }
    public int getMaxInUseAmount(UUID uuid){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String key = player.getName();
        return playerDataConfig.getInt(key + ".maxInUseAmount");
    }
    public void setMaxLockAmount(UUID uuid, int amount){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String key = player.getName();
        playerDataConfig.set(key + ".maxLockAmount", amount);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setLockAmount(UUID uuid, int amount){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String key = player.getName();
        playerDataConfig.set(key + ".lockAmount", amount);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setInUseAmount(UUID uuid, int amount){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String key = player.getName();
        playerDataConfig.set(key + ".inUseAmount", amount);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setMaxInUseAmount(UUID uuid, int amount){
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        String key = player.getName();
        playerDataConfig.set(key + ".maxInUseAmount", amount);
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<OfflinePlayer> getAllPlayers() {
        List<OfflinePlayer> players = new ArrayList<>();

        // Itereer door alle keys in het playerDataConfig-bestand
        for (String key : playerDataConfig.getKeys(false)) {
            // Controleer of de key geldig is (bijvoorbeeld, of de key een uuid bevat)
            String uuid = playerDataConfig.getString(key + ".uuid");

            if (uuid != null) {
                // Haal de speler op
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

                // Voeg alleen toe als de speler online is
                if (player != null) {
                    players.add(player);
                }
            }
        }
        return players;
    }

}
