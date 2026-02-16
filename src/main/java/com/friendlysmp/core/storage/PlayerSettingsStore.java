package com.friendlysmp.core.storage;

import com.friendlysmp.core.platform.Schedulers;
import com.friendlysmp.core.storage.SqlManager;
import com.friendlysmp.core.storage.WitherSoundDao;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerSettingsStore {

    private final JavaPlugin plugin;
    private final Schedulers schedulers;

    private final SqlManager sql;
    private final WitherSoundDao witherDao;

    // cache: uuid -> muted
    private final Map<UUID, Boolean> muteWitherDeath = new ConcurrentHashMap<>();

    // debounce save per player
    private final Map<UUID, Boolean> saveQueued = new ConcurrentHashMap<>();

    public PlayerSettingsStore(JavaPlugin plugin, Schedulers schedulers) {
        this.plugin = plugin;
        this.schedulers = schedulers;

        this.sql = new SqlManager(plugin);
        this.witherDao = new WitherSoundDao(sql.dataSource());

        // init table sync (fast + avoids races)
        try {
            witherDao.init();
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to init SQLite tables: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    /** Fast read for hot paths (packet listener). If not loaded yet, returns false (sound ON). */
    public boolean isWitherDeathMuted(UUID uuid) {
        return muteWitherDeath.getOrDefault(uuid, false);
    }

    /** Load this player from DB async into cache */
    public void ensureLoadedAsync(UUID uuid) {
        if (muteWitherDeath.containsKey(uuid)) return;

        schedulers.async(() -> {
            try {
                boolean muted = witherDao.load(uuid);
                // set even if absent (we want the loaded truth)
                muteWitherDeath.put(uuid, muted);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load wither sound setting for " + uuid + ": " + e.getMessage());
            }
        });
    }

    public boolean setWitherDeathMuted(UUID uuid, boolean muted) {
        muteWitherDeath.put(uuid, muted);
        queueSave(uuid);
        return muted;
    }

    public boolean toggleWitherDeathMuted(UUID uuid) {
        boolean now = !isWitherDeathMuted(uuid);
        setWitherDeathMuted(uuid, now);
        return now;
    }

    private void queueSave(UUID uuid) {
        if (saveQueued.putIfAbsent(uuid, true) != null) return;

        // debounce so spam toggles don't spam DB
        schedulers.asyncLater(Duration.ofMillis(500), () -> saveNowAsync(uuid));
    }

    private void saveNowAsync(UUID uuid) {
        try {
            boolean muted = isWitherDeathMuted(uuid);
            witherDao.upsert(uuid, muted);
            // plugin.getLogger().info("[WitherSound] Saved " + uuid + " muted=" + muted); // optional debug
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save wither sound setting for " + uuid + ": " + e.getMessage());
        } finally {
            saveQueued.remove(uuid);
        }
    }

    public void shutdown() {
        sql.shutdown();
    }
}