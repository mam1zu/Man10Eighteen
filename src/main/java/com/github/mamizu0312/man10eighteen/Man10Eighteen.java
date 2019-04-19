package com.github.mamizu0312.man10eighteen;

import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Man10Eighteen extends JavaPlugin {
    public List<UUID> onGame = new ArrayList<>();
    public List<UUID> onVoting = new ArrayList<>();
    public List<UUID> votep1 = new ArrayList<>();
    public List<UUID> votep2 = new ArrayList<>();
    boolean plstatus;
    public Inventory p1inv;
    public Inventory p2inv;
    public final int rockfinger = 0;
    public final int scissorfinger = 2;
    public final int paperfinger = 5;
    public int p1putoutfinger = 0;
    public int p2putoutfinger = 0;
    boolean prewait = false;
    public boolean p1canchooserps = false;
    public boolean p2canchooserps = false;
    String prefix;
    VaultManager vault;
    public EighteenEventManager event;
    public EighteenBattleManager battle;
    EighteenConfigManager config;
    EighteenVoteManager vote;
    int round = 1;
    int p1score = 0;
    int p2score = 0;
    int p1finger = 18;
    int p2finger = 18;
    //最小掛け金、最大掛け金はconfigで設定
    double minimumbet;
    double maximumbet;
    double betmoney = 0;
    boolean fevertime = false;
    double specialbonus;
    double bonuscompetitive = 0.05;
    int chance;
    String HOST;
    String USER;
    String PASS;
    String PORT;
    String DB;
    //p1status, p2statusはtrueでオンライン、falseでオフライン
    boolean p1status;
    boolean p2status;
    //votetimeはtrueで投票受付中、falseで投票受付停止中
    boolean votetime;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("mer").setExecutor(new EighteenCommandManager(this));
        event = new EighteenEventManager(this);
        getServer().getPluginManager().registerEvents(event, this);
        config = new EighteenConfigManager(this);
        config.load();
        vault = new VaultManager(this);
        vote = new EighteenVoteManager(this);
    }

    @Override
    public void onDisable() {
        config.save();
    }

    public void reset() {
        onGame.clear();
        p1putoutfinger = 0;
        p2putoutfinger = 0;
        round = 1;
        p1score = 0;
        p2score = 0;
        p1finger = 18;
        p2finger = 18;
        betmoney = 0;
        this.battle = new EighteenBattleManager(this);
        prewait = false;
        fevertime = false;
        p1status = false;
        p2status = false;
        votetime = false;
    }
}
