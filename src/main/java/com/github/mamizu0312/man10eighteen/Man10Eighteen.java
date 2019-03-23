package com.github.mamizu0312.man10eighteen;

import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Man10Eighteen extends JavaPlugin {
    public List<UUID> onGame = new ArrayList<>();
    boolean plstatus;
    public Inventory p1inv;
    public Inventory p2inv;
    public int rockfinger = 0;
    public int scissorfinger = 2;
    public int paperfinger = 5;
    public int p1putoutfinger = 0;
    public int p2putoutfinger = 0;
    public boolean p1canchooserps = false;
    public boolean p2canchooserps = false;
    String prefix;
    public EighteenEventManager event;
    public EighteenBattleManager battle;
    EighteenConfigManager config;
    boolean ingame = false;
    int round = 0;
    int p1score = 0;
    int p2score = 0;
    int p1finger = 18;
    int p2finger = 18;
    //最小掛け金1000万円、最大掛け金10億円
    double minimumbet = 10000000;
    double maximumbet = 1000000000;
    double betmoney = 0;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config.loadConfig();
        getCommand("mer").setExecutor(new EighteenCommandManager(this));
        event = new EighteenEventManager(this);
        battle = new EighteenBattleManager(this);
        config = new EighteenConfigManager(this);
    }

    @Override
    public void onDisable() {
        config.writePluginStatus();
    }
}
