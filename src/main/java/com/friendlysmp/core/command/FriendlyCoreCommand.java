package com.friendlysmp.core.command;

import com.friendlysmp.core.FriendlyCorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public final class FriendlyCoreCommand implements CommandExecutor {

    private final FriendlyCorePlugin plugin;

    public FriendlyCoreCommand(FriendlyCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("friendlycore.admin")) {
            sender.sendMessage("§cNo permission.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadFriendlyCore();
            sender.sendMessage("§aFriendlyCore reloaded.");
            return true;
        }

        sender.sendMessage("§eUsage: /friendlycore reload");
        return true;
    }
}