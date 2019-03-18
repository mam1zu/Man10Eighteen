package com.github.mamizu0312.man10eighteen;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Man10Eighteen extends JavaPlugin {
    static HashMap<UUID, String> playerstatus = new HashMap<>();
    @Override
    public void onEnable() {
        getCommand("man10eighteen").setExecutor(new EighteenCommandManager());
        new EighteenEventManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
