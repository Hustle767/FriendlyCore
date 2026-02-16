package com.friendlysmp.core.platform;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class Schedulers {
    private final Plugin plugin;

    public Schedulers(Plugin plugin) {
        this.plugin = plugin;
    }

    /** Safe for Paper+Folia: runs on global region thread. */
    public void global(Runnable task) {
        Bukkit.getGlobalRegionScheduler().execute(plugin, task);
    }

    /** Safe for Paper+Folia: async for disk/network I/O only. */
    public void async(Runnable task) {
        Bukkit.getAsyncScheduler().runNow(plugin, scheduledTask -> task.run());
    }

    /** Safe for Paper+Folia: async later (disk I/O only). */
    public void asyncLater(Duration delay, Runnable task) {
        long millis = Math.max(0L, delay.toMillis());
        Bukkit.getAsyncScheduler().runDelayed(plugin, scheduledTask -> task.run(), millis, TimeUnit.MILLISECONDS);
    }
}