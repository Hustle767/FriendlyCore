package com.friendlysmp.core;

import com.friendlysmp.core.command.FriendlyCoreCommand;
import com.friendlysmp.core.feature.FeatureManager;
import com.friendlysmp.core.features.withersound.WitherSoundFeature;
import com.friendlysmp.core.platform.Schedulers;
import com.friendlysmp.core.storage.PlayerSettingsStore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FriendlyCorePlugin extends JavaPlugin {
    private Schedulers schedulers;
    private PlayerSettingsStore playerSettings;
    private FeatureManager featureManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.schedulers = new Schedulers(this);

        this.playerSettings = new PlayerSettingsStore(getDataFolder(), schedulers);
        this.playerSettings.load();

        // Require PacketEvents
        if (Bukkit.getPluginManager().getPlugin("PacketEvents") == null) {
            getLogger().severe("PacketEvents is required for FriendlyCore. Disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.featureManager = new FeatureManager(this);
        featureManager.register(new WitherSoundFeature(this, playerSettings));

        // Register /friendlycore
        var cmd = getCommand("friendlycore");
        if (cmd != null) cmd.setExecutor(new FriendlyCoreCommand(this));

        // Enable features based on config
        featureManager.enableConfigured();

        getLogger().info("FriendlyCore enabled.");
    }

    @Override
    public void onDisable() {
        if (featureManager != null) featureManager.disableAll();
    }

    public void reloadFriendlyCore() {
        reloadConfig();
        if (featureManager != null) featureManager.reloadConfigured();
    }
}