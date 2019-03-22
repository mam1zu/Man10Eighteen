package com.github.mamizu0312.man10eighteen;

import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Man10Eighteen extends JavaPlugin {
    public List<UUID> onMenu = new ArrayList<>();
    public List<UUID> onGame = new ArrayList<>();
    public Map<UUID, UUID> onWait = new HashMap<>();

    public Inventory p1inv;
    public Inventory p2inv;
    public Inventory menuinv;

    public final String prefix = "§l[§dM§fa§an§f10§6Eighteen§f]§r";
    public EighteenEventManager event;
    public EighteenBattleManager battle;

    @Override
    public void onEnable() {
        getCommand("m18").setExecutor(new EighteenCommandManager(this));
        event = new EighteenEventManager(this);
        battle = new EighteenBattleManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
