package com.includivics.events;

import com.includivics.BeautiBackpackPlugin;
import com.includivics.holders.BackpackHolder;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.includivics.events.BackpackPages.currentPage;

public final class BackpackClose implements Listener {

    private final BeautiBackpackPlugin main;

    public BackpackClose(BeautiBackpackPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        ItemStack holdingItemStack = player.getInventory().getItemInMainHand();
        if (!(inventory.getHolder() instanceof BackpackHolder)) {
            return;
        }
        int page = currentPage.getOrDefault(main.backpackUtil.getUUID(holdingItemStack), 1);
        World world = player.getWorld();
        world.playSound(player.getLocation(), Sound.BLOCK_SNOW_PLACE, SoundCategory.MASTER, 0.8f, 0.4f);
        main.backpackUtil.saveBackpack(holdingItemStack, inventory, page, player);

    }
}
