package com.includivics.nms.version;

import org.bukkit.inventory.ItemStack;

public interface nmsAbtraction {

    ItemStack setTag(ItemStack itemStack, String key, Object value);

    <T> T getTag(ItemStack itemStack, String key, Class<T> typeClass, Object defaultValue);

}
