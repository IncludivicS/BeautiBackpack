package com.includivics.events;

import com.includivics.BeautiBackpackPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GiveRecipes implements Listener {

    private final BeautiBackpackPlugin main;

    public GiveRecipes(BeautiBackpackPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.discoverRecipes(main.backpackConfig.getRecipes());
    }


}
