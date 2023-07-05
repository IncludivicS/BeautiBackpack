package com.includivics.configuration;

import com.includivics.BeautiBackpackPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class MainConfig {
    private final File file;
    private FileConfiguration config;

    public MainConfig(BeautiBackpackPlugin main) {
        JavaPlugin plugin = main.plugin;
        this.file = new File(plugin.getDataFolder(), "config.yml");
        load();
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
        loadDefaults();
    }

    private void save() {
        try {
            config.save(file);
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaults() {
        // Set default values for config properties
        config.addDefault("can-burn-in-furnace", false);
        config.addDefault("can-use-as-fuel", false);
        config.addDefault("can-use-as-craft-ingredient", false);
        config.addDefault("can-use-in-smithing", false);
        config.addDefault("can-repair-in-anvil", false);
        config.addDefault("can-store-in-shulker", false);
        config.addDefault("can-store-in-enderchest", false);
        config.addDefault("blacklist-items.static", Arrays.asList("GOLD_INGOT", "IRON_INGOT"));
        config.addDefault("blacklist-items.regex", List.of("^.*_SHULKER_BOX$"));

        // Save default values to config file if it doesn't exist yet
        config.options().copyDefaults(true);
        save();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    // Getters for config properties
    public boolean canUseInSmithingTable() {
        return config.getBoolean("can-use-in-smithing");
    }

    public boolean canBurnInFurnace() {
        return config.getBoolean("can-burn-in-furnace");
    }

    public boolean canUseAsFuel() {
        return config.getBoolean("can-use-as-fuel");
    }

    public boolean canUseAsCraftIngredient() {
        return config.getBoolean("can-use-as-craft-ingredient");
    }

    public boolean canRepairInAnvil() {
        return config.getBoolean("can-repair-in-anvil");
    }

    public boolean canStoreInShulker() {
        return config.getBoolean("can-store-in-shulker");
    }

    public boolean canStoreInEnderChest() {
        return config.getBoolean("can-store-in-enderchest");
    }

    public List<String> getBlacklistStaticItems() {
        return config.getStringList("blacklist-items.static");
    }

    public List<String> getBlacklistRegexItems() {
        return config.getStringList("blacklist-items.regex");
    }


}
