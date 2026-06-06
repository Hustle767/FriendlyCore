package com.friendlysmp.core.features.creativeitemcontrol.handlers;

import com.friendlysmp.core.features.creativeitemcontrol.CreativeItemCheck;
import com.friendlysmp.core.features.creativeitemcontrol.CreativeFeature;
import com.friendlysmp.core.features.creativeitemcontrol.ItemCheckContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class CreativePotionHandler implements CreativeItemCheck {
    private final CreativeFeature plugin;

    public CreativePotionHandler(CreativeFeature plugin) {
        this.plugin = plugin;
    }


    @Override
    public void check(ItemCheckContext ctx) {
        if (ctx.isCancelled()) return;
        if (!plugin.potionsEnabled) return;
        boolean nullPlayer = ctx.player == null;
        if (!nullPlayer && ctx.player.hasPermission("friendlycore.cic.bypass.potions")) return;

        if (ctx.meta instanceof BundleMeta bm) {
            for (ItemStack item : bm.getItems()) {
                if (item.getItemMeta() instanceof PotionMeta pm) {
                    if (pm.hasCustomEffects()) {
                        if (!nullPlayer && plugin.playerAlerts) {
                            ctx.player.sendMessage(Component.text("Custom potions are not allowed here!", NamedTextColor.RED, TextDecoration.BOLD));
                        }
                        ctx.cancel();
                    }
                }
            }
        }

        switch (ctx.item.getType()) {
            case POTION, LINGERING_POTION, SPLASH_POTION -> {}
            default -> { return; }

        }

        PotionMeta potionMeta = (PotionMeta) ctx.meta;

        if (potionMeta.hasCustomEffects()) {
            if (!nullPlayer && plugin.playerAlerts) {
                ctx.player.sendMessage(Component.text("Custom potions are not allowed here!", NamedTextColor.RED, TextDecoration.BOLD));
            }
            ctx.cancel();
        }
    }
}

