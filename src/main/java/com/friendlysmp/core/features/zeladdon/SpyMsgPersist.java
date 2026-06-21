package com.friendlysmp.core.features.zeladdon;

import com.friendlysmp.core.FriendlyCorePlugin;
import it.pino.zelchat.api.ZelChatAPI;
import it.pino.zelchat.api.player.ChatPlayer;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SpyMsgPersist implements Listener {
    private final ZelAddonFeature feature;
    private final NamespacedKey key;
    private final FriendlyCorePlugin plugin;

    public SpyMsgPersist(ZelAddonFeature feature, FriendlyCorePlugin plugin) {
        this.feature = feature;
        this.key = new NamespacedKey("zelchat", "is_spying");
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().hasPermission("zelchat.command.spy")) return;
        PersistentDataContainer pdc = event.getPlayer().getPersistentDataContainer();
        ChatPlayer chatPlayer = ZelChatAPI.get().getPlayerService().getOnlinePlayers().get(event.getPlayer().getUniqueId());
        if (chatPlayer == null) return;
        pdc.set(key, PersistentDataType.BOOLEAN, chatPlayer.isSpyingMessages());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("zelchat.command.spy")) return;
        plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, st -> {
            PersistentDataContainer pdc = event.getPlayer().getPersistentDataContainer();
            Boolean spyingMessages = pdc.get(key, PersistentDataType.BOOLEAN);
            if (spyingMessages == null) return;
            if (spyingMessages) {
                ChatPlayer chatPlayer =  ZelChatAPI.get().getPlayerService().getOnlinePlayers().get(event.getPlayer().getUniqueId());
                if (chatPlayer == null) {
                    feature.getLogger().severe("ChatPlayer null");
                    return;
                }
                chatPlayer.setSpyingMessages(true);
            }
        }, 20L);
    }



}
