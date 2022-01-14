package com.tyrengard.aureycore.customguis;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ACustomSpecialGUI extends ACustomGUI {
    protected final InventoryType inventoryType;

    public ACustomSpecialGUI(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    @Override
    public Inventory getInventory() {
        if (inventory == null) {
            inventory = Bukkit.createInventory(this, inventoryType);
            inventory.setContents(getContents());
        }

        return inventory;
    }

    @Override
    protected ItemStack[] getContents() {
        return inventory.getContents();
    }

    @Override
    protected void update() {
        inventory.setContents(getContents());
    }
}
