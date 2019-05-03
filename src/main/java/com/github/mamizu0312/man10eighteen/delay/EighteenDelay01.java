package com.github.mamizu0312.man10eighteen.delay;

import com.github.mamizu0312.man10eighteen.EighteenConfigManager;
import com.github.mamizu0312.man10eighteen.EighteenMySQLManager;
import com.github.mamizu0312.man10eighteen.Man10Eighteen;
import com.github.mamizu0312.man10eighteen.VaultManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EighteenDelay01 extends BukkitRunnable {
    //delay01:一定期間操作がなかった場合の処理
    Man10Eighteen plugin;
    VaultManager vault;
    EighteenConfigManager config;
    EighteenMySQLManager mysql;
    Player p1;
    Player p2;

    public EighteenDelay01(Man10Eighteen plugin, Player p1, Player p2) {
        this.plugin = plugin;
        this.vault = new VaultManager(plugin);
        this.config = new EighteenConfigManager(plugin);
        this.mysql = new EighteenMySQLManager(plugin, "Man10Eighteen");
        this.p1 = p1;
        this.p2 = p2;
    }
    public EighteenDelay01(Man10Eighteen plugin) {
        this.plugin = plugin;
        this.vault = new VaultManager(plugin);
        this.config = new EighteenConfigManager(plugin);
        this.mysql = new EighteenMySQLManager(plugin, "Man10Eighteen");
    }

    @Override
    public void run() {
        if(plugin.p1canchooserps && plugin.p2canchooserps) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§l" + p1.getName() + "と" + p2.getName() + "がどちらも一定時間以上放置したため、試合は中止となり賭け金はすべてストックへ追加されます");
            p1.closeInventory();
            p2.closeInventory();
            plugin.specialbonus += plugin.betmoney * 2;
            config.reload();
            plugin.reset();
            return;
        }
        if (!plugin.p1canchooserps && !plugin.p2canchooserps) {
            return;
        }
        if (plugin.p1canchooserps) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + p1.getName() + "が一定時間以上放置したため、" + p2.getName() + "さんの勝利となりました");
            p1.closeInventory();
            vault.deposit(p2.getUniqueId(), plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive);
            mysql.senddepositinfo(p2, plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive);
            plugin.specialbonus += plugin.betmoney * 2 * plugin.bonuscompetitive;
            config.reload();
            plugin.reset();
            return;
        }
        Bukkit.getServer().broadcastMessage(plugin.prefix + "§l" + p2.getName() + "が一定時間以上放置したため、" + p1.getName() + "さんの勝利となりました");
        p2.closeInventory();
        vault.deposit(p1.getUniqueId(), plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive);
        mysql.senddepositinfo(p1, plugin.betmoney * 2 - plugin.betmoney * 2 * plugin.bonuscompetitive);
        plugin.specialbonus += plugin.betmoney * 2 * plugin.bonuscompetitive;
        config.reload();
        plugin.reset();
    }
}
