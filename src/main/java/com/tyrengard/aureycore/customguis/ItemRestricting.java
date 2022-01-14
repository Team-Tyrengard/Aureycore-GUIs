package com.tyrengard.aureycore.customguis;

import org.bukkit.inventory.ItemStack;

public interface ItemRestricting {
    boolean slotAllowsItemStack(int slot, ItemStack itemStack);
}
