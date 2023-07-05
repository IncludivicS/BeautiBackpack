package com.includivics.configuration;

import com.includivics.BeautiBackpackPlugin;
import com.includivics.items.BackpackItem;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class BackpackConfig {
    private final BeautiBackpackPlugin main;
    private final JavaPlugin plugin;
    private final File file;
    private final List<BackpackItem> bagList = new ArrayList<>();
    private final List<NamespacedKey> recipes = new ArrayList<>();
    private FileConfiguration config;

    public BackpackConfig(BeautiBackpackPlugin main) {
        this.main = main;
        this.plugin = main.plugin;
        this.file = new File(plugin.getDataFolder(), "backpacks.yml");
    }

    public void createDefaultConfig() {
        config = YamlConfiguration.loadConfiguration(file);

        String prefix = "backpacks.small.";
        config.addDefault(prefix + "size", 27);
        config.addDefault(prefix + "material", Material.IRON_INGOT.toString());
        config.addDefault(prefix + "name", "&eSmall Backpack");
        config.addDefault(prefix + "lore", new String[]{"&athis is backpack"});
        config.addDefault(prefix + "crafting.ingredient.A", Material.LEATHER.toString());
        config.addDefault(prefix + "crafting.ingredient.B", Material.CHEST.toString());
        config.addDefault(prefix + "crafting.shape", new String[]{"AAA", "ABA", "AAA"});
        config.addDefault(prefix + "customModelData", 1);
        config.addDefault(prefix + "shapeless", false);

        config.options().copyDefaults(true);
        save();
    }

    private BackpackItem getBackpackFromConfig(String key) {
        BackpackItem bag = new BackpackItem(main.nms);
        String configPrefix = "backpacks." + key + ".";
        bag.setName(config.getString(configPrefix + "name"));
        bag.setMaterial(config.getString(configPrefix + "material"));
        bag.setCraftingRecipe(config.getStringList(configPrefix + "crafting.shape"));
        bag.setIngredient(getIngredientList(key));
        bag.setCustomModelData(config.getInt(configPrefix + "customModelData"));
        bag.setSize(config.getInt(configPrefix + "size"));
        bag.setLore((ArrayList<String>) config.getStringList(configPrefix + "lore"));
        bag.setShapeless(config.getBoolean(configPrefix + "crafting.shapeless"));
        return bag;
    }

    public Boolean isBackpack(ItemStack itemStack) {
        for (BackpackItem bag : bagList) {
            if (itemStack == null) {
                return false;
            }

            Material material = itemStack.getType();
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta == null) {
                return false;
            }
            Material bagMaterial = Material.valueOf(bag.getMaterial().toUpperCase());

            if (bagMaterial == material && main.backpackUtil.getBackpackSize(itemStack) != 0) {
                return true;
            }
        }
        return false;
    }


    private void save() {
        try {
            config.save(file);
            config.load(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
        createDefaultConfig();
        loadBackpacks();
    }

    public BackpackItem getBackpack(String name) {
        if (config.isSet("backpacks." + name)) {
            return getBackpackFromConfig(name);
        }

        return null;
    }

    public List<String> getbackpacklist() {
        ConfigurationSection configurationSection = config.getConfigurationSection("backpacks");
        assert configurationSection != null;
        return new ArrayList<>(configurationSection.getKeys(false));
    }

    private void loadBackpacks() {
        bagList.clear();
        recipes.clear();
        ConfigurationSection configurationSection = config.getConfigurationSection("backpacks");
        assert configurationSection != null;
        for (String key : configurationSection.getKeys(false)) {
            bagList.add(getBackpackFromConfig(key));
        }

        registerRecipes();
    }

    private void registerRecipes() {

        for (BackpackItem bag : bagList) {
            boolean shapeless = bag.isShapeless();
            List<String> recipeStrings = bag.getCraftingRecipe();
            HashMap<String, Material> ingredients = bag.getIngredient();
            ItemStack resultItemStack = bag.getBackpackItemStack();
            if (shapeless) {
                addShapelessRecipe(recipeStrings, ingredients, resultItemStack);
            } else {
                addShapedRecipe(recipeStrings, ingredients, resultItemStack);
            }
        }
    }

    public Collection<NamespacedKey> getRecipes() {
        return recipes;
    }

    private void addShapedRecipe(List<String> recipeStrings, HashMap<String, Material> ingredients, ItemStack resultItemStack) {

        NamespacedKey namespacedKey = new NamespacedKey(plugin, ChatColor.stripColor(Objects.requireNonNull(resultItemStack.getItemMeta()).getDisplayName()).replace(' ', '_'));
        ShapedRecipe shapedRecipe = new ShapedRecipe(namespacedKey, resultItemStack);
        shapedRecipe.shape(recipeStrings.toArray(new String[0]));
        for (Map.Entry<String, Material> entry : ingredients.entrySet()) {
            String materialShortcut = entry.getKey();
            Material material = entry.getValue();
            shapedRecipe.setIngredient(materialShortcut.charAt(0), material);
        }
        Bukkit.removeRecipe(namespacedKey);
        Bukkit.addRecipe(shapedRecipe);
        recipes.add(namespacedKey);
    }

    private void addShapelessRecipe(List<String> recipeStrings, HashMap<String, Material> ingredients, ItemStack resultItemStack) {

        NamespacedKey namespacedKey = new NamespacedKey(plugin, ChatColor.stripColor(Objects.requireNonNull(resultItemStack.getItemMeta()).getDisplayName()).replace(' ', '_'));
        ShapelessRecipe shapelessRecipe = new ShapelessRecipe(namespacedKey, resultItemStack);
        for (Map.Entry<String, Material> entry : ingredients.entrySet()) {
            String materialShortcut = entry.getKey();
            Material material = entry.getValue();
            StringBuilder fullString = new StringBuilder();

            for (String string : recipeStrings) {
                fullString.append(string);
            }

            int itemCount = StringUtils.countMatches(fullString.toString(), materialShortcut);
            shapelessRecipe.addIngredient(itemCount, material);
        }
        Bukkit.removeRecipe(namespacedKey);
        Bukkit.addRecipe(shapelessRecipe);
        recipes.add(namespacedKey);
    }


    private HashMap<String, Material> getIngredientList(String key) {
        String configPrefix = "backpacks." + key + ".";
        HashMap<String, Material> ingredientList = new HashMap<>();

        ConfigurationSection configurationSection = config.getConfigurationSection(configPrefix + "crafting.ingredient");
        assert configurationSection != null;
        for (String ingredientShortcut : configurationSection.getKeys(false)) {
            String materialString = config.getString(configPrefix + "crafting.ingredient." + ingredientShortcut);
            ingredientList.put(ingredientShortcut, Material.valueOf(materialString));
        }

        return ingredientList;
    }
}
