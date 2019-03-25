package com.github.mamizu0312.man10eighteen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class EighteenBattleManager {
    Man10Eighteen plugin;
    EighteenConfigManager config;
    VaultManager vault;
    Player p1;
    Player p2;

    ItemStack roundwatch = new ItemStack(Material.WATCH,1);
    ItemStack p1Skull = new ItemStack(Material.SKULL_ITEM,1, (short) 3);
    ItemStack p2Skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    public EighteenBattleManager(Man10Eighteen plugin) {
        this.plugin = plugin;
        this.vault = plugin.vault;
    }
    public EighteenBattleManager(Man10Eighteen plugin, Player p1, Player p2) {
        this.plugin = plugin;
        this.p1 = p1;
        this.p2 = p2;
        this.vault = new VaultManager(plugin);
        this.config = new EighteenConfigManager(plugin);

    }
    public void game() {
        plugin.p1inv = Bukkit.createInventory(null,27,plugin.prefix);
        plugin.p2inv = Bukkit.createInventory(null,27,plugin.prefix);

        ItemStack rock = new ItemStack(Material.STONE,1,(short)0);
        ItemMeta rockmeta = rock.getItemMeta();
        rockmeta.setDisplayName("§l§7グー§r");
        List<String> rocklore = new ArrayList<>();
        rocklore.add("§r§lグーを選択します。使う指: 0本");
        rockmeta.setLore(rocklore);
        rock.setItemMeta(rockmeta);

        ItemStack scissor = new ItemStack(Material.SHEARS,1,(short)0);
        ItemMeta scissormeta = scissor.getItemMeta();
        scissormeta.setDisplayName("§l§cチョキ§r");
        List<String> scissorlore = new ArrayList<>();
        scissorlore.add("§r§lチョキを選択します。使う指: 2本");
        scissormeta.setLore(scissorlore);
        scissor.setItemMeta(scissormeta);

        ItemStack paper = new ItemStack(Material.PAPER,1,(short)0);
        ItemMeta papermeta = paper.getItemMeta();
        papermeta.setDisplayName("§lパー§r");
        List<String> paperlore = new ArrayList<>();
        paperlore.add("§r§lパーを選択します。 使う指: 5本");
        papermeta.setLore(paperlore);
        paper.setItemMeta(papermeta);

        ItemStack p1Skull = new ItemStack(Material.SKULL_ITEM,1, (short) 3);
        SkullMeta p1skullmeta = (SkullMeta) p1Skull.getItemMeta();
        List<String> p1skulllore = new ArrayList<>();
        p1skulllore.add("§r§6Score§f: §d"+plugin.p1score);
        p1skulllore.add("§r§6残りの指の数: §d"+plugin.p1finger);
        p1skullmeta.setLore(p1skulllore);
        p1skullmeta.setDisplayName("§l§3"+p1.getName());
        OfflinePlayer p1offline = Bukkit.getOfflinePlayer(p1.getUniqueId());
        p1skullmeta.setOwningPlayer(p1offline);
        p1Skull.setItemMeta(p1skullmeta);

        ItemStack p2Skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta p2skullmeta = (SkullMeta) p2Skull.getItemMeta();
        List<String> p2skulllore = new ArrayList<>();
        p2skulllore.add("§6Score§f: §d"+plugin.p2score);
        p2skulllore.add("§r§6残りの指の数§f: §d"+plugin.p2finger);
        p2skullmeta.setLore(p2skulllore);
        p2skullmeta.setDisplayName("§l§c"+p2.getName());
        OfflinePlayer p2offline = Bukkit.getOfflinePlayer(p2.getUniqueId());
        p2skullmeta.setOwningPlayer(p2offline);
        p2Skull.setItemMeta(p2skullmeta);

        ItemStack roundwatch = new ItemStack(Material.WATCH,1);
        ItemMeta roundwatchmeta = roundwatch.getItemMeta();
        List<String> roundwatchlore = new ArrayList<>();
        roundwatchlore.add("§6現在のラウンド§f: §d"+plugin.round);
        roundwatchmeta.setLore(roundwatchlore);
        roundwatchmeta.setDisplayName("§cラウンドウォッチ");
        roundwatch.setItemMeta(roundwatchmeta);

        plugin.p1inv.setItem(11, rock);
        plugin.p1inv.setItem(13, scissor);
        plugin.p1inv.setItem(15, paper);
        plugin.p1inv.setItem(0, p1Skull);
        plugin.p1inv.setItem(8, p2Skull);
        plugin.p1inv.setItem(4, roundwatch);

        plugin.p2inv.setItem(11, rock);
        plugin.p2inv.setItem(13, scissor);
        plugin.p2inv.setItem(15, paper);
        plugin.p2inv.setItem(0, p2Skull);
        plugin.p2inv.setItem(8, p1Skull);
        plugin.p2inv.setItem(4, roundwatch);
        p1.openInventory(plugin.p1inv);
        p2.openInventory(plugin.p2inv);
    }
    void emergencystop() {
        p1.closeInventory();
        p2.closeInventory();
        vault.deposit(p1.getUniqueId(), plugin.betmoney);
        vault.deposit(p2.getUniqueId(), plugin.betmoney);
        p1.sendMessage(plugin.prefix + "§c§l試合がキャンセルされました。賭け金は返金されます");
        p2.sendMessage(plugin.prefix + "§c§l試合がキャンセルされました。賭け金は返金されます");
        plugin.reset();
    }
    void winp1() {
        Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p2.getName()+"§fが抜けたため、§e"+p1.getName()+"§fの勝ちとなりました");
        vault.deposit(p1.getUniqueId(), plugin.betmoney * 2 - plugin.betmoney * plugin.bonuscompetitive);
        plugin.specialbonus += plugin.betmoney * plugin.bonuscompetitive;
        config.reload();
    }
    void winp2() {
        Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p1.getName()+"§fが抜けたため、§e"+p2.getName()+"§fの勝ちとなりました");
        vault.deposit(p2.getUniqueId(), plugin.betmoney * 2 - plugin.betmoney * plugin.bonuscompetitive);
        plugin.specialbonus += plugin.betmoney * plugin.bonuscompetitive;
        config.reload();
    }
    void judge() {
        int result = rpsjudge();
        if(result == 0) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
            new BukkitRunnable() {
                public void run() {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§lぽん！");
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第"+plugin.round+"ラウンドは§eあいこ§fでした");
                }
            }.runTaskLater(plugin, 40);
            new BukkitRunnable() {
                public void run() {
                    if(plugin.round == 10) {
                        lastjudge();
                        return;
                    }
                    plugin.round++;
                    restart();
                }
            }.runTaskLater(plugin,41);
        }
        if(result == 1) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
            new BukkitRunnable() {
                public void run() {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§lぽん！");
                    switch (plugin.round) {
                        case 6:
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第" + plugin.round + "ラウンドは§e" + p1.getName() + "§fの勝利！");
                            plugin.p1score += 2;
                            plugin.round++;
                            break;
                        case 10:
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第" + plugin.round + "ラウンドは§e" + p1.getName() + "§fの勝利！");
                            plugin.p1score += 2;
                            lastjudge();
                            return;
                        default:
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第" + plugin.round + "ラウンドは§e" + p1.getName() + "§fの勝利！");
                            plugin.p1score++;
                            plugin.round++;
                            break;
                    }
                    restart();
                }
            }.runTaskLater(plugin,40);
        }
        if(result == 2) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
            new BukkitRunnable() {
                public void run() {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§lぽん！");
                    switch (plugin.round) {
                        case 6:
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第" + plugin.round + "ラウンドは§e" + p2.getName() + "§fの勝利！");
                            plugin.p2score += 2;
                            plugin.round++;
                            break;
                        case 10:
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第" + plugin.round + "ラウンドは§e" + p2.getName() + "§fの勝利！");
                            plugin.p2score += 2;
                            lastjudge();
                            return;
                        default:
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l第" + plugin.round + "ラウンドは§e" + p2.getName() + "§fの勝利！");
                            plugin.p2score++;
                            plugin.round++;
                            break;
                    }
                    restart();
                }
            }.runTaskLater(plugin, 40);
        }
    }
    void restart() {
        plugin.p1canchooserps = true;
        plugin.p2canchooserps = true;
        ItemStack p1Skull = new ItemStack(Material.SKULL_ITEM,1, (short) 3);
        SkullMeta p1skullmeta = (SkullMeta) p1Skull.getItemMeta();
        List<String> p1skulllore = new ArrayList<>();
        p1skulllore.add("§r§6Score§f: §d"+plugin.p1score);
        p1skulllore.add("§r§6残りの指の数: §d"+plugin.p1finger);
        p1skullmeta.setLore(p1skulllore);
        p1skullmeta.setDisplayName("§l§3"+p1.getName());
        OfflinePlayer p1offline = Bukkit.getOfflinePlayer(p1.getUniqueId());
        p1skullmeta.setOwningPlayer(p1offline);
        p1Skull.setItemMeta(p1skullmeta);

        ItemStack p2Skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta p2skullmeta = (SkullMeta) p2Skull.getItemMeta();
        List<String> p2skulllore = new ArrayList<>();
        p2skulllore.add("§6Score§f: §d"+plugin.p2score);
        p2skulllore.add("§r§6残りの指の数§f: §d"+plugin.p2finger);
        p2skullmeta.setLore(p2skulllore);
        p2skullmeta.setDisplayName("§l§c"+p2.getName());
        OfflinePlayer p2offline = Bukkit.getOfflinePlayer(p2.getUniqueId());
        p2skullmeta.setOwningPlayer(p2offline);
        p2Skull.setItemMeta(p2skullmeta);

        ItemStack roundwatch = new ItemStack(Material.WATCH,1);
        ItemMeta roundwatchmeta = roundwatch.getItemMeta();
        List<String> roundwatchlore = new ArrayList<>();
        roundwatchlore.add("§6現在のラウンド§f: §d"+plugin.round);
        roundwatchmeta.setLore(roundwatchlore);
        roundwatchmeta.setDisplayName("§cラウンドウォッチ");
        roundwatch.setItemMeta(roundwatchmeta);
        plugin.p1inv.setItem(0, p1Skull);
        plugin.p1inv.setItem(8, p2Skull);
        plugin.p1inv.setItem(4, roundwatch);
        plugin.p2inv.setItem(0, p2Skull);
        plugin.p2inv.setItem(8, p1Skull);
        plugin.p2inv.setItem(4, roundwatch);
        p1.openInventory(plugin.p1inv);
        p2.openInventory(plugin.p2inv);
        p1.updateInventory();
        p2.updateInventory();
    }
    void lastjudge() {
        plugin.p1score -= plugin.p1finger;
        plugin.p2score -= plugin.p2finger;
        Bukkit.getServer().broadcastMessage(plugin.prefix + "§f試合終了! 結果は...§kaaa");
        new BukkitRunnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p1.getName()+"§fのスコア: "+plugin.p1score);
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p2.getName()+"§fのスコア: "+plugin.p2score);
                if(plugin.p1score == plugin.p2score) {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§f§l引き分けでした！試合を終了します");
                    vault.deposit(p1.getUniqueId(), plugin.betmoney);
                    vault.deposit(p2.getUniqueId(), plugin.betmoney);
                    plugin.fevertime = false;
                    return;
                }
                if(plugin.p1score > plugin.p2score) {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p1.getName()+"§fの勝利です！試合を終了します");
                    if(plugin.fevertime) {
                        List<Player> serverplayer = new LinkedList<>();
                        for(Player player : Bukkit.getOnlinePlayers()){
                            serverplayer.add(player);
                        }
                        Random r = new Random();
                        Player bonusplayer = serverplayer.get(r.nextInt(serverplayer.size()));
                        while(bonusplayer == p1) {
                            bonusplayer = serverplayer.get(r.nextInt(serverplayer.size()));
                        }
                        Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p1.getName()+"§fと§e"+bonusplayer.getName()+"§fは追加ボーナスを受け取った！");
                        vault.deposit(p1.getUniqueId(), plugin.betmoney *= 2);
                        vault.deposit(p1.getUniqueId(),plugin.specialbonus / 2);
                        vault.deposit(serverplayer.get(r.nextInt(serverplayer.size())).getUniqueId(), plugin.specialbonus / 2);
                        plugin.specialbonus = 0;
                        plugin.fevertime = false;
                        plugin.reset();
                        return;
                    }
                    plugin.specialbonus += plugin.betmoney * plugin.bonuscompetitive;
                    vault.deposit(p1.getUniqueId(), plugin.betmoney * 2 - plugin.betmoney * plugin.bonuscompetitive);
                    plugin.reset();
                } else {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p2.getName()+"§fの勝利です！試合を終了します");
                    if(plugin.fevertime) {
                        List<Player> serverplayer = new LinkedList<>();
                        for(Player player : Bukkit.getOnlinePlayers()){
                            serverplayer.add(player);
                        }
                        Random r = new Random();
                        Player bonusplayer = serverplayer.get(r.nextInt(serverplayer.size()));
                        while(bonusplayer == p1) {
                            bonusplayer = serverplayer.get(r.nextInt(serverplayer.size()));
                        }
                        Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p2.getName()+"§fと§e"+bonusplayer.getName()+"§fは追加ボーナスを受け取った！");
                        vault.deposit(p2.getUniqueId(), plugin.betmoney *= 2);
                        vault.deposit(p2.getUniqueId(),plugin.specialbonus / 2);
                        vault.deposit(serverplayer.get(r.nextInt(serverplayer.size())).getUniqueId(), plugin.specialbonus / 2);
                        plugin.specialbonus = 0;
                        plugin.fevertime = false;
                        plugin.reset();
                        return;
                    }
                    plugin.specialbonus += plugin.betmoney * plugin.bonuscompetitive;
                    config.reload();
                    vault.deposit(p2.getUniqueId(), plugin.betmoney * 2 - plugin.betmoney * plugin.bonuscompetitive);
                    plugin.reset();
                }
            }
        }.runTaskLater(plugin, 60);
    }
    int rpsjudge() {
        ////////////////////////////
        ///あいこ0、p1勝利1、p2勝利2//
        ///////////////////////////
        if(plugin.p1putoutfinger == plugin.p2putoutfinger) {
            return 0;
        }
        if(plugin.p1putoutfinger == plugin.rockfinger && plugin.p2putoutfinger == plugin.scissorfinger) {
            return 1;
        }
        if(plugin.p1putoutfinger == plugin.rockfinger && plugin.p2putoutfinger == plugin.paperfinger) {
            return 2;
        }
        if(plugin.p1putoutfinger == plugin.scissorfinger && plugin.p2putoutfinger == plugin.rockfinger) {
            return 2;
        }
        if(plugin.p1putoutfinger == plugin.scissorfinger && plugin.p2putoutfinger == plugin.paperfinger) {
            return 1;
        }
        if(plugin.p1putoutfinger == plugin.paperfinger && plugin.p2putoutfinger == plugin.rockfinger) {
            return 1;
        }
        if(plugin.p1putoutfinger == plugin.paperfinger && plugin.p2putoutfinger == plugin.scissorfinger) {
            return 2;
        }
        return -1;
    }

}

