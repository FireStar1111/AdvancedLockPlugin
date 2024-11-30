package dev.firestar.advancedLockPlugin.managers;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.Menus.SettingsMenu;
import dev.firestar.advancedLockPlugin.commands.LockCommand;
import dev.firestar.advancedLockPlugin.listeners.BlockBreakEvent;
import dev.firestar.advancedLockPlugin.listeners.onInteract;
import dev.firestar.advancedLockPlugin.listeners.onJoinEvent;
import dev.firestar.advancedLockPlugin.utils.LocationtoString;

public class ClassManager implements Manager {

    private LockDataManager lockDataManager;
    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private LocationtoString locationtoString;
    private SettingsMenu settingsMenu;
    private final AdvancedLockPlugin advancedLockPlugin;

    public ClassManager(AdvancedLockPlugin advancedLockPlugin) {
        this.advancedLockPlugin = advancedLockPlugin;
    }

    @Override
    public void registerManagers() {
        lockDataManager = new LockDataManager(advancedLockPlugin, locationtoString);
        playerDataManager = new PlayerDataManager(advancedLockPlugin);
        configManager = new ConfigManager(advancedLockPlugin);
    }

    @Override
    public void registerUtils() {
        locationtoString = new LocationtoString();
    }

    @Override
    public void registerCommands() {

        advancedLockPlugin.getCommand("lock").setExecutor(new LockCommand(advancedLockPlugin, lockDataManager, playerDataManager, configManager));

    }

    @Override
    public void registerListeners() {
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new onInteract(advancedLockPlugin, lockDataManager, playerDataManager, configManager), advancedLockPlugin);
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new onJoinEvent(advancedLockPlugin, playerDataManager, configManager), advancedLockPlugin);
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new BlockBreakEvent(advancedLockPlugin, lockDataManager, playerDataManager, configManager), advancedLockPlugin);
    }

    @Override
    public void registerClasses() {
        settingsMenu = new SettingsMenu(advancedLockPlugin, lockDataManager);

    }

    public LockDataManager getLockDataManager() {
        return lockDataManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public SettingsMenu getSettingsMenu() {
        return settingsMenu;
    }
}
