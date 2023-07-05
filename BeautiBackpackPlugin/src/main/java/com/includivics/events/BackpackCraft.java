package com.includivics.events;

import com.includivics.BeautiBackpackPlugin;
import com.includivics.nms.version.nmsAbtraction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class BackpackCraft implements Listener {

    private final nmsAbtraction nms;
    private final BeautiBackpackPlugin main;

    public BackpackCraft(BeautiBackpackPlugin main) {
        this.main = main;
        this.nms = main.nms;
    }

    @EventHandler
    public void craftingInventoryClickEvent(InventoryClickEvent event) {
        if (event.getInventory() instanceof CraftingInventory inventory) {
            if (!main.backpackConfig.isBackpack(inventory.getResult()))
                return;
            if (event.getClick().isShiftClick())
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player clicker)) {
            return;
        }
        ItemStack result = event.getRecipe().getResult();
        if (!main.backpackConfig.isBackpack(result)) return;
        Inventory inventory = event.getInventory();
        ItemStack itemStack = event.getInventory().getResult();
        itemStack = nms.setTag(itemStack, "UUID", UUID.randomUUID().toString());
        itemStack = nms.setTag(itemStack, "owner", clicker.getName());
        inventory.setItem(0, itemStack);
        clicker.updateInventory();
    }
}
