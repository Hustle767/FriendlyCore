package com.friendlysmp.core.features.creativeitemcontrol.handlers;

import com.friendlysmp.core.features.creativeitemcontrol.CreativeFeature;
import com.friendlysmp.core.features.creativeitemcontrol.CreativeItemCheck;
import com.friendlysmp.core.features.creativeitemcontrol.ItemCheckContext;
import io.papermc.paper.datacomponent.DataComponentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;

import java.util.Objects;

public class CreativeComponentHandler implements CreativeItemCheck {
    private final CreativeFeature feature;
    public CreativeComponentHandler(CreativeFeature feature) {
        this.feature = feature;
    }
    @Override
    public void check(ItemCheckContext ctx) {
        if (ctx.isCancelled()) return;
        if (!feature.componentsEnabled) return;
        boolean nullPlayer = ctx.player == null;
        if (!nullPlayer && ctx.player.hasPermission("friendlycore.cic.bypass.components")) return;

        for (DataComponentType type : feature.resolvedComponents) {
            ItemStack defaultItem = feature.getDefaultItem(ctx.item.getType());
            if (ctx.item.hasData(type)) {
                if (type instanceof DataComponentType.Valued<?> valued) {
                    if (Objects.equals(defaultItem.getData(valued), ctx.item.getData(valued))) continue;
                } else {
                    if (defaultItem.hasData(type)) continue;
                }
                ctx.cancel();
            }
        }

        if (!ctx.isCancelled()) {
            if (ctx.meta instanceof BundleMeta bm) {
                for (ItemStack item : bm.getItems()) {
                    for (DataComponentType type : feature.resolvedComponents) {
                        ItemStack defaultItem = feature.getDefaultItem(item.getType());
                        if (item.hasData(type)) {
                            if (type instanceof DataComponentType.Valued<?> valued) {
                                if (Objects.equals(defaultItem.getData(valued), item.getData(valued))) continue;
                            } else {
                                if (defaultItem.hasData(type)) continue;
                            }
                            ctx.cancel();
                        }
                    }
                }
            }
        }

        if (!nullPlayer && ctx.isCancelled() && feature.playerAlerts) {
            ctx.player.sendMessage(Component.text("Items with custom components are not allowed here!", NamedTextColor.RED, TextDecoration.BOLD));
        }
    }
}
