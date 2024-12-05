package dev.firestar.advancedLockPlugin.utils;

import dev.firestar.UpdateUtil;
import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import org.bukkit.Bukkit;

import java.io.File;

import static org.bukkit.Bukkit.getLogger;

public class Updater {

    private final String VERSION;
    private final AdvancedLockPlugin advancedLockPlugin;

    public Updater(AdvancedLockPlugin advancedLockPlugin) {
        this.advancedLockPlugin = advancedLockPlugin;
        this.VERSION = advancedLockPlugin.getDescription().getVersion();
    }
    public File getPluginFile(){
        File pluginFile = new File(advancedLockPlugin.getDataFolder().getParentFile(), "advancedLockPlugin-" + VERSION + ".jar");
        return pluginFile;
    }
    public boolean checkAndUpdate(){

        return UpdateUtil.checkForUpdates(VERSION, "FireStar1111", "AdvancedLockPlugin", getPluginFile());

    }

    public void restartServer() {
        getLogger().info("Server wordt herstart om de update toe te passen...");
        Bukkit.getServer().shutdown(); // Stopt de server
    }

}
