package com.friendlysmp.core.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.io.File;

public final class SqlManager {
    private final HikariDataSource ds;

    public SqlManager(JavaPlugin plugin) {
        File folder = plugin.getDataFolder();
        if (!folder.exists()) folder.mkdirs();

        String fileName = plugin.getConfig().getString("storage.sqlite.file", "core.db");
        File dbFile = new File(folder, fileName);

        HikariConfig cfg = new HikariConfig();
        cfg.setPoolName("FriendlyCore-SQLite");
        cfg.setMaximumPoolSize(4);
        cfg.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());

        // helps SQLite behave better under light concurrency
        cfg.addDataSourceProperty("busy_timeout", "5000");

        this.ds = new HikariDataSource(cfg);
    }

    public DataSource dataSource() {
        return ds;
    }

    public void shutdown() {
        ds.close();
    }
}