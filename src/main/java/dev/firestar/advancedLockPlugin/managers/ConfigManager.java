package dev.firestar.advancedLockPlugin.managers;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;


public class ConfigManager {

    private final AdvancedLockPlugin plugin;
    private FileConfiguration config;

    //settings
    private int PlayerMaxLockAmount;
    private int PlayerMaxUseAmount;
    private boolean ActionBarForLockedMessage;
    private String ActionBarMessage;
    private Boolean SubmitMenu;
    //messages
    private String LimitLockMessage;
    private String SuccesMessageCreate;
    private String SelectBlockMessage;
    private String HelpMessage;
    private String ForceEnabledSuccesMessage;
    private String ForceDisabledSuccesMessage;
    private String BlockIsLockedMessage;
    private String BlockBreakMessageAdmin;
    private String BlockBreakMessageUser;
    private String BlockBreakMessagePlayer;
    private String AlreadySelectingMessage;
    private String PlayerIsNotSelectingMessage;
    private String CancelselectingSuccesMessage;
    private String InvalidBlockMessage;
    private String AlreadyLockedMessage;
    private String SuccessLockDeleteMessage;
    private String CancelLockDeleteMessage;
    private String DisableLockMessage;
    private String EnabledLockMessage;

    public ConfigManager(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
        this.PlayerMaxLockAmount = getConfigInt("settings.PlayerMaxLockAmount", 3);
        this.PlayerMaxUseAmount = getConfigInt("settings.PlayerMaxUseAmount", 10);
        this.ActionBarForLockedMessage = getConfigBoolean("settings.ActionBarForLockedMessage", true);
        this.ActionBarMessage = getConfigString("settings.ActionBarMessage", "&cLocked");

        this.LimitLockMessage = getConfigString("messages.LimitLockMessage", "&cYou cant lock things, because you have reach your limit.");
        this.SuccesMessageCreate = getConfigString("messages.SuccesMessageCreate", "&aSuccessful locked");
        this.SelectBlockMessage = getConfigString("messages.SelectBlockMessage", "&eSelect the block you want to lock. Use /lock help for more information");
        this.HelpMessage = getConfigString("messages.HelpMessage", "&e&lWelcome to advanced-lock plugin! Use /lock to begin.");
        this.ForceEnabledSuccesMessage = getConfigString("messages.ForceEnabledSuccesMessage", "&aSuccessful enabled force-modus!");
        this.ForceDisabledSuccesMessage = getConfigString("messages.ForceDisabledSuccesMessage", "&cSuccessful disabled force-modes!");
        this.BlockIsLockedMessage = getConfigString("messages.BlockIsLockedMessage", "&cYou can't open this, because it's locked!");
        this.BlockBreakMessageAdmin = getConfigString("messages.BlockBreakMessageAdmin", "&cIf you want to delete this lock, you can delete them in the settings-menu!");
        this.BlockBreakMessageUser = getConfigString("messages.BlockBreakMessageUser", "&cYou can't break this!");
        this.BlockBreakMessagePlayer = getConfigString("messages.BlockBreakMessagePlayer", "&cYou can't break this, because it's locked!");
        this.AlreadySelectingMessage = getConfigString("messages.AlreadySelectingMessage", "&cYou are already selecting. If you want to cancel type /lock cancel! Use /lock help for more information");
        this.PlayerIsNotSelectingMessage = getConfigString("messages.PlayerIsNotSelectingMessage", "&cYou are not selecting! Use /lock to select a block to lock.");
        this.CancelselectingSuccesMessage = getConfigString("messages.CancelselectingSuccesMessage", "&cSelecting canceled");
        this.InvalidBlockMessage = getConfigString("messages.InvalidBlockMessage", "&cYou can't select this block!");
        this.AlreadyLockedMessage = getConfigString("messages.AlreadyLockedMessage", "&cThis is already locked!");
        this.SubmitMenu = getConfigBoolean("settings.SubmitMenu", true);
        this.SuccessLockDeleteMessage = getConfigString("messages.SuccessLockDeleteMessage", "&cYou have successful deleted this lock!");
        this.CancelLockDeleteMessage = getConfigString("messages.CancelLockDeleteMessage", "&cYou successful canceled deletion");
        this.DisableLockMessage = getConfigString("messages.DisableLockMessage", "&cSuccessful disabled lock modus!");
        this.EnabledLockMessage = getConfigString("messages.EnabledLockMessage", "&aSuccessful enabled lock modus!");
    }


    public void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public String getConfigString(String path, String Default) {
        return config.getString(path, Default);
    }
    public int getConfigInt(String path, int Default) {
        return config.getInt(path, Default);
    }
    public boolean getConfigBoolean(String path, boolean Default) {
        return config.getBoolean(path, Default);
    }

    public boolean isActionBarForLockedMessage() {
        return ActionBarForLockedMessage;
    }

    public int getPlayerMaxLockAmount() {
        return PlayerMaxLockAmount;
    }

    public int getPlayerMaxUseAmount() {
        return PlayerMaxUseAmount;
    }

    public String getActionBarMessage() {
        return ActionBarMessage;
    }

    public String getBlockBreakMessageAdmin() {
        return BlockBreakMessageAdmin;
    }

    public String getBlockBreakMessagePlayer() {
        return BlockBreakMessagePlayer;
    }

    public String getBlockBreakMessageUser() {
        return BlockBreakMessageUser;
    }

    public String getBlockIsLockedMessage() {
        return BlockIsLockedMessage;
    }

    public String getForceDisabledSuccesMessage() {
        return ForceDisabledSuccesMessage;
    }

    public String getForceEnabledSuccesMessage() {
        return ForceEnabledSuccesMessage;
    }

    public String getHelpMessage() {
        return HelpMessage;
    }

    public String getLimitLockMessage() {
        return LimitLockMessage;
    }

    public String getSelectBlockMessage() {
        return SelectBlockMessage;
    }

    public String getSuccesMessageCreate() {
        return SuccesMessageCreate;
    }

    public String getAlreadySelectingMessage() {
        return AlreadySelectingMessage;
    }

    public String getPlayerIsNotSelectingMessage() {
        return PlayerIsNotSelectingMessage;
    }

    public String getCancelselectingSuccesMessage() {
        return CancelselectingSuccesMessage;
    }

    public String getInvalidBlockMessage() {
        return InvalidBlockMessage;
    }

    public String getAlreadyLockedMessage() {
        return AlreadyLockedMessage;
    }

    public Boolean getSubmitMenu() {
        return SubmitMenu;
    }

    public String getSuccessLockDeleteMessage() {
        return SuccessLockDeleteMessage;
    }

    public String getCancelLockDeleteMessage() {
        return CancelLockDeleteMessage;
    }

    public String getDisableLockMessage() {
        return DisableLockMessage;
    }

    public String getEnabledLockMessage() {
        return EnabledLockMessage;
    }
}
