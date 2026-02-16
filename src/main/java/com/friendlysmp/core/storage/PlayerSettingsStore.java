package com.friendlysmp.core.storage;

import com.friendlysmp.core.platform.Schedulers;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerSettingsStore {
    private final File file;
    private final Schedulers schedulers;

    private final Map<UUID, Boolean> muteWitherDeath = new ConcurrentHashMap<>();
    private volatile boolean saveQueued = false;

    public PlayerSettingsStore(File dataFolder, Schedulers schedulers) {
        this.schedulers = schedulers;
        this.file = new File(dataFolder, "player-settings.yml");
    }

    public void load() {
        if (!file.exists()) return;

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        var section = cfg.getConfigurationSection("players");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                boolean muted = section.getBoolean(key + ".mute-wither-death", false);
                muteWitherDeath.put(uuid, muted);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public boolean isWitherDeathMuted(UUID uuid) {
        return muteWitherDeath.getOrDefault(uuid, false);
    }

    public boolean setWitherDeathMuted(UUID uuid, boolean muted) {
        muteWitherDeath.put(uuid, muted);
        queueSave();
        return muted;
    }

    public boolean toggleWitherDeathMuted(UUID uuid) {
        boolean now = !isWitherDeathMuted(uuid);
        setWitherDeathMuted(uuid, now);
        return now;
    }

    private void queueSave() {
        if (saveQueued) return;
        saveQueued = true;

        // debounce so spam toggles don't spam disk
        schedulers.asyncLater(Duration.ofMillis(500), this::saveNowAsync);
    }

    private void saveNowAsync() {
        FileConfiguration cfg = new YamlConfiguration();
        for (var entry : muteWitherDeath.entrySet()) {
            cfg.set("players." + entry.getKey() + ".mute-wither-death", entry.getValue());
        }

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            saveQueued = false;
        }
    }
}