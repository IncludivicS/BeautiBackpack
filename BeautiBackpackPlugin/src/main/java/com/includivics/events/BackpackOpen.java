package com.includivics.events;

import com.includivics.BeautiBackpackPlugin;
import com.includivics.utilities.BackpackUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class BackpackOpen implements Listener {

    private final BackpackUtil backpackUtil;

    public BackpackOpen(BeautiBackpackPlugin main) {
        this.backpackUtil = main.backpackUtil;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        ItemStack holdingItemStack = player.getInventory().getItemInMainHand();
        if (!backpackUtil.isBackpack(holdingItemStack)) return;
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Inventory inventory = backpackUtil.loadBackpack(holdingItemStack, 1, player);
            player.openInventory(inventory);
            event.setCancelled(true);
        }
    }


}
