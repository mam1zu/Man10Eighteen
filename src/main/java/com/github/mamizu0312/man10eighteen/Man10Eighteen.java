package com.github.mamizu0312.man10eighteen;

import com.github.mamizu0312.man10eighteen.betselecter.MoneySelectInventoryAPI;
import com.github.mamizu0312.man10eighteen.delay.EighteenDelay01;
import com.github.mamizu0312.man10eighteen.delay.EighteenDelay02;
import com.github.mamizu0312.man10eighteen.delay.EighteenDelay03;
import com.github.mamizu0312.man10eighteen.delay.EighteenDelay04;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
    public String prefix;
    public VaultManager vault;
    public EighteenEventManager event;
    public EighteenBattleManager battle;
    EighteenConfigManager config;
    EighteenVoteManager vote;
    public int round = 1;
    public int p1score = 0;
    public int p2score = 0;
    public int p1finger = 18;
    public int p2finger = 18;
    //最小掛け金、最大掛け金はconfigで設定
    public double minimumbet;
    public double maximumbet;
    public double betmoney = 0;
    public boolean fevertime = false;
    public double specialbonus;
    public double bonuscompetitive = 0.05;
    public int chance;
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
    EighteenDelay01 delay01 ;//delay01: 一定期間操作がなかった場合の処理
    EighteenDelay02 delay02; //delay02: じゃんけんの処理(勝者p1の場合)
    EighteenDelay03 delay03; //delay03] じゃんけんの処理(勝者p2の場所)
    EighteenDelay04 delay04; //delay04: 勝敗の処理
    //MoneySelectInventoryAPI
    MoneySelectInventoryAPI msi;
    public boolean delay01status;
    public boolean delay02status;
    public boolean delay03status;
    public boolean delay04status;

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
        msi = new MoneySelectInventoryAPI(this,"Man10Eighteen");
        delay01 = new EighteenDelay01(this);
        delay02 = new EighteenDelay02(this);
        delay03 = new EighteenDelay03(this);
        delay04 = new EighteenDelay04(this);
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
        onVoting.clear();
        votep1.clear();
        votep2.clear();
        p1canchooserps = false;
        p2canchooserps = false;
        delay01 = new EighteenDelay01(this);
        delay02 = new EighteenDelay02(this);
        delay03 = new EighteenDelay03(this);
        delay04 = new EighteenDelay04(this);
        delay01status = false;
        delay02status = false;
        delay03status = false;
        delay04status = false;
    }
    public void cancelAllTasks() {
        if(delay01status) {
            delay01.cancel();
        }
        if(delay02status) {
            delay02.cancel();
        }
        if(delay03status) {
            delay03.cancel();
        }
        if(delay04status) {
            delay04.cancel();
        }
    }

    public static String getJpBal(double balance) {
        long val = (long) balance;
        String addition = "";
        String form = "万";
        long man = val / 10000;
        if (val >= 100000000) {
            man = val / 100000000;
            form = "億";
            long mann = (val - man * 100000000) / 10000;
            addition = mann + "万";
        }
        return man + form + addition;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////
    //  マインクラフトチャットに、ホバーテキストや、クリックコマンドを設定する関数
    // [例1] sendHoverText(player,"ここをクリック",null,"/say おはまん");
    // [例2] sendHoverText(player,"カーソルをあわせて","ヘルプメッセージとか",null);
    // [例3] sendHoverText(player,"カーソルをあわせてクリック","ヘルプメッセージとか","/say おはまん");
    public static void sendHoverText(Player p, String text, String hoverText, String command){
        //////////////////////////////////////////
        //      ホバーテキストとイベントを作成する
        HoverEvent hoverEvent = null;
        if(hoverText != null){
            BaseComponent[] hover = new ComponentBuilder(hoverText).create();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        }

        //////////////////////////////////////////
        //   クリックイベントを作成する
        ClickEvent clickEvent = null;
        if(command != null){
            clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND,command);
        }

        BaseComponent[] message = new ComponentBuilder(text).event(hoverEvent).event(clickEvent). create();
        p.spigot().sendMessage(message);
    }

    //  マインクラフトチャットに、ホバーテキストや、クリックコマンドサジェストを設定する
    public static void sendSuggestCommand(Player p, String text, String hoverText, String command){

        //////////////////////////////////////////
        //      ホバーテキストとイベントを作成する
        HoverEvent hoverEvent = null;
        if(hoverText != null){
            BaseComponent[] hover = new ComponentBuilder(hoverText).create();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        }

        //////////////////////////////////////////
        //   クリックイベントを作成する
        ClickEvent clickEvent = null;
        if(command != null){
            clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND ,command);
        }

        BaseComponent[] message = new ComponentBuilder(text). event(hoverEvent).event(clickEvent). create();
        p.spigot().sendMessage(message);
    }

    public static void playSound(String uuid, Sound sound){
        Player p = Bukkit.getPlayer(UUID.fromString(uuid));
        if(p == null){
            return;
        }
        if(p.isOnline()){
            p.playSound(p.getLocation(),sound,1,1);
        }
    }


}
