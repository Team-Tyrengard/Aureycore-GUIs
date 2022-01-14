package com.tyrengard.aureycore.customguis;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public final class CustomGUIUtil {
    public static ItemStack[] getSpacerContent(int size) {
        ItemStack[] content = new ItemStack[size];
        Arrays.fill(content, CustomGUIItem.SPACER);
        return content;
    }
}
