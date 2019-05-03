package com.github.mamizu0312.man10eighteen.delay;

import com.github.mamizu0312.man10eighteen.EighteenConfigManager;
import com.github.mamizu0312.man10eighteen.EighteenMySQLManager;
import com.github.mamizu0312.man10eighteen.Man10Eighteen;
import com.github.mamizu0312.man10eighteen.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class EighteenDelay04 extends BukkitRunnable {
    Man10Eighteen plugin;
    VaultManager vault;
    EighteenConfigManager config;
    EighteenMySQLManager mysql;
    Player p1;
    Player p2;

    public EighteenDelay04(Man10Eighteen plugin) {
        this.plugin = plugin;
        this.vault = plugin.vault;
    }

    public EighteenDelay04(Man10Eighteen plugin, Player p1, Player p2) {
        this.plugin = plugin;
        this.p1 = p1;
        this.p2 = p2;
        this.mysql = new EighteenMySQLManager(plugin, "Man10Eighteen");
        this.config = new EighteenConfigManager(plugin);
    }

    public void run() {
        Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + p1.getName() + "§fのスコア: " + plugin.p1score);
        Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + p2.getName() + "§fのスコア: " + plugin.p2score);
        if (plugin.p1score == plugin.p2score) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l引き分け！");
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f試合を終了します");
            vault.deposit(p1.getUniqueId(), plugin.betmoney);
            mysql.senddepositinfo(p1, plugin.betmoney);
            vault.deposit(p2.getUniqueId(), plugin.betmoney);
            mysql.senddepositinfo(p2, plugin.betmoney);
            plugin.fevertime = false;
            plugin.cancelAllTasks();
            plugin.reset();
            return;
        }
        if (plugin.p1score > plugin.p2score) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + p1.getName() + "§fの勝ち！");
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§l試合を終了します");
            if (plugin.fevertime) {
                if (plugin.votep1.isEmpty()) {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + p1.getName() + "が勝利しましたが、だれも予想していなかったためストックは勝者にすべて支払われます！");
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + p1.getName() + "は" + plugin.betmoney * 2 + "円とストック" + plugin.specialbonus + "円をゲットした");
                    vault.deposit(p1.getUniqueId(), plugin.betmoney * 2);
                    mysql.senddepositinfo(p1, plugin.betmoney * 2);
                    vault.deposit(p1.getUniqueId(), plugin.specialbonus);
                    mysql.senddepositinfo(p1, plugin.specialbonus);
                    plugin.specialbonus = 0;
                    config.reload();
                    plugin.fevertime = false;
                    plugin.cancelAllTasks();
                    plugin.reset();
                    return;
                }
                Random r = new Random();
                Player bonusplayer = Bukkit.getServer().getPlayer(plugin.votep1.get(r.nextInt(plugin.votep1.size())));
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + p1.getName() + "は" + plugin.betmoney * 2 + "円とストック" + plugin.specialbonus / 2 + "円をゲットした");
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + bonusplayer.getName() + "は抽選で選ばれストック" + plugin.specialbonus / 2 + "円をゲットした");
                vault.deposit(p1.getUniqueId(), plugin.betmoney * 2);
                mysql.senddepositinfo(p1, plugin.betmoney * 2);
                vault.deposit(p1.getUniqueId(), plugin.specialbonus / 2);
                mysql.senddepositinfo(p1, plugin.specialbonus / 2);
                vault.deposit(bonusplayer.getUniqueId(), plugin.specialbonus / 2);
                mysql.senddepositinfo(bonusplayer, plugin.specialbonus / 2);
                plugin.specialbonus = 0;
                config.reload();
                plugin.fevertime = false;
                plugin.cancelAllTasks();
                plugin.reset();
                return;
            }
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + p1.getName() + "は試合に勝利し" + (plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive) + "円をゲットした");
            plugin.specialbonus += plugin.betmoney * 2 * plugin.bonuscompetitive;
            vault.deposit(p1.getUniqueId(), plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive);
            mysql.senddepositinfo(p1, plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive);
            plugin.cancelAllTasks();
            plugin.reset();
        } else {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + p2.getName() + "§fの勝ち！");
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§l試合を終了します");
            if (plugin.fevertime) {
                if (plugin.votep2.isEmpty()) {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l" + p2.getName() + "が勝利しましたが、だれも予想していなかったためストックはすべて勝者に支払われます！");
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + p2.getName() + "は" + plugin.betmoney * 2 + "円とストック" + plugin.specialbonus + "円をゲットした");
                    vault.deposit(p2.getUniqueId(), plugin.betmoney * 2);
                    mysql.senddepositinfo(p2, plugin.betmoney * 2);
                    vault.deposit(p2.getUniqueId(), plugin.specialbonus);
                    mysql.senddepositinfo(p2, plugin.specialbonus);
                    plugin.specialbonus = 0;
                    config.reload();
                    plugin.fevertime = false;
                    plugin.cancelAllTasks();
                    plugin.reset();
                    return;
                }
                Random r = new Random();
                Player bonusplayer = Bukkit.getServer().getPlayer(plugin.votep2.get(r.nextInt(plugin.votep2.size())));
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + p2.getName() + "は" + plugin.betmoney * 2 + "円とストック" + plugin.specialbonus / 2 + "円をゲットした");
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + bonusplayer.getName() + "は抽選で選ばれストック" + plugin.specialbonus / 2 + "円をゲットした");
                vault.deposit(p2.getUniqueId(), plugin.betmoney * 2);
                mysql.senddepositinfo(p2, plugin.betmoney * 2);
                vault.deposit(p2.getUniqueId(), plugin.specialbonus / 2);
                mysql.senddepositinfo(p2, plugin.specialbonus / 2);
                vault.deposit(bonusplayer.getUniqueId(), plugin.specialbonus / 2);
                mysql.senddepositinfo(bonusplayer, plugin.specialbonus / 2);

                plugin.specialbonus = 0;
                config.reload();
                plugin.fevertime = false;
                plugin.cancelAllTasks();
                plugin.reset();
                return;
            }
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + p2.getName() + "は試合に勝利し" + (plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive) + "円をゲットした");
            plugin.specialbonus += plugin.betmoney * 2 * plugin.bonuscompetitive;
            config.reload();
            vault.deposit(p2.getUniqueId(), plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive);
            mysql.senddepositinfo(p2, plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive);
            plugin.cancelAllTasks();
            plugin.reset();
        }
    }
}
