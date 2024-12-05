package dev.firestar.advancedLockPlugin;

import dev.firestar.advancedLockPlugin.managers.ClassManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class AdvancedLockPlugin extends JavaPlugin {

    private ClassManager classManager;


    @Override
    public void onEnable() {
        System.out.println("Made by Firestar!");
        classManager = new ClassManager(this);
        classManager.registerUtils();
        classManager.registerManagers();
        classManager.getLockDataManager().loadDataFile();
        classManager.getPlayerDataManager().loadPlayerDataFile();
        classManager.registerClasses();
        classManager.registerCommands();
        classManager.registerListeners();

    }

    @Override
    public void onDisable() {

    }

    public ClassManager getClassManager() {
        return classManager;
    }
}