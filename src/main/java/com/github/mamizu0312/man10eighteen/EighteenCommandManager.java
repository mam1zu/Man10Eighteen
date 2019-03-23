package com.github.mamizu0312.man10eighteen;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class EighteenCommandManager implements CommandExecutor {
    Man10Eighteen plugin;
    EighteenConfigManager config;
    EighteenBattleManager battle;
    public EighteenCommandManager(Man10Eighteen plugin) {
        this.plugin = plugin;
        config = new EighteenConfigManager(plugin);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if(args.length == 0) {
            HelpCommand(p);
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("on")) {
                if(!plugin.plstatus) {
                    config.setPluginStatus(true);
                    p.sendMessage(plugin.prefix + "§e§lプラグインをオンにしました");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("off")) {
                if(plugin.plstatus) {
                    if(plugin.ingame) {
                        //TODO:試合中の場合も強制終了できるメソッドを作っておくこと
                    }
                    config.setPluginStatus(false);
                    p.sendMessage(plugin.prefix + "§c§lプラグインをオフにしました。");
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("help")) {
                HelpCommand(p);
                return true;
            }
            if(args[0].equalsIgnoreCase("game")) {
                p.sendMessage("§c§l掛け金を入力してください。");
                return true;
            }
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("game")) {
            if(!plugin.onGame.isEmpty()) {
                p.sendMessage(plugin.prefix+"§c§l現在開催中または試合中です");
            }
            try {
                plugin.betmoney = Double.parseDouble(args[1]);
            } catch(NumberFormatException e) {
                p.sendMessage(plugin.prefix + "§c§l賭け金が不正です");
                return true;
            }
            if(plugin.betmoney < plugin.minimumbet || plugin.betmoney > plugin.maximumbet) {
                p.sendMessage(plugin.prefix + "§c§l");
            }
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p.getName()+"§fさんが§e"+moneyformat((int) plugin.betmoney)+"§fで試合を開きました！ §a/mer join§fで参加！");
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
            p.sendMessage("§c§l/mer on  §f: プラグインを起動します");
            p.sendMessage("§c§l/mer off §f: プラグインを停止します");
        }
        p.sendMessage("----------"+plugin.prefix+"----------");
        p.sendMessage("§e§l/mer help§f: このページを開きます");
        p.sendMessage("§e§l/mer game§f: 新しく試合を開きます");
        p.sendMessage("§e§l/mer join§f: 試合に入ります");
    }
    String moneyformat(int money) {
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