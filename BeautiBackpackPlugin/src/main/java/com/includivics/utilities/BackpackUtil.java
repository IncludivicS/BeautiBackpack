package com.includivics.utilities;

import com.includivics.holders.BackpackHolder;
import com.includivics.nms.version.nmsAbtraction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static com.includivics.events.BackpackPages.currentPage;

public final class BackpackUtil {

    private final nmsAbtraction nms;

    public BackpackUtil(nmsAbtraction nms) {
        this.nms = nms;
    }

    public static ItemStack createNextPageButton() {
        ItemStack nextPageButton = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = nextPageButton.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ColorUtil.translateColorCodes("&eNext Page"));
        itemMeta.setCustomModelData(50);
        nextPageButton.setItemMeta(itemMeta);
        return nextPageButton;
    }

    public static ItemStack createPreviousPageButton() {
        ItemStack previousPageButton = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = previousPageButton.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ColorUtil.translateColorCodes("&ePrevious Page"));
        itemMeta.setCustomModelData(51);
        previousPageButton.setItemMeta(itemMeta);
        return previousPageButton;
    }

    public static ItemStack createBlockedSlot() {
        ItemStack previousPageButton = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = previousPageButton.getItemMeta();
        assert itemMeta != null;
        itemMeta.setCustomModelData(50);
        itemMeta.setDisplayName(ColorUtil.translateColorCodes("&#000000Blocked!"));
        previousPageButton.setItemMeta(itemMeta);
        return previousPageButton;
    }

    public String getUUID(ItemStack itemStack) {
        return nms.getTag(itemStack, "UUID", String.class, null);
    }

    public Boolean isBackpack(ItemStack itemStack) {
        return nms.getTag(itemStack, "UUID", String.class, null) != null;
    }

    public Integer getBackpackSize(ItemStack itemStack) {
        return nms.getTag(itemStack, "size", Integer.class, 0);
    }

    public void saveBackpack(ItemStack itemStack, Inventory inventory, int page, Player closer) {
        closer.getInventory().setItemInMainHand(nms.setTag(itemStack, "storage`page" + page, inventoryToBase64(inventory)));
    }

    public Inventory loadBackpack(ItemStack itemStack, int page, Player opener) {
        String storage = nms.getTag(itemStack, "storage`page" + page, String.class, null);
        if (storage == null) {
            createNewBackpack(itemStack, opener);
        }
        currentPage.put(getUUID(itemStack), page);
        return base64toInventory(nms.getTag(opener.getInventory().getItemInMainHand(), "storage`page" + page, String.class, null), Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName());
    }

    private void createNewBackpack(ItemStack stack, Player player) {
        int slots = getBackpackSize(stack);
        if (slots <= 54) {
            int lastPageSize = slots;
            // If there are not enough slots to fill up the last page, add additional
            // slots to make the page a multiple of 9
            if (slots % 9 != 0) {
                lastPageSize += 9 - (slots % 9);
            }
            int addedSlots = lastPageSize - slots;
            Inventory inv = Bukkit.createInventory(new BackpackHolder(), lastPageSize, Objects.requireNonNull(stack.getItemMeta()).getDisplayName());
            for (int i = 0; i < addedSlots; i++) {
                inv.setItem(lastPageSize - (i + 1), createBlockedSlot());
            }
            if (addedSlots == 0) {
                inv.addItem(new ItemStack(Material.AIR));
            }
            inv.addItem(new ItemStack(Material.AIR));
            saveBackpack(stack, inv, 1, player);
        } else {
            int slotsPerPage = 52;
            int numPages = (int) Math.ceil((double) slots / slotsPerPage); // 3
            //pages
            for (int i = 0; i < numPages - 1; i++) {
                Inventory inv = Bukkit.createInventory(new BackpackHolder(), 54, Objects.requireNonNull(stack.getItemMeta()).getDisplayName());
                inv.setItem(54 - 9, createPreviousPageButton());
                inv.setItem(54 - 1, createNextPageButton());
                if (i == 0) {
                    inv.setItem(54 - 9, createBlockedSlot());
                }

                int page = i + 1;
                saveBackpack(player.getInventory().getItemInMainHand(), inv, page, player);
            }
            // last page
            int lastPageSlotsNeed = slots % slotsPerPage; // 0
            int lastPageSize = calculateToFullsize(lastPageSlotsNeed); // 0
            if (lastPageSize == 0) {
                lastPageSize = 54;
                lastPageSlotsNeed = 52;
            }
            int lastPageBlockedSlots = (lastPageSize - lastPageSlotsNeed) - 1;
            if (lastPageBlockedSlots < 0) {
                lastPageSize += 9;
                lastPageBlockedSlots = (lastPageSize - lastPageSlotsNeed) - 1;
            }
            Inventory inv = Bukkit.createInventory(new BackpackHolder(), lastPageSize, Objects.requireNonNull(stack.getItemMeta()).getDisplayName());
            for (int i = 0; i < lastPageBlockedSlots; i++) {
                inv.setItem((lastPageSize - 1) - i, createBlockedSlot());
            }
            inv.setItem(lastPageSize - 9, createPreviousPageButton());
            saveBackpack(player.getInventory().getItemInMainHand(), inv, numPages, player);
        }
    }

    private int calculateToFullsize(int lastPageSlots) {// 24
        int remain;
        if (lastPageSlots % 9 != 0) {
            remain = (int) Math.ceil((double) lastPageSlots / 9);
            lastPageSlots = remain * 9;
        }
        return lastPageSlots;
    }

    private String inventoryToBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos);
            boos.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++) {
                boos.writeObject(inventory.getItem(i));
            }
            boos.close();

            return Base64Coder.encodeLines(baos.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save inventory.", e);
        }
    }

    private Inventory base64toInventory(String data, String backpackTitle) throws IllegalStateException {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);
            Inventory inventory = Bukkit.createInventory(new BackpackHolder(), bois.readInt(), backpackTitle);
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) bois.readObject());
            }
            bois.close();
            return inventory;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load inventory.", e);
        }
    }

}
