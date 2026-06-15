package com.friendlysmp.core.features.voidguard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VoidGuardListener implements Listener {
    private final VoidGuardFeature feature;
    public VoidGuardListener(VoidGuardFeature feature) {
        this.feature = feature;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        if (to == null) return;

        Player player = event.getPlayer();
        World world = to.getWorld();
        if (world == null) return;

        if (!feature.isGuardedWorld(world)) return;

        if (to.getY() < world.getMinHeight()) {
            Location target = feature.getVoidLocation(world);

            // Reset fall distance to prevent carried momentum
            player.setFallDistance(0);

            player.teleportAsync(target).exceptionally(ex -> {
                feature.getLogger().info("Failed to teleport " + player.getName() + " from void: " + ex.getMessage());
                return null;
            });
        }
    }
}
