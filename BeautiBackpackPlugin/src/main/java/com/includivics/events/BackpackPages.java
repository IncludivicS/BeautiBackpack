package com.includivics.events;

import com.includivics.BeautiBackpackPlugin;
import com.includivics.holders.BackpackHolder;
import com.includivics.utilities.BackpackUtil;
import de.jeff_media.chestsort.api.ChestSortAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static com.includivics.utilities.BackpackUtil.*;

public final class BackpackPages implements Listener {

    public static HashMap<String, Integer> currentPage = new HashMap<>();
    private final BackpackUtil backpackUtil;

    public BackpackPages(BeautiBackpackPlugin main) {
        this.backpackUtil = main.backpackUtil;
    }

    @EventHandler
    public void onPageArrowClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        ItemStack holding = player.getInventory().getItemInMainHand();
        String backpackUUID = backpackUtil.getUUID(holding);
        Inventory topInventory = player.getOpenInventory().getTopInventory();
        boolean isChestSortEnable = Bukkit.getPluginManager().getPlugin("ChestSort") != null;
        if (clickedItem == null || !(topInventory.getHolder() instanceof BackpackHolder)) {
            return;
        }
        if (clickedItem.isSimilar(createBlockedSlot())) {
            event.setCancelled(true);
        } else if (clickedItem.isSimilar(createNextPageButton())) {
            int curPage = currentPage.getOrDefault(backpackUUID, 1);
            backpackUtil.saveBackpack(holding, topInventory, curPage, player);
            curPage++;
            Inventory nextPage = backpackUtil.loadBackpack(holding, curPage, player);
            player.openInventory(nextPage);
            if (isChestSortEnable){
                ChestSortAPI.setSortable(nextPage);
            }
            event.setCancelled(true);
        } else if (clickedItem.isSimilar(createPreviousPageButton())) {
            int curPage = currentPage.getOrDefault(backpackUUID, 1);
            backpackUtil.saveBackpack(holding, topInventory, curPage, player);
            curPage--;
            Inventory previousPage = backpackUtil.loadBackpack(holding, curPage, player);
            player.openInventory(previousPage);
            if (isChestSortEnable){
                ChestSortAPI.setSortable(previousPage);
            }
            event.setCancelled(true);
        }
    }

}
