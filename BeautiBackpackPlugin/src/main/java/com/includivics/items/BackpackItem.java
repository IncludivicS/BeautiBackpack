package com.includivics.items;

import com.includivics.nms.version.nmsAbtraction;
import com.includivics.utilities.ColorUtil;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class BackpackItem {

    private final nmsAbtraction nms;
    private int size;
    private HashMap<String, Material> ingredient = new HashMap<>();
    private String name;
    private List<String> craftingRecipe;
    private String material;
    private String skullTexture;
    private ArrayList<String> lore;
    private int customModelData;
    private boolean shapeless;


    public BackpackItem(nmsAbtraction nms) {
        this.nms = nms;
    }

    public ItemStack getBackpackItemStack() {
        ItemStack item;
        if (getMaterial().equalsIgnoreCase("PLAYER_HEAD")){
            item = createSkull(getSkullTexture());
        }else {
            item = new ItemStack(Material.valueOf(getMaterial().toUpperCase()));
        }
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
        ArrayList<String> loreList = new ArrayList<>();
        for (String lore : getLore()) {
            loreList.add(ColorUtil.translateColorCodes(lore));
        }
        return loreList;
    }

    public static ItemStack createSkull(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty())
            return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));

        try {
            assert headMeta != null;
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
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
    public String getSkullTexture() {
        return skullTexture;
    }

    public void setSkullTexture(String skullTexture) {
        this.skullTexture = skullTexture;
    }

}
