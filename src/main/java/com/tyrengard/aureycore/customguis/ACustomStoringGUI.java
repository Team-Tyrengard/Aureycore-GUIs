package com.tyrengard.aureycore.customguis;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class ACustomStoringGUI extends ACustomChestGUI {
	Inventory storageInventory;
	private boolean storageInventoryIsLocked;
	private final int[] storageSlots;

	public ACustomStoringGUI(int size, int[] storageSlots) {
		super(size);
		storageInventory = Bukkit.createInventory(null, size);
		this.storageSlots = storageSlots;
	}
	
	@Override
	protected void onClose(Player p) {
		super.onClose(p);
		Inventory playerInv = p.getInventory();
		Location playerLoc = p.getLocation();
		ItemStack[] items = Arrays.stream(storageSlots).boxed().map(slot -> storageInventory.getItem(slot))
				.filter(item -> item != null && item.getType() != Material.AIR).toArray(ItemStack[]::new);
		for (ItemStack item : items) {
			if (item != null && item.getType() != Material.AIR)
				for (ItemStack excessItem : playerInv.addItem(item).values())
					if (excessItem != null && excessItem.getType() != Material.AIR)
						p.getWorld().dropItem(playerLoc, excessItem);
		}
		storageInventory.clear();
	}
	
	@Override
	protected void update() {
		super.update();
		// copy over items from GUI storage
		for(int slot : storageSlots) {
			ItemStack item = storageInventory.getItem(slot);
			if (item != null) inventory.setItem(slot, item);
		}
	}
	
	protected final void clearStorage() { 
		for(int slot : storageSlots)
			storageInventory.setItem(slot, null);
	}
	protected final void lockStorage() { storageInventoryIsLocked = true; }
	protected final void unlockStorage() { storageInventoryIsLocked = false; }
	final boolean isStorageLocked() { return storageInventoryIsLocked; }

	final void updateStorageInventory() {
		final Inventory inv = getInventory();
		for (int slot : storageSlots)
			storageInventory.setItem(slot, inv.getItem(slot));
		onStorageInventoryChange(storageInventory);
	}
	
//	final void updateStorageInventoryAsync() {
//		final Inventory inv = getInventory();
//		TaskUtils.runTask(AureyCore.getInstance(), () -> {
//			for (int slot : storageSlots)
//				storageInventory.setItem(slot, inv.getItem(slot));
//			onStorageInventoryChange(storageInventory);
//		});
//	}
	
	protected final void forceUpdateStorageInventory() { this.onStorageInventoryChange(storageInventory); }
	
	protected final void removeItemFromStorage(ItemStack item) {
		storageInventory.removeItem(item);
		onStorageInventoryChange(storageInventory);
	}
	
	protected void onStorageInventoryChange(Inventory storageInventory) {
		return;
	}
	
	private int lockedPlayerInventorySlot = -1;
	protected final void setLockedPlayerInventorySlot(int slot) { lockedPlayerInventorySlot = slot; }
	final int getLockedPlayerInventorySlot() { return lockedPlayerInventorySlot; }
}
