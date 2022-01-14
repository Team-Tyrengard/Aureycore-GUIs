package com.tyrengard.aureycore.customguis;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

public final class Button<G extends ACustomGUI> extends CustomGUIItem<G> {
	private final ButtonClickHandler<G, InventoryClickEvent, Button<G>> clickHandler;
	
	public Button(Material mat, String name, ButtonClickHandler<G, InventoryClickEvent, Button<G>> clickHandler) {
		this(mat, name, null, clickHandler);
	}
	
	public Button(Material mat, String name, List<String> lore, boolean enchanted, ButtonClickHandler<G, InventoryClickEvent, Button<G>> clickHandler) {
		this(mat, 1, name, lore, enchanted ? Collections.singletonMap(Enchantment.LOYALTY, 1) : null, clickHandler);
	}
	
	public Button(Material mat, String name, List<String> lore, ButtonClickHandler<G, InventoryClickEvent, Button<G>> clickHandler) {
		this(mat, 1, name, lore, null, clickHandler);
	}
	
	public Button(Material mat, String name, List<String> lore, ButtonClickHandler<G, InventoryClickEvent, Button<G>> clickHandler,
			ItemFlag... flags) {
		this(mat, 1, name, lore, null, clickHandler, flags);
	}
	
	public Button(Material mat, int amount, String name, List<String> lore, Map<Enchantment, Integer> enchantments,
				  ButtonClickHandler<G, InventoryClickEvent, Button<G>> clickHandler) {
		super(mat, amount, name, lore, enchantments, ItemFlag.HIDE_ENCHANTS);
		
		this.clickHandler = clickHandler;
	}
	
	public Button(Material mat, int amount, String name, List<String> lore, Map<Enchantment, Integer> enchantments,
				  ButtonClickHandler<G, InventoryClickEvent, Button<G>> clickHandler, ItemFlag... flags) {
		super(mat, amount, name, lore, enchantments, flags);
		
		this.clickHandler = clickHandler;
	}
	
	@Override
	public void onClick(G gui, InventoryClickEvent e) {
		clickHandler.accept(gui, e, this);
	}
	
	public void changeName(String newName) {
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(newName);
		this.setItemMeta(meta);
	}

	@FunctionalInterface
	public interface ButtonClickHandler<G extends ACustomGUI, InventoryClickEvent, B extends Button<G>> {
		void accept(G gui, InventoryClickEvent e, B b);
	}
}
