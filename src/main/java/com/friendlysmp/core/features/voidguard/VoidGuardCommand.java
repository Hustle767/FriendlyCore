package com.friendlysmp.core.features.voidguard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VoidGuardCommand implements CommandExecutor, TabCompleter {
    private final VoidGuardFeature feature;
    public VoidGuardCommand(VoidGuardFeature feature) {
        this.feature = feature;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("friendlycore.admin")) {
            sender.sendMessage(Component.text("You do not have permission to use this command", NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Component.text("/voidguard set <world> §7- set void-teleport location for a world", NamedTextColor.YELLOW));
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cOnly players can use this command.");
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage("§cUsage: /voidguard set <world>");
                return true;
            }

            String worldName = args[1];
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                sender.sendMessage("§cWorld '" + worldName + "' not found.");
                return true;
            }

            Location loc = player.getLocation();
            feature.saveVoidLocation(world, loc);

            sender.sendMessage("§aVoidGuard: Saved void location for world §e" + worldName +
                    " §aat §f(" +
                    loc.getBlockX() + ", " +
                    loc.getBlockY() + ", " +
                    loc.getBlockZ() + ")§a.");
            return true;
        }

        sender.sendMessage(Component.text("/voidguard set <world> §7- set void-teleport location for a world", NamedTextColor.YELLOW));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        List<String> completions = new ArrayList<>();

        if (!sender.hasPermission("voidguard.admin")) {
            return completions;
        }

        if (args.length == 1) {
            String current = args[0].toLowerCase();
            if ("set".startsWith(current)) {
                completions.add("set");
            }
            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            String current = args[1].toLowerCase();
            for (World world : Bukkit.getWorlds()) {
                String name = world.getName();
                if (name.toLowerCase().startsWith(current)) {
                    completions.add(name);
                }
            }
            return completions;
        }

        return completions;
    }
}
