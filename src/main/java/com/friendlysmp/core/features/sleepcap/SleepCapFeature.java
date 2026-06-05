package com.friendlysmp.core.features.sleepcap;

import com.friendlysmp.core.FriendlyCorePlugin;
import com.friendlysmp.core.feature.Feature;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.HandlerList;

public class SleepCapFeature implements Feature {
    private final FriendlyCorePlugin plugin;
    private EnterBedListener enterBedListener;

    public SleepCapFeature(FriendlyCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String id() {
        return "sleep-cap";
    }

    @Override
    public void enable() {
        this.enterBedListener = new EnterBedListener(this);
        plugin.getServer().getPluginManager().registerEvents(enterBedListener, plugin);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(enterBedListener);
    }

    @Override
    public void reload() {

    }

    public Configuration getConfig() {
        return plugin.getConfig();
    }
}
