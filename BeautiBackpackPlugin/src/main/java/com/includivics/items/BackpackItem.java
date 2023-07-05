package com.includivics.items;

import com.includivics.nms.version.nmsAbtraction;
import com.includivics.utilities.ColorUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class BackpackItem {

    private final nmsAbtraction nms;
    private int size;
    private HashMap<String, Material> ingredient = new HashMap<>();
    private String name;
    private List<String> craftingRecipe;
    private String material;
    private ArrayList<String> lore;
    private int customModelData;
    private boolean shapeless;


    public BackpackItem(nmsAbtraction nms) {
        this.nms = nms;
    }

    public ItemStack getBackpackItemStack() {
        ItemStack item = new ItemStack(Material.valueOf(getMaterial().toUpperCase()));
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ColorUtil.translateColorCodes(getName()));
        meta.setLore(operatedLore());
        meta.setCustomModelData(getCustomModelData());
        item.setItemMeta(meta);
        item = nms.setTag(item, "size", getSize());
        return item;
    }

    private ArrayList<String> operatedLore() {
        ArrayList<String> lorelist = new ArrayList<>();
        for (String lore : getLore()) {
            lorelist.add(ColorUtil.translateColorCodes(lore));
        }
        return lorelist;
    }

    public boolean isShapeless() {
        return shapeless;
    }

    public void setShapeless(Boolean bool) {
        this.shapeless = bool;
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public void setLore(ArrayList<String> lore) {
        this.lore = lore;
    }

    public List<String> getCraftingRecipe() {
        return craftingRecipe;
    }

    public void setCraftingRecipe(List<String> craftingRecipe) {
        this.craftingRecipe = craftingRecipe;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public HashMap<String, Material> getIngredient() {
        return ingredient;
    }

    public void setIngredient(HashMap<String, Material> ingredient) {
        this.ingredient = ingredient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(int customodeldata) {
        this.customModelData = customodeldata;
    }

}
