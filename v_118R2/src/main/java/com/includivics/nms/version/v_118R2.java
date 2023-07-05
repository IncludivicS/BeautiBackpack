package com.includivics.nms.version;

import net.minecraft.nbt.CompoundTag;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public final class v_118R2 implements nmsAbtraction {


    @Override
    public ItemStack setTag(ItemStack itemStack, String key, Object value) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag compoundTag = nmsStack.getOrCreateTag();

        if (value instanceof String)
            compoundTag.putString(key, (String) value);
        else if (value instanceof Integer)
            compoundTag.putInt(key, (Integer) value);
        else if (value instanceof Double)
            compoundTag.putDouble(key, (Double) value);
        else if (value instanceof Float)
            compoundTag.putFloat(key, (Float) value);
        else if (value instanceof Long)
            compoundTag.putLong(key, (Long) value);
        else if (value instanceof Short)
            compoundTag.putShort(key, (Short) value);
        else if (value instanceof Boolean)
            compoundTag.putBoolean(key, (Boolean) value);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    @Override
    public <T> T getTag(ItemStack itemStack, String key, Class<T> typeClass, Object defaultValue) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

        if (nmsStack == null || nmsStack.isEmpty())
            return typeClass.cast(defaultValue);

        CompoundTag compoundTag = nmsStack.getOrCreateTag();

        if (!compoundTag.contains(key))
            return typeClass.cast(defaultValue);
        else if (typeClass.equals(String.class))
            return typeClass.cast(compoundTag.getString(key));
        else if (typeClass.equals(Integer.class))
            return typeClass.cast(compoundTag.getInt(key));
        else if (typeClass.equals(Double.class))
            return typeClass.cast(compoundTag.getDouble(key));
        else if (typeClass.equals(Float.class))
            return typeClass.cast(compoundTag.getFloat(key));
        else if (typeClass.equals(Long.class))
            return typeClass.cast(compoundTag.getLong(key));
        else if (typeClass.equals(Short.class))
            return typeClass.cast(compoundTag.getShort(key));
        else if (typeClass.equals(Boolean.class))
            return typeClass.cast(compoundTag.getBoolean(key));

        throw new IllegalArgumentException("nbt class type not found " + typeClass);
    }
}
