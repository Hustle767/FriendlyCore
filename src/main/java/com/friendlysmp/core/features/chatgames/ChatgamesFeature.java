package com.friendlysmp.core.features.chatgames;

import com.friendlysmp.core.FriendlyCorePlugin;
import com.friendlysmp.core.feature.Feature;
import com.friendlysmp.core.schedulers.Schedulers;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.Configuration;

public class ChatgamesFeature implements Feature {
    private final FriendlyCorePlugin plugin;
    private final Schedulers schedulers;
    public ChatgamesFeature(FriendlyCorePlugin plugin, Schedulers schedulers) {
        this.plugin = plugin;
        this.schedulers = schedulers;
    }

    @Override
    public String id() {
        return "chatgames";
    }

    @Override
    public void enable() {
        var command = plugin.getCommand("pcg");
        if (command != null) {
            ChatgamesCommand c = new ChatgamesCommand(this);
            command.setExecutor(c);
            command.setTabCompleter(c);
        }
    }

    @Override
    public void disable() {

    }

    @Override
    public void reload() {

    }

    public Economy getEconomy() {
        return plugin.getEconomy();
    }

    public Configuration getConfig() {
        return plugin.getConfig();
    }

    public FriendlyCorePlugin getPlugin() {
        return plugin;
    }

    public Schedulers getSchedulers() {
        return schedulers;
    }


}
