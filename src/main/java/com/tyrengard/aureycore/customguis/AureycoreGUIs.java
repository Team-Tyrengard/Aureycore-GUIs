package com.tyrengard.aureycore.customguis;

import org.bukkit.plugin.java.JavaPlugin;

public final class AureycoreGUIs extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CustomGUIManager(), this);
    }

    @Override
    public void onDisable() {

    }
}
