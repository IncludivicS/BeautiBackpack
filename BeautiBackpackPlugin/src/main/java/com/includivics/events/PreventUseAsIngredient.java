package com.includivics.events;

import com.includivics.BeautiBackpackPlugin;
import com.includivics.configuration.MainConfig;
import com.includivics.utilities.BackpackUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public final class PreventUseAsIngredient implements Listener {

    private final BackpackUtil backpackUtil;
    private final MainConfig mainConfig;
    public PreventUseAsIngredient(BeautiBackpackPlugin main){
        this.backpackUtil = main.backpackUtil;
        this.mainConfig = main.mainConfig;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        ItemStack[] inv = e.getInventory().getStorageContents();
        ItemStack result = e.getInventory().getResult();
        if (result == null) return;
        for (ItemStack i : inv) {
            if (backpackUtil.isBackpack(i) && !result.isSimilar(i) && !mainConfig.canUseAsCraftIngredient()) {
                e.getInventory().setResult(null);
            }
        }
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent e) {
        ItemStack[] inv = e.getInventory().getStorageContents();
        for (ItemStack i : inv) {
            if (backpackUtil.isBackpack(i) && !mainConfig.canRepairInAnvil()) {
                e.setResult(null);
            }
        }
    }

    @EventHandler
    public void onSmithingTable(PrepareSmithingEvent e) {
        ItemStack[] inv = e.getInventory().getStorageContents();
        for (ItemStack i : inv) {
            if (backpackUtil.isBackpack(i) && !mainConfig.canUseInSmithingTable()) {
                e.setResult(null);
            }
        }
    }

    @EventHandler
    public void FurnacePrepare(FurnaceStartSmeltEvent e) {
        ItemStack stack = e.getSource();
        if (backpackUtil.isBackpack(stack) && !mainConfig.canBurnInFurnace()) {
            e.setTotalCookTime(Integer.MAX_VALUE);
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent e) {
        ItemStack fuel = e.getFuel();
        if (backpackUtil.isBackpack(fuel) && !mainConfig.canUseAsFuel()) {
            e.setCancelled(true);
        }
    }
}
