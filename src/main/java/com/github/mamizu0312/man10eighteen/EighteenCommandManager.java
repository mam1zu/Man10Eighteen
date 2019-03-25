package com.github.mamizu0312.man10eighteen;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class EighteenCommandManager implements CommandExecutor {
    Man10Eighteen plugin;
    EighteenConfigManager config;
    EighteenBattleManager battle;
    VaultManager vault;
    public EighteenCommandManager(Man10Eighteen plugin) {
        this.plugin = plugin;
        config = plugin.config;
        vault = new VaultManager(plugin);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if(!p.hasPermission("mer.play")) {
            p.sendMessage(plugin.prefix + "§4§l権限がありません");
        }
        if(args.length == 0) {
            HelpCommand(p);
        }
        if(args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                if(!p.hasPermission("mer.staff")) {
                    p.sendMessage(plugin.prefix + "§4§l権限がありません");
                }
                if (!plugin.plstatus) {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを起動しています...");
                    plugin.reset();
                    config.setPluginStatus(true);
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを起動しました！");
                    return true;
                }
                p.sendMessage(plugin.prefix + "§c§lプラグインはすでに起動しています");
                return true;
            }
            if (args[0].equalsIgnoreCase("off")) {
                if(!p.hasPermission("mer.staff")) {
                    p.sendMessage(plugin.prefix + "§4§l権限がありません");
                }
                if (plugin.plstatus) {
                    if (!plugin.onGame.isEmpty()) {
                        Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しています...");
                        config.setPluginStatus(false);
                        if(plugin.prewait) {
                            p.sendMessage(plugin.prefix + "§c§l試合がキャンセルされました。賭け金は返金されます");
                            vault.deposit(p.getUniqueId(), plugin.betmoney);
                            plugin.onGame.clear();
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しました。");
                            return true;
                        }
                        battle.emergencystop();
                        Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しました。");
                        return true;
                    }
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しています...");
                    plugin.reset();
                    config.setPluginStatus(false);
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しました。");
                    return true;
                }
                p.sendMessage(plugin.prefix + "§c§lプラグインはすでに停止しています");
                return true;
            }
            if(args[0].equalsIgnoreCase("reload")) {
                if(!p.hasPermission("mer.staff")) {
                    p.sendMessage(plugin.prefix + "§4§l権限がありません");
                    return true;
                }
                config.reload();
                p.sendMessage(plugin.prefix + "§e§lconfigをリロードしました。");
                return true;
            }
            if(args[0].equalsIgnoreCase("help")) {
                HelpCommand(p);
                return true;
            }
            if(args[0].equalsIgnoreCase("game")) {
                p.sendMessage(plugin.prefix + "§c§l掛け金を入力してください。");
                return true;
            }
            if(args[0].equalsIgnoreCase("join")) {
                if (plugin.onGame.isEmpty()) {
                    p.sendMessage(plugin.prefix + "§c§l現在開催されていません");
                    return true;
                }
                if(!plugin.prewait) {
                    p.sendMessage(plugin.prefix+"§c§l現在開催中です");
                    return true;
                }
                if (plugin.onGame.get(0) == p.getUniqueId()) {
                    p.sendMessage(plugin.prefix + "§c§lあなたはすでに参加しています");
                    return true;
                }
                if(vault.getBalance(p.getUniqueId()) <= plugin.betmoney) {
                    p.sendMessage(plugin.prefix + "§c§l所持金が足りません");
                    return true;
                }
                Random r = new Random();
                if(r.nextInt(plugin.chance)+1 == 1) {
                    plugin.fevertime = true;
                    getServer().broadcastMessage(plugin.prefix + "§e§l" + p.getName() + "§fさんが参加しました！ゲームを開始します...§ka");
                    getServer().broadcastMessage(plugin.prefix + "§ka§r§e§lBonusTime§ka");
                    vault.withdraw(p.getUniqueId(), plugin.betmoney);
                    plugin.onGame.add(p.getUniqueId());
                    battle = new EighteenBattleManager(plugin, Bukkit.getPlayer(plugin.onGame.get(0)), p);
                    plugin.event.battle =  battle;
                    battle.game();
                    plugin.p1canchooserps = true;
                    plugin.p2canchooserps = true;
                    plugin.prewait = false;
                    return true;
                }
                vault.withdraw(p.getUniqueId(), plugin.betmoney);
                getServer().broadcastMessage(plugin.prefix + "§e§l" + p.getName() + "§fさんが参加しました！ゲームを開始します...");
                plugin.prewait = false;
                plugin.onGame.add(p.getUniqueId());
                battle = new EighteenBattleManager(plugin, Bukkit.getPlayer(plugin.onGame.get(0)), p);
                plugin.event.battle =  battle;
                battle.game();
                plugin.p1canchooserps = true;
                plugin.p2canchooserps = true;
            }
            if(args[0].equalsIgnoreCase("reopen")) {
                if(plugin.onGame.isEmpty()) {
                    p.sendMessage(plugin.prefix + "§c§lあなたは試合に参加していません");
                    return true;
                }
                if(plugin.onGame.get(0) == p.getUniqueId()) {
                    p.openInventory(plugin.p1inv);
                    return true;
                }
                if(plugin.onGame.get(1) == p.getUniqueId()) {
                    p.openInventory(plugin.p2inv);
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("stop")) {
                if(!p.hasPermission("mer.staff")) {
                    p.sendMessage(plugin.prefix + "§4§l権限がありません");
                    return true;
                }
                if(plugin.onGame.isEmpty()) {
                    p.sendMessage(plugin.prefix + "§c§l現在開催されていません");
                    return true;
                }
                if(plugin.prewait) {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§4§l試合がキャンセルされました");
                    p.sendMessage(plugin.prefix + "§c§l試合がキャンセルされました。賭け金は返金されます");
                    vault.deposit(p.getUniqueId(), plugin.betmoney);
                    plugin.onGame.clear();
                    return true;
                }
                battle.emergencystop();
                return true;
            }
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("game")) {
            if(!plugin.plstatus) {
                p.sendMessage(plugin.prefix + "§e§l現在プラグインは停止しています");
                return true;
            }
            if(!plugin.onGame.isEmpty()) {
                p.sendMessage(plugin.prefix+"§c§l現在開催中です");
                return true;
            }
            try {
                plugin.betmoney = Double.parseDouble(args[1]);
            } catch(NumberFormatException e) {
                p.sendMessage(plugin.prefix + "§c§l賭け金が不正です");
                return true;
            }
            if(plugin.betmoney < plugin.minimumbet || plugin.betmoney > plugin.maximumbet) {
                p.sendMessage(plugin.prefix + "§c§l賭け金が少なすぎるまたは多すぎます");
                return true;
            }
            vault.withdraw(p.getUniqueId(), plugin.betmoney);
            getServer().broadcastMessage(plugin.prefix + "§e§l"+p.getName()+"§fさんが§e"+moneyformat((int) plugin.betmoney)+"§fで試合を開きました！ §a/mer join§fで参加！");
            plugin.prewait = true;
            plugin.onGame.add(p.getUniqueId());
        }
        return true;
    }
    void HelpCommand(Player p) {
        if(p.hasPermission("mer.staff") || p.isOp()) {
            p.sendMessage("----------"+plugin.prefix+"----------");
            p.sendMessage("§e§l/mer help§f: このページを開きます");
            p.sendMessage("§e§l/mer game§f: 新しく試合を開きます");
            p.sendMessage("§e§l/mer join§f: 試合に入ります");
            p.sendMessage("§e§l/mer reopen§f: メニューを再度開きます");
            p.sendMessage("§c§l/mer on  §f: プラグインを起動します");
            p.sendMessage("§c§l/mer off §f: プラグインを停止します");
            p.sendMessage("§c§l/mer stop §f: 試合をストップします");
            return;
        }
        p.sendMessage("----------"+plugin.prefix+"----------");
        p.sendMessage("§e§l/mer help§f: このページを開きます");
        p.sendMessage("§e§l/mer game§f: 新しく試合を開きます");
        p.sendMessage("§e§l/mer join§f: 試合に入ります");
        p.sendMessage("§e§l/mer reopen§f: メニューを再度開きます");
    }
    String moneyformat(int money) {
        if(money >= 1000000000) {
            int under1000m = money - 1000000000;
            return money / 100000000+"億"+under1000m+"万円";
        }
        if(money >= 100000000) {
            int under100m = money - 100000000;
            return money / 100000000+"億"+under100m+"万円";
        }
        if(money >= 1000000) {
            return money / 10000 +"万円";
        }
        return null;
    }
}