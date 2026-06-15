package com.friendlysmp.core.features.voidguard;

import com.friendlysmp.core.FriendlyCorePlugin;
import com.friendlysmp.core.feature.Feature;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class VoidGuardFeature implements Feature {
    private final FriendlyCorePlugin plugin;
    public Set<String> guardedWorlds = new HashSet<>();

    public VoidGuardFeature(FriendlyCorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String id() {
        return "void-guard";
    }

    @Override
    public void enable() {
        plugin.getServer().getPluginManager().registerEvents(new VoidGuardListener(this), plugin);
        reloadGuardedWorlds();

        if (plugin.getCommand("voidguard") != null) {
            VoidGuardCommand command = new VoidGuardCommand(this);
            plugin.getCommand("voidguard").setExecutor(command);
            plugin.getCommand("voidguard").setTabCompleter(command);
        }


    }

    public void reloadGuardedWorlds() {
        List<String> list = plugin.getConfig().getStringList("void-guard.guarded-worlds");
        guardedWorlds.clear();
        guardedWorlds.addAll(list);
    }

    @Override
    public void disable() {

    }

    @Override
    public void reload() {
        reloadGuardedWorlds();
    }

    public void saveVoidLocation(World world, Location loc) {
        String path = "void-guard.locations." + world.getName();
        Configuration config = plugin.getConfig();

        config.set(path + ".world", world.getName());
        config.set(path + ".x", loc.getX());
        config.set(path + ".y", loc.getY());
        config.set(path + ".z", loc.getZ());
        config.set(path + ".yaw", loc.getYaw());
        config.set(path + ".pitch", loc.getPitch());

        if (!guardedWorlds.contains(world.getName())) {
            guardedWorlds.add(world.getName());
            config.set("void-guard.guarded-worlds", new ArrayList<>(guardedWorlds));
        }

        plugin.saveConfig();

    }

    public Location getVoidLocation(World world) {
        String path = "void-guard.locations." + world.getName();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(path);

        if (section == null) {
            return world.getSpawnLocation();
        }

        double x = section.getDouble( "x", world.getSpawnLocation().getX() );
        double y = section.getDouble( "y", world.getSpawnLocation().getY() );
        double z = section.getDouble( "z", world.getSpawnLocation().getZ() );
        float yaw =  (float) section.getDouble( "yaw", world.getSpawnLocation().getYaw() );
        float pitch  = (float) section.getDouble( "pitch", world.getSpawnLocation().getPitch() );

        return new Location(world, x, y, z, yaw, pitch);
    }

    public boolean isGuardedWorld(World world) { return guardedWorlds.contains(world.getName()); }

    public Logger  getLogger() { return plugin.getLogger(); }

}
