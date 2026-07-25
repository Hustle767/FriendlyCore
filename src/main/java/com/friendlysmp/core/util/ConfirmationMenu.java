package com.friendlysmp.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationMenu implements InventoryHolder {
    static final int CONFIRM_SLOT = 11;
    static final int CANCEL_SLOT = 15;
    static final int PREVIEW_SLOT = 13;

    private final Runnable onConfirm;
    private final Runnable onCancel;
    private Inventory inv;
    private boolean resolved = false;
    public final int price;
    private final List<Component> preview;


    public ConfirmationMenu(int price, List<Component> preview, Runnable onConfirm, Runnable onCancel) {
        this.onConfirm = onConfirm;
        this.onCancel = onCancel;
        this.price = price;
        this.preview = preview;
        buildInv();
    }

    private void buildInv() {
        inv = Bukkit.createInventory(this, 27, Component.text("Confirmation Menu", NamedTextColor.GRAY));
        ItemStack confirmItem = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta confirmItemMeta = confirmItem.getItemMeta();
        confirmItemMeta.displayName(Component.text("Confirm", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        if (price > 0) {
            confirmItemMeta.lore(List.of(Component.text("Cost: ", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
                    .append(Component.text(price + " diamonds", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false))));
        }
        confirmItem.setItemMeta(confirmItemMeta);

        ItemStack cancelItem = new ItemStack(Material.RED_CONCRETE);
        ItemMeta cancelItemMeta = cancelItem.getItemMeta();
        cancelItemMeta.displayName(Component.text("Cancel", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        cancelItem.setItemMeta(cancelItemMeta);

        if (preview != null) {
            ItemStack previewItem = new ItemStack(Material.BLACK_CONCRETE);
            ItemMeta previewItemMeta = previewItem.getItemMeta();
            previewItemMeta.displayName(Component.text("Preview", NamedTextColor.WHITE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            previewItemMeta.lore(preview);
            previewItem.setItemMeta(previewItemMeta);
            inv.setItem(PREVIEW_SLOT, previewItem);
        }

        inv.setItem(CONFIRM_SLOT, confirmItem);
        inv.setItem(CANCEL_SLOT, cancelItem);
    }

    public void openMenu(Player player) {
        player.openInventory(inv);
    }

    void confirm() {
        if (resolved) return;
        resolved = true;
        if (onConfirm != null) onConfirm.run();
    }

    void cancel() {
        if (resolved) return;
        resolved = true;
        if (onCancel != null) onCancel.run();
    }

    @Override
    public @NonNull Inventory getInventory() {
        return this.inv;
    }

}
