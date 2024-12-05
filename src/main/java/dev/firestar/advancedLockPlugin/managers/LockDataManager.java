package dev.firestar.advancedLockPlugin.managers;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.utils.LocationtoString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.io.IOException;

public class LockDataManager {

    private final AdvancedLockPlugin advancedLockPlugin;
    private final LocationtoString locationtoString;
    private final ClassManager classManager;
    private File dataFile;
    private YamlConfiguration dataConfig;
    private List<Player> bezig;
    private Map<Player, Location> onSignEdit;
    private List<Player> force;
    private List<Player> inspect;

    public LockDataManager(AdvancedLockPlugin advancedLockPlugin) {
        this.advancedLockPlugin = advancedLockPlugin;
        this.classManager = advancedLockPlugin.getClassManager();
        this.locationtoString = classManager.getLocationtoString();
        bezig = new ArrayList<>();
        force = new ArrayList<>();
        onSignEdit = new HashMap<>();
        inspect = new ArrayList<>();
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


    public void setupLock(Material block, Location location, UUID owner, boolean islocked, PlayerDataManager playerDataManager){
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
        List<OfflinePlayer> users = getUsers(location);
        for (OfflinePlayer user : users){
            int useAmount = playerDataManager.getinUseAmount(user.getUniqueId());
            playerDataManager.setInUseAmount(user.getUniqueId(), useAmount + 1);
        }
    }
    public void deleteLock(Location location, PlayerDataManager playerDataManager){
        String locationKey = locationtoString.convert(location);
        Player owner = Bukkit.getPlayer(getOwnerUUID(location));
        int lockAmount = playerDataManager.getLockAmount(owner.getUniqueId());
        playerDataManager.setLockAmount(owner.getUniqueId(), lockAmount - 1);
        List<OfflinePlayer> users = getUsers(location);
        for (OfflinePlayer user : users){
            int useAmount = playerDataManager.getinUseAmount(user.getUniqueId());
            playerDataManager.setInUseAmount(user.getUniqueId(), useAmount - 1);
        }

        dataConfig.set(locationKey, null);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public List<OfflinePlayer> getAdmins(Location location){
        List<OfflinePlayer> admins = new ArrayList<>();
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            String key = locationKey + ".admin" + i;
            if (dataConfig.contains(key)){
                admins.add(Bukkit.getOfflinePlayer(UUID.fromString(dataConfig.getString(key))));
            } else {
                break;
            }
            i++;
        }
        return admins;
    }

    public int getAdminInt(UUID uuid, Location location){
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            String key = locationKey + ".admin" + i;
            if (dataConfig.contains(key)){
                if (UUID.fromString(dataConfig.getString(locationKey + ".admin" + i)).equals(uuid)){
                    return i;

                } else {
                    i++;
                }

            } else {
                break;
            }
        }
        return 0;


    }
    public int getUserInt(Player player, Location location){
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            String key = locationKey + ".user" + i;
            if (dataConfig.contains(key)){
                if (UUID.fromString(dataConfig.getString(locationKey + ".user" + i)).equals(player.getUniqueId())){
                    return i;

                } else {
                    i++;
                }

            } else {
                break;
            }
        }
        return 0;


    }
    public List<OfflinePlayer> getUsers(Location location){
        List<OfflinePlayer> users = new ArrayList<>();
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            String key = locationKey + ".user" + i;
            if (dataConfig.contains(key)){
                users.add(Bukkit.getOfflinePlayer(UUID.fromString(dataConfig.getString(key))));
            } else {
                break;
            }
            i++;
        }
        return users;
    }

    public void addUser(Location location, OfflinePlayer user, PlayerDataManager playerDataManager){
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
        int useAmount = playerDataManager.getinUseAmount(user.getUniqueId());
        playerDataManager.setInUseAmount(user.getUniqueId(), useAmount + 1);
    }

    public void addAdmin(Location location, UUID uuid){
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true){
            if (dataConfig.contains(locationKey + ".admin" + i)){
                i++;
            } else {
                dataConfig.set(locationKey + ".admin" + i, uuid.toString());
                break;
            }
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeUser(Location location, UUID uuid, PlayerDataManager playerDataManager) {
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true) {
            // Haal de waarde op
            Object adminId = dataConfig.get(locationKey + ".user" + i);

            // Controleer of de waarde null is
            if (adminId == null) break;

            // Controleer of de waarde gelijk is aan de UUID van de admin
            if (adminId.equals(uuid.toString())) {
                dataConfig.set(locationKey + ".user" + i, null);
                break;
            } else {
                i++;
            }
        }
        int useAmount = playerDataManager.getinUseAmount(uuid);
        playerDataManager.setInUseAmount(uuid, useAmount + 1);
    }
    public void removeAdmin(Location location, UUID uuid) {
        String locationKey = locationtoString.convert(location);
        int i = 1;
        while (true) {
            // Haal de waarde op
            Object adminId = dataConfig.get(locationKey + ".admin" + i);

            // Controleer of de waarde null is
            if (adminId == null) break;

            // Controleer of de waarde gelijk is aan de UUID van de admin
            if (adminId.equals(uuid.toString())) {
                dataConfig.set(locationKey + ".admin" + i, null);
                break;
            } else {
                i++;
            }
        }

        // Probeer de config op te slaan
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

    public Map<Player, Location> getOnSignEdit() {
        return onSignEdit;
    }

    public void setOnSignEdit(Map<Player, Location> onSignEdit) {
        this.onSignEdit = onSignEdit;
    }
    public List<Location> getAllLockLocations() {
        List<Location> lockLocations = new ArrayList<>();

        // Itereer door alle keys in het dataConfig-bestand
        for (String key : dataConfig.getKeys(false)) {
            // Controleer of de key geldig is (bijvoorbeeld, een wereldnaam bevat)
            if (dataConfig.contains(key + ".worldName")) {
                String worldName = dataConfig.getString(key + ".worldName");
                double x = dataConfig.getDouble(key + ".x");
                double y = dataConfig.getDouble(key + ".y");
                double z = dataConfig.getDouble(key + ".z");

                // Zoek de wereld en maak een locatie-object
                if (Bukkit.getWorld(worldName) != null) {
                    Location location = new Location(Bukkit.getWorld(worldName), x, y, z);
                    lockLocations.add(location);
                }
            }
        }
        return lockLocations;
    }

    public List<Player> getInspect() {
        return inspect;
    }

    public void setInspect(List<Player> inspect) {
        this.inspect = inspect;
    }
}
