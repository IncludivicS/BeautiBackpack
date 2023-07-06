package com.includivics.events;

import com.includivics.BeautiBackpackPlugin;
import com.includivics.configuration.BackpackConfig;
import com.includivics.configuration.MainConfig;
import com.includivics.holders.BackpackHolder;
import de.jeff_media.chestsort.api.ChestSortEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.includivics.utilities.BackpackUtil.*;

public final class SortInventory implements Listener {
    private final BeautiBackpackPlugin main;
    private final MainConfig mainConfig;

    public SortInventory(BeautiBackpackPlugin main) {
        this.main = main;
        this.mainConfig = main.mainConfig;
    }

    @EventHandler
    public void onChestSortEvent(ChestSortEvent event) {
        if (event.getInventory().getHolder() instanceof BackpackHolder) {
            List<Material> materials = new ArrayList<>();
            mainConfig.getBlacklistStaticItems().forEach(s -> materials.add(Material.valueOf(s.toUpperCase())));
            List<Pattern> regex = new ArrayList<>();
            mainConfig.getBlacklistRegexItems().forEach(s -> regex.add(Pattern.compile(s.toUpperCase())));
            Arrays.stream(Material.values()).forEachOrdered(material -> {
                if (materials.contains(material)) return;
                regex.forEach(pattern -> {
                    if (pattern.matcher(material.name()).matches()) {
                        materials.add(material);
                    }
                });
            });

            ItemStack[] contents;
            if (event.getPlayer() != null){
                contents = event.getPlayer().getInventory().getContents();
            }else {
                contents = event.getInventory().getContents();
            }

            for (ItemStack inventoryItemStack : contents){
                if (inventoryItemStack == null ) continue;
                Material inventoryMaterial = inventoryItemStack.getType();
                if (main.backpackConfig.isBackpack(inventoryItemStack)){
                    event.setUnmovable(inventoryItemStack);
                }
                for (Material material : materials){
                    if (inventoryMaterial.equals(material)){
                        event.setUnmovable(inventoryItemStack);
                    }
                }
            }
            event.setUnmovable(createBlockedSlot());
            event.setUnmovable(createPreviousPageButton());
            event.setUnmovable(createNextPageButton());
        }
    }
}
