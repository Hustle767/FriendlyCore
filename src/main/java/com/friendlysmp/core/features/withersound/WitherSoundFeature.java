package com.friendlysmp.core.features.withersound;

import com.friendlysmp.core.feature.Feature;
import com.friendlysmp.core.storage.PlayerSettingsStore;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class WitherSoundFeature implements Feature {

    private final JavaPlugin plugin;
    private final PlayerSettingsStore store;

    private PacketListenerAbstract registered; // THIS is what we register/unregister

    public WitherSoundFeature(JavaPlugin plugin, PlayerSettingsStore store) {
        this.plugin = plugin;
        this.store = store;
    }

    @Override public String id() { return "wither-sound"; }

    @Override
    public void enable() {
        PluginCommand cmd = plugin.getCommand("withersound");
        if (cmd != null) {
            WitherSoundCommand exec = new WitherSoundCommand(store);
            cmd.setExecutor(exec);
            cmd.setTabCompleter(exec);
        }

        // Convert interface listener -> abstract listener with priority, then register
        WitherDeathSoundPacketListener listener = new WitherDeathSoundPacketListener(store);
        registered = listener.asAbstract(PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().getEventManager().registerListener(registered);
    }

    @Override
    public void disable() {
        if (registered != null) {
            PacketEvents.getAPI().getEventManager().unregisterListener(registered);
            registered = null;
        }
    }

    @Override
    public void reload() {
        // nothing yet
    }
}