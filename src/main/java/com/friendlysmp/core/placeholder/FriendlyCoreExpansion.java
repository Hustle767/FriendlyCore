package com.friendlysmp.core.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public final class FriendlyCoreExpansion extends PlaceholderExpansion {

    private final JavaPlugin plugin;
    private final Map<String, BiFunction<Player, String[], String>> handlers = new ConcurrentHashMap<>();

    public FriendlyCoreExpansion(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerHandler(String key, BiFunction<Player, String[], String> handler) {
        handlers.put(key.toLowerCase(), handler);
    }

    @Override public String getIdentifier() { return "friendlycore"; }
    @Override public String getAuthor() { return String.join(", ", plugin.getDescription().getAuthors()); }
    @Override public String getVersion() { return plugin.getDescription().getVersion(); }
    @Override public boolean persist() { return true; }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null || params == null || params.isBlank()) return "";

        String[] parts = params.split("_");
        String key = parts[0].toLowerCase();

        BiFunction<Player, String[], String> handler = handlers.get(key);
        if (handler == null) return "";

        String[] rest = (parts.length <= 1) ? new String[0] : java.util.Arrays.copyOfRange(parts, 1, parts.length);

        try {
            return handler.apply(player, rest);
        } catch (Exception ignored) {
            return "";
        }
    }
}