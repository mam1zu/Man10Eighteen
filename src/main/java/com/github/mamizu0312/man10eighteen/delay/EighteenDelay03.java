package com.github.mamizu0312.man10eighteen.delay;

import com.github.mamizu0312.man10eighteen.Man10Eighteen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EighteenDelay03 extends BukkitRunnable {
    Man10Eighteen plugin;
    Player p1;
    Player p2;
    int judgetime = 8;
    public EighteenDelay03(Man10Eighteen plugin) {
        this.plugin = plugin;
    }
    public EighteenDelay03(Man10Eighteen plugin, Player p1, Player p2) {
        this.plugin = plugin;
        this.p1 = p1;
        this.p2 = p2;
    }
    public void run() {
        if(judgetime == 7) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§ka");
        }
        if(judgetime == 5) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§lぽん！");
        }
        if(judgetime == 3) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§3§l" + p1.getName() + "§r :" + fingertostring(plugin.p1putoutfinger));
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§l" + p2.getName() + "§r :" + fingertostring(plugin.p2putoutfinger));
        }
        if(judgetime == 1) {
            switch (plugin.round) {
                case 6:
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第" + plugin.round + "ラウンドは§e" + p2.getName() + "§fの勝利！");
                    plugin.p2score += 2;
                    plugin.round++;
                    break;
                case 10:
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第" + plugin.round + "ラウンドは§e" + p2.getName() + "§fの勝利！");
                    plugin.p2score += 2;
                    cancel();
                    plugin.battle.lastjudge();
                    return;
                default:
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第" + plugin.round + "ラウンドは§e" + p2.getName() + "§fの勝利！");
                    plugin.p2score++;
                    plugin.round++;
                    break;
            }
        }
        if(judgetime == 0) {
            plugin.delay03status = false;
            cancel();
            judgetime = 8;
            plugin.battle.restart();
            return;
        }
        judgetime--;
    }
    String fingertostring(int putoutfinger) {
        if (putoutfinger == 0) {
            return "グー";
        }
        if (putoutfinger == 2) {
            return "チョキ";
        }
        if (putoutfinger == 5) {
            return "パー";
        }
        return "？";
    }
}
