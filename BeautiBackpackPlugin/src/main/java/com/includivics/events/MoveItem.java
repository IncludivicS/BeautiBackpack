package com.includivics.events;

import com.includivics.BeautiBackpackPlugin;
import com.includivics.holders.BackpackHolder;
import com.includivics.utilities.BackpackUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class MoveItem implements Listener {

    private final BeautiBackpackPlugin main;

    public MoveItem(BeautiBackpackPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        List<Material> blacklist = new ArrayList<>();
        ItemStack clickedItemStack = event.getCurrentItem();
        ClickType clickType = event.getClick();
        InventoryType inventoryType = event.getInventory().getType();
        BackpackUtil backpackUtil = main.backpackUtil;

        if (!(event.getWhoClicked() instanceof Player) || clickedItemStack == null) {
            return;
        }

        if (event.getInventory().getHolder() instanceof BackpackHolder) {
            if (clickType == ClickType.NUMBER_KEY || clickType == ClickType.UNKNOWN) event.setCancelled(true);
            main.mainConfig.getBlacklistStaticItems().forEach(s -> blacklist.add(Material.valueOf(s.toUpperCase())));
            List<Pattern> regex = new ArrayList<>();
            main.mainConfig.getBlacklistRegexItems().forEach(s -> regex.add(Pattern.compile(s.toUpperCase())));

            Arrays.stream(Material.values()).forEachOrdered(material -> {
                if (blacklist.contains(material)) return;
                regex.forEach(pattern -> {
                    if (pattern.matcher(material.name()).matches()) {
                        blacklist.add(material);
                    }
                });
            });

            for (Material blacklistMaterial : blacklist) {
                if (clickedItemStack.getType().equals(blacklistMaterial)) {
                    event.setCancelled(true);
                }
            }

        }
        if (backpackUtil.isBackpack(clickedItemStack) && (
                event.getInventory().getHolder() instanceof BackpackHolder ||
                        (inventoryType == InventoryType.SHULKER_BOX && !main.mainConfig.canStoreInShulker()) ||
                        (inventoryType == InventoryType.ENDER_CHEST && !main.mainConfig.canStoreInEnderChest()))) {
            event.setCancelled(true);
        }

    }

}
