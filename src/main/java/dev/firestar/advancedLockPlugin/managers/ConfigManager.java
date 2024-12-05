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
    private int DefaultUserLimit;
    private boolean ActionBarForLockedMessage;
    private String ActionBarMessage;
    private Boolean SubmitMenu;
    private Boolean EnableHelpMessage;
    private Boolean CommandNotFound;
    private String CommandNotFoundMessage;

    //messages
    private String LimitLockMessage;
    private String SuccesMessageCreate;
    private String SelectBlockMessage;
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
    private String UserDeleteSuccesMessage;
    private String PromoteMessage;
    private String DemoteMessage;
    private String SignText;
    private String PlayerNotFoundMessage;
    private String PlayerIsAlreadyAnUserMessage;
    private String AddingUserSuccessfulMessage;
    private String ManageYourselfMessage;
    private String CantCombineMessage;
    private String SelectBlockToInspectMessage;
    private String AlreadyInspectingMessage;
    private String PlayerHasMaxUsesMessage;

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
        this.DefaultUserLimit = getConfigInt("settings.DefaultUserLimit", 7);
        this.UserDeleteSuccesMessage = getConfigString("messages.UserDeleteSuccesMessage", "&aSuccessful deleted user!");
        this.PromoteMessage = getConfigString("messages.PromoteMessage", "&aUser promoted!");
        this.DemoteMessage = getConfigString("messages.DemoteMessage", "&cUser demoted!");
        this.SignText = getConfigString("settings.SignText", "Type here");
        this.PlayerNotFoundMessage = getConfigString("messages.PlayerNotFoundMessage", "&cPlayer not found!");
        this.PlayerIsAlreadyAnUserMessage = getConfigString("messages.PlayerIsAlreadyAnUserMessage", "&cPlayer is already an user");
        this.AddingUserSuccessfulMessage = getConfigString("messages.AddingUserSuccessfulMessage", "&aAdded user!");
        this.ManageYourselfMessage = getConfigString("messages.ManageYourselfMessage", "&cYou can't manage yourself!");
        this.CantCombineMessage = getConfigString("messages.CantCombineMessage", "&cYou can't combine this because its locked!");
        this.EnableHelpMessage = getConfigBoolean("settings.EnableHelpMessage", true);
        this.CommandNotFound = getConfigBoolean("settings.CommandNotFound", true);
        this.CommandNotFoundMessage = getConfigString("settings.CommandNotFoundMessage", "&cCommand not found!");
        this.SelectBlockToInspectMessage = getConfigString("messages.SelectBlockToInspectMessage", "&aSelect an block to inspect");
        this.AlreadyInspectingMessage = getConfigString("messages.AlreadyInspectingMessage", "&cYou are already inspecting!");
        this.PlayerHasMaxUsesMessage = getConfigString("messages.PlayerHasMaxUsesMessage", "&cPlayer has reached the limit of usage");
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

    public int getDefaultUserLimit() {
        return DefaultUserLimit;
    }

    public String getUserDeleteSuccesMessage() {
        return UserDeleteSuccesMessage;
    }

    public String getPromoteMessage() {
        return PromoteMessage;
    }

    public String getDemoteMessage() {
        return DemoteMessage;
    }

    public String getSignText() {
        return SignText;
    }

    public String getPlayerNotFoundMessage() {
        return PlayerNotFoundMessage;
    }

    public String getPlayerIsAlreadyAnUserMessage() {
        return PlayerIsAlreadyAnUserMessage;
    }

    public String getAddingUserSuccessfulMessage() {
        return AddingUserSuccessfulMessage;
    }

    public String getManageYourselfMessage() {
        return ManageYourselfMessage;
    }

    public String getCantCombineMessage() {
        return CantCombineMessage;
    }

    public Boolean getEnableHelpMessage() {
        return EnableHelpMessage;
    }

    public Boolean getCommandNotFound() {
        return CommandNotFound;
    }

    public String getCommandNotFoundMessage() {
        return CommandNotFoundMessage;
    }

    public String getSelectBlockToInspectMessage() {
        return SelectBlockToInspectMessage;
    }

    public String getAlreadyInspectingMessage() {
        return AlreadyInspectingMessage;
    }

    public String getPlayerHasMaxUsesMessage() {
        return PlayerHasMaxUsesMessage;
    }
}
