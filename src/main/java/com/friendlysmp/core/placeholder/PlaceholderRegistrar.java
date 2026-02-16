package com.friendlysmp.core.placeholder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlaceholderRegistrar {

    private PlaceholderRegistrar() {}

    public static boolean isPapiPresent() {
        Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        return papi != null && papi.isEnabled();
    }

    public static FriendlyCoreExpansion register(JavaPlugin plugin) {
        if (!isPapiPresent()) {
            plugin.getLogger().info("PlaceholderAPI not found. Skipping placeholders.");
            return null;
        }

        FriendlyCoreExpansion expansion = new FriendlyCoreExpansion(plugin);
        expansion.register();
        plugin.getLogger().info("Registered PlaceholderAPI expansion: " + expansion.getIdentifier());
        return expansion;
    }
}