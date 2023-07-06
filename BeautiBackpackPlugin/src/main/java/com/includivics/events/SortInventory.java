package com.includivics.events;

import com.includivics.holders.BackpackHolder;
import de.jeff_media.chestsort.api.ChestSortEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.includivics.utilities.BackpackUtil.*;

public final class SortInventory implements Listener {

    @EventHandler
    public void onChestSortEvent(ChestSortEvent event) {
        if (event.getInventory().getHolder() instanceof BackpackHolder) {
            event.setUnmovable(createBlockedSlot());
            event.setUnmovable(createPreviousPageButton());
            event.setUnmovable(createNextPageButton());
        }
    }
}
