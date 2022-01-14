package com.tyrengard.aureycore.customguis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public final class Information extends CustomGUIItem<ACustomGUI> {
	public Information(ItemStack item, String... lore) {
		this(item.getType(), item.hasItemMeta() && item.getItemMeta().getEnchants().size() > 0, getItemName(item),
				item.hasItemMeta() && item.getItemMeta().hasLore()
					? Stream.concat(item.getItemMeta().getLore().stream(), Arrays.stream(lore)).collect(Collectors.toList()) 
					: Arrays.asList(lore));
	}
	
	public Information(Material mat, boolean enchanted, String title, String... lore) {
		this(mat, enchanted, title, Arrays.asList(lore));
	}

	@SuppressWarnings("serial")
	public Information(Material mat, boolean enchanted, String title, List<String> lore) {
		this(mat, title, lore, enchanted ? new HashMap<Enchantment, Integer>() {
			{
				put(Enchantment.LOYALTY, 1);
			}
		} : null, enchanted ? ItemFlag.HIDE_ENCHANTS : null);
	}

	public Information(Material mat, String title, List<String> lore, HashMap<Enchantment, Integer> enchantments, ItemFlag... flags) {
		super(mat, 1, title, lore, enchantments, flags);
	}

	private static String getItemName(ItemStack item) {
		if (item.hasItemMeta()) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName()) return item.getItemMeta().getDisplayName();
			else if (meta instanceof PotionMeta) {
				PotionType type = ((PotionMeta) meta).getBasePotionData().getType();
				switch (type) {
					case INSTANT_HEAL: return "Potion of Healing";
					default: return "Potion of " + toTitleCase(type.toString().replace("_", " ").toLowerCase());
				}
			}
			else return getItemDefaultName(item);
		}
		else return getItemDefaultName(item);
	}

	private static String getItemDefaultName(ItemStack item) {
		return toTitleCase(item.getType().toString().replace("_", " ").toLowerCase());
	}

	private static String toTitleCase(String input) {
		return String.join(" ", Arrays.stream(ChatColor.stripColor(input).split("[ _]"))
				.map(word -> String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1).toLowerCase())
				.toArray(String[]::new));
	}
}
