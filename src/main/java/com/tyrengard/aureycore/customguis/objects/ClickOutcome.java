package com.tyrengard.aureycore.customguis.objects;

import com.tyrengard.aureycore.customguis.enums.ClickAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public record ClickOutcome(ClickAction clickAction,
                           ItemStack itemStackInInventory, Inventory inventory, int slot,
                           ItemStack itemStackInCursor) {
}
