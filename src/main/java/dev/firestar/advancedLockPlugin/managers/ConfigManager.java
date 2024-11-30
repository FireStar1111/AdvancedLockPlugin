package dev.firestar.advancedLockPlugin.managers;

import dev.firestar.advancedLockPlugin.AdvancedLockPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ConfigManager {

    private final AdvancedLockPlugin plugin;

    private Map<String, Object> config;
    private Map<String, Object> settings;
    private Map<String, Object> messages;

    private int PlayerMaxLockAmount;
    private int PlayerMaxUseAmount;

    private String LimitLockMessage;
    private String SuccesMessageCreate;
    private String SelectBlockMessage;
    private String HelpMessage;
    private String ForceEnabledSuccesMessage;
    private String ForceDisabledSuccesMessage;
    private String BlockIsLockedMessage;
    private boolean ActionBarForLockedMessage;
    private String ActionBarMessage;
    private String BlockBreakMessageAdmin;
    private String BlockBreakMessageUser;
    private String BlockBreakMessagePlayer;

    public ConfigManager(AdvancedLockPlugin plugin) {
        this.plugin = plugin;
    }
    public void loadConfigFile(){
        Yaml yaml = new Yaml();
        try (InputStream in = getClass().getResourceAsStream("/config.yaml")) {
            config = yaml.load(in);
            settings = (Map<String, Object>) config.get("settings");
            messages = (Map<String, Object>) config.get("messages");
            this.PlayerMaxLockAmount = (int) settings.get("PlayerMaxLockAmount");
            this.PlayerMaxUseAmount = (int) settings.get("PlayerMaxUseAmount");
            this.SuccesMessageCreate = (String) messages.get("SuccesMessageCreate");
            this.LimitLockMessage = (String) messages.get("LimitLockMessage");
            this.SelectBlockMessage = (String) messages.get("SelectBlockMessage");
            this.HelpMessage = (String) messages.get("HelpMessage");
            this.ForceEnabledSuccesMessage = (String) messages.get("ForceEnabledSuccesMessage");
            this.ForceDisabledSuccesMessage = (String) messages.get("ForceDisabledSuccesMessage");
            this.BlockIsLockedMessage = (String) messages.get("BlockIsLockedMessage");
            this.ActionBarForLockedMessage = (Boolean) settings.get("ActionBarForLockedMessage");
            this.ActionBarMessage = (String) settings.get("ActionBarMessage");
            this.BlockBreakMessageAdmin = (String) messages.get("BlockBreakMessageAdmin");
            this.BlockBreakMessageUser = (String) messages.get("BlockBreakMessageUser");
            this.BlockBreakMessagePlayer = (String) messages.get("BlockBreakMessagePlayer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPlayerMaxLockAmount() {
        return PlayerMaxLockAmount;
    }

    public int getPlayerMaxUseAmount() {
        return PlayerMaxUseAmount;
    }

    public String getSuccesMessageCreate() {
        return SuccesMessageCreate;
    }

    public String getLimitLockMessage() {
        return LimitLockMessage;
    }

    public String getSelectBlockMessage() {
        return SelectBlockMessage;
    }

    public String getHelpMessage() {
        return HelpMessage;
    }

    public String getForceDisabledSuccesMessage() {
        return ForceDisabledSuccesMessage;
    }

    public String getForceEnabledSuccesMessage() {
        return ForceEnabledSuccesMessage;
    }

    public String getBlockIsLockedMessage() {
        return BlockIsLockedMessage;
    }

    public boolean isActionBarForLockedMessage() {
        return ActionBarForLockedMessage;
    }

    public String getActionBarMessage() {
        return ActionBarMessage;
    }

    public String getBlockBreakMessagePlayer() {
        return BlockBreakMessagePlayer;
    }

    public String getBlockBreakMessageAdmin() {
        return BlockBreakMessageAdmin;
    }

    public String getBlockBreakMessageUser() {
        return BlockBreakMessageUser;
    }
}
