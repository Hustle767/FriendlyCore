package com.friendlysmp.core.features.withersound;

import com.friendlysmp.core.storage.PlayerSettingsStore;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntitySoundEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import org.bukkit.entity.Player;

public final class WitherDeathSoundPacketListener implements PacketListener {

    private static final String WITHER_DEATH = "minecraft:entity.wither.death";
    private final PlayerSettingsStore store;

    public WitherDeathSoundPacketListener(PlayerSettingsStore store) {
        this.store = store;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        Object playerObj = event.getPlayer();
        if (!(playerObj instanceof Player player)) return;
        
        store.ensureLoadedAsync(player.getUniqueId()); 

        if (!store.isWitherDeathMuted(player.getUniqueId())) return;

        if (event.getPacketType() == PacketType.Play.Server.SOUND_EFFECT) {
            WrapperPlayServerSoundEffect packet = new WrapperPlayServerSoundEffect(event);
            if (isWitherDeath(packet.getSound())) event.setCancelled(true);
            return;
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_SOUND_EFFECT) {
            WrapperPlayServerEntitySoundEffect packet = new WrapperPlayServerEntitySoundEffect(event);
            if (isWitherDeath(packet.getSound())) event.setCancelled(true);
        }
    }

    private boolean isWitherDeath(Sound sound) {
        if (sound == null) return false;
        return WITHER_DEATH.equalsIgnoreCase(sound.getSoundId().toString());
    }
}