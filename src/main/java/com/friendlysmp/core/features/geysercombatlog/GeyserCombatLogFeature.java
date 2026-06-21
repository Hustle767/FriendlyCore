package com.friendlysmp.core.features.geysercombatlog;

import com.friendlysmp.core.FriendlyCorePlugin;
import com.friendlysmp.core.feature.Feature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GeyserCombatLogFeature implements Feature, Listener {
    private final FriendlyCorePlugin plugin;

    public GeyserCombatLogFeature(FriendlyCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String id() {
        return "geyser-combat-log";
    }

    @Override
    public void enable() {
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void disable() {

    }

    @Override
    public void reload() {

    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isDead()) event.getPlayer().spigot().respawn();
    }

}
