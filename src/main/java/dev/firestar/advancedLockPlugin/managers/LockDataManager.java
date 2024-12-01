package dev.firestar.advancedLockPlugin.managers;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.utils.LocationtoString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.UUID;

public class LockDataManager {

    private final AdvancedLockPlugin advancedLockPlugin;
    private final LocationtoString locationtoString;
    private final ClassManager classManager;
    private File dataFile;
    private YamlConfiguration dataConfig;
    private List<Player> bezig;
    private List<Player> force;

    public LockDataManager(AdvancedLockPlugin advancedLockPlugin) {
        this.advancedLockPlugin = advancedLockPlugin;
        this.classManager = advancedLockPlugin.getClassManager();
        this.locationtoString = classManager.getLocationtoString();
        bezig = new ArrayList<>();
        force = new ArrayList<>();
    }
    public void loadDataFile(){
        advancedLockPlugin.getDataFolder().mkdir();
        dataFile = new File(advancedLockPlugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()){
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }


    public void setupLock(Material block, Location location, UUID owner, boolean islocked){
        String locationKey = locationtoString.convert(location);
        dataConfig.set(locationKey + ".worldName", location.getWorld().getName());
        dataConfig.set(locationKey + ".block", block.toString());
        dataConfig.set(locationKey + ".owner", owner.toString());
        dataConfig.set(locationKey + ".x", location.getX());
        dataConfig.set(locationKey + ".y", location.getY());
        dataConfig.set(locationKey + ".z", location.getZ());
        dataConfig.set(locationKey + ".isLocked", islocked);
        List<Player> admins = new ArrayList<>();
        admins.add(Bukkit.getPlayer(owner));
        int i = 1;
        for (Player player : admins){
            dataConfig.set(locationKey + ".admin" + i, player.getUniqueId().toString());
            i++;
        }
        dataConfig.set(locationKey + ".user1", Bukkit.getPlayer(owner).getUniqueId().toString());
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteLock(Location location){
        String locationKey = locationtoString.convert(location);
        dataConfig.set(locationKey, null);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public List<Player> getAdmins(Location location){
        List<Player> admins = new ArrayList<>();
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            String key = locationKey + ".admin" + i;
            if (dataConfig.contains(key)){
                admins.add(Bukkit.getPlayer(UUID.fromString(dataConfig.getString(key))));
            } else {
                break;
            }
            i++;
        }
        return admins;
    }
    public List<Player> getUsers(Location location){
        List<Player> users = new ArrayList<>();
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            String key = locationKey + ".user" + i;
            if (dataConfig.contains(key)){
                users.add(Bukkit.getPlayer((UUID) dataConfig.get(key)));
            } else {
                break;
            }

        }
        return users;
    }

    public void addUser(Location location, Player user){
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            if (dataConfig.contains(locationKey + ".user" + i)){
                i++;
            } else {
                dataConfig.set(locationKey + ".user" + i, user.getUniqueId().toString());
                break;
            }
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAdmin(Location location, Player admin){
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            if (dataConfig.contains(locationKey + ".admin" + i)){
                i++;
            } else {
                dataConfig.set(locationKey + ".admin" + i, admin.getUniqueId().toString());
                break;
            }
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeUser(Location location, Player user){
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            if (!dataConfig.get(locationKey + ".user" + i).equals(user.getUniqueId())){
                i++;
            } else {
                dataConfig.set(locationKey + ".user" + i, null);
                break;
            }
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeAdmin(Location location, Player admin){
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            if (!dataConfig.get(locationKey + ".admin" + i).equals(admin.getUniqueId())){
                i++;
            } else {
                dataConfig.set(locationKey + ".admin" + i, null);
                break;
            }
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean locationExists(Location location){
        String locationKey = locationtoString.convert(location);
        return dataConfig.contains(locationKey);
    }
    public boolean isLocked(Location location){
        String locationKey = locationtoString.convert(location);
        return dataConfig.getBoolean(locationKey + ".isLocked");
    }
    public Material getBlock(Location location){
        String locationKey = locationtoString.convert(location);
        return Material.valueOf(dataConfig.getString(locationKey + ".block"));
    }
    public String getWorldName(Location location){
        String locationKey = locationtoString.convert(location);
        return dataConfig.getString(locationKey + ".worldName");
    }

    public List<Player> getBezig() {
        return bezig;
    }

    public void setBezig(List<Player> bezig) {
        this.bezig = bezig;
    }

    public List<Player> getForce() {
        return force;
    }

    public void setForce(List<Player> force) {
        this.force = force;
    }
    public UUID getOwnerUUID(Location location){
        String locationKey = locationtoString.convert(location);
        return UUID.fromString(dataConfig.getString(locationKey + ".owner"));
    }
    public void setLocked(Location location, boolean input){

        String locationKey = locationtoString.convert(location);
        dataConfig.set(locationKey + ".isLocked", input);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
