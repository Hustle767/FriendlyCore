package com.friendlysmp.core.storage;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public final class WitherSoundDao {
    private final DataSource ds;

    public WitherSoundDao(DataSource ds) {
        this.ds = ds;
    }

    public void init() throws Exception {
        try (Connection c = ds.getConnection(); Statement s = c.createStatement()) {
            s.executeUpdate("""
                CREATE TABLE IF NOT EXISTS friendlycore_wither_sound (
                  uuid TEXT PRIMARY KEY,
                  muted INTEGER NOT NULL DEFAULT 0
                )
            """);
        }
    }

    public boolean load(UUID uuid) throws Exception {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("""
                 SELECT muted FROM friendlycore_wither_sound WHERE uuid=?
             """)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                return rs.getInt(1) == 1;
            }
        }
    }

    public void upsert(UUID uuid, boolean muted) throws Exception {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("""
                 INSERT INTO friendlycore_wither_sound (uuid, muted)
                 VALUES (?, ?)
                 ON CONFLICT(uuid) DO UPDATE SET muted=excluded.muted
             """)) {
            ps.setString(1, uuid.toString());
            ps.setInt(2, muted ? 1 : 0);
            ps.executeUpdate();
        }
    }
}