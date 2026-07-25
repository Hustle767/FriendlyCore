package com.friendlysmp.core.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ConfirmationMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ConfirmationMenu menu)) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;

        int slot = event.getRawSlot();
        if (slot == ConfirmationMenu.CONFIRM_SLOT) {
            menu.confirm();
            player.closeInventory();
        } else if (slot == ConfirmationMenu.CANCEL_SLOT) {
            player.closeInventory();
            menu.cancel();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof ConfirmationMenu menu) {
            menu.cancel();
        }
    }
}