package com.tyrengard.aureycore.customguis;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ACustomGUI implements InventoryHolder {
	protected Inventory inventory;
	protected Player player;
	
	final void assignPlayer(Player p) {
		this.player = p;
		this.onAssign();
	}
	protected abstract void onAssign();
	protected void onOpen(Player p) { return; }
	protected void onClose(Player p) { return; }
	
	protected abstract void update();
	
	protected abstract String getName();
	protected abstract ItemStack[] getContents();

	protected void updateAsync(Plugin plugin) {
		new BukkitRunnable() {
			@Override
			public void run() {
				update();
			}
		}.runTask(plugin).getTaskId();
	}
}
