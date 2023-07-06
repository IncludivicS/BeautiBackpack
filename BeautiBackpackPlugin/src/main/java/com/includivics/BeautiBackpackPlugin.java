package com.includivics;

import com.includivics.commands.BackpackTabCompleter;
import com.includivics.commands.Commands;
import com.includivics.configuration.BackpackConfig;
import com.includivics.configuration.MainConfig;
import com.includivics.events.*;
import com.includivics.nms.version.*;
import com.includivics.utilities.BackpackUtil;
import com.includivics.utilities.ColorUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class BeautiBackpackPlugin extends JavaPlugin {

    public static String pluginPrefix = ColorUtil.translateColorCodes("&7[&#1488CCB&#1681CAe&#187BC8a&#1974C6u&#1B6EC4t&#1D67C2i&#1F60C0B&#205ABEa&#2253BCc&#244CBAk&#2646B8p&#273FB6a&#2939B4c&#2B32B2k&7] &r");
    public JavaPlugin plugin;
    public nmsAbtraction nms;
    public MainConfig mainConfig;
    public BackpackUtil backpackUtil;
    public BackpackConfig backpackConfig;

    @Override
    public void onEnable() {
        plugin = this;
        nmsIdentify();
        registerCommand();
        registerClasses();
        registerEvents();
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(pluginPrefix + ChatColor.YELLOW + "Plugin Disabled");
    }

    private void nmsIdentify() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch (version) {
            case "v1_18_R1" -> nms = new v_118R1();
            case "v1_18_R2" -> nms = new v_118R2();
            case "v1_19_R1" -> nms = new v_119R1();
            case "v1_19_R2" -> nms = new v_119R2();
            case "v1_19_R3" -> nms = new v_119R3();
            case "v1_20_R1" -> nms = new v_120R1();
            default -> {
                getServer().getConsoleSender().sendMessage(pluginPrefix + ChatColor.YELLOW + "Unsupported server version (" + version + ")");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        getServer().getConsoleSender().sendMessage(pluginPrefix + ChatColor.YELLOW + "Plugin Enabled!");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new BackpackOpen(this), plugin);
        getServer().getPluginManager().registerEvents(new GiveRecipes(this), plugin);
        getServer().getPluginManager().registerEvents(new BackpackCraft(this), plugin);
        getServer().getPluginManager().registerEvents(new MoveItem(this), plugin);
        getServer().getPluginManager().registerEvents(new BackpackClose(this), plugin);
        getServer().getPluginManager().registerEvents(new BackpackPages(this), plugin);
        getServer().getPluginManager().registerEvents(new PreventUseAsIngredient(this), plugin);
        getServer().getPluginManager().registerEvents(new SortInventory(), plugin);
    }

    private void registerClasses() {
        backpackUtil = new BackpackUtil(nms);
        mainConfig = new MainConfig(this);
        backpackConfig = new BackpackConfig(this);
        mainConfig.load();
        backpackConfig.load();
    }

    private void registerCommand() {
        Objects.requireNonNull(getCommand("beautibackpack")).setExecutor(new Commands(this));
        Objects.requireNonNull(getCommand("beautibackpack")).setTabCompleter(new BackpackTabCompleter(this));

    }

}
