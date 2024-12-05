package dev.firestar.advancedLockPlugin.managers;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import dev.firestar.advancedLockPlugin.Menus.AdminMenu;
import dev.firestar.advancedLockPlugin.Menus.SettingsMenu;
import dev.firestar.advancedLockPlugin.Menus.SubmitMenu;
import dev.firestar.advancedLockPlugin.commands.LockCommand;
import dev.firestar.advancedLockPlugin.listeners.*;
import dev.firestar.advancedLockPlugin.utils.LocationtoString;
import dev.firestar.advancedLockPlugin.utils.Updater;

public class ClassManager implements Manager {

    private LockDataManager lockDataManager;
    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private LocationtoString locationtoString;
    private SettingsMenu settingsMenu;
    private final AdvancedLockPlugin advancedLockPlugin;
    private SubmitMenu submitMenu;
    private AdminMenu adminMenu;
    private Updater updater;
    public ClassManager(AdvancedLockPlugin advancedLockPlugin) {
        this.advancedLockPlugin = advancedLockPlugin;
    }

    @Override
    public void registerManagers() {
        lockDataManager = new LockDataManager(advancedLockPlugin);
        playerDataManager = new PlayerDataManager(advancedLockPlugin);
        configManager = new ConfigManager(advancedLockPlugin);
    }

    @Override
    public void registerUtils() {
        locationtoString = new LocationtoString();
    }

    @Override
    public void registerCommands() {

        advancedLockPlugin.getCommand("lock").setExecutor(new LockCommand(advancedLockPlugin));

    }

    @Override
    public void registerListeners() {
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new onInteract(advancedLockPlugin), advancedLockPlugin);
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new onJoinEvent(advancedLockPlugin), advancedLockPlugin);
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new BlockBreakEvent(advancedLockPlugin), advancedLockPlugin);
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new onInventoryClick(advancedLockPlugin), advancedLockPlugin);
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new onLeaveListener(advancedLockPlugin), advancedLockPlugin);
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new onSignChange(advancedLockPlugin), advancedLockPlugin);
        advancedLockPlugin.getServer().getPluginManager().registerEvents(new onMoveEvent(advancedLockPlugin), advancedLockPlugin);
    }

    @Override
    public void registerClasses() {
        settingsMenu = new SettingsMenu(advancedLockPlugin);
        submitMenu = new SubmitMenu(advancedLockPlugin);
        adminMenu = new AdminMenu(advancedLockPlugin);
        updater = new Updater(advancedLockPlugin);
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

    public LocationtoString getLocationtoString() {
        return locationtoString;
    }
    public SubmitMenu getSubmitMenu() {
        return submitMenu;
    }
    public AdminMenu getAdminMenu() {
        return adminMenu;
    }

    public Updater getUpdater() {
        return updater;
    }
}
