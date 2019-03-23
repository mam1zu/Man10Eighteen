package com.github.mamizu0312.man10eighteen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class EighteenBattleManager {
    Man10Eighteen plugin;
    VaultManager vault;
    Player p1;
    Player p2;
    public EighteenBattleManager(Man10Eighteen plugin) {
        this.plugin = plugin;
        this.vault = new VaultManager(plugin);
    }
    public EighteenBattleManager(Man10Eighteen plugin, Player p1, Player p2) {
        this.plugin = plugin;
        this.p1 = p1;
        this.p2 = p2;
    }
    public void game() {
        plugin.p1inv = Bukkit.createInventory(null,27,plugin.prefix);
        plugin.p2inv = Bukkit.createInventory(null,27,plugin.prefix);

        ItemStack rock = new ItemStack(Material.STONE,1,(short)0);
        ItemMeta rockmeta = rock.getItemMeta();
        rockmeta.setDisplayName("§l§7グー§r");
        List<String> rocklore = new ArrayList<>();
        rocklore.add("グーを選択します。使う指: 0本");
        rockmeta.setLore(rocklore);
        rock.setItemMeta(rockmeta);

        ItemStack scissor = new ItemStack(Material.SHEARS,1,(short)0);
        ItemMeta scissormeta = scissor.getItemMeta();
        scissormeta.setDisplayName("§l§cチョキ§r");
        List<String> scissorlore = new ArrayList<>();
        scissorlore.add("チョキを選択します。使う指: 2本");
        scissormeta.setLore(scissorlore);
        scissor.setItemMeta(scissormeta);

        ItemStack paper = new ItemStack(Material.PAPER,1,(short)0);
        ItemMeta papermeta = paper.getItemMeta();
        papermeta.setDisplayName("§lパー§r");
        List<String> paperlore = new ArrayList<>();
        paperlore.add("パーを選択します。 使う指: 5本");
        papermeta.setLore(paperlore);
        paper.setItemMeta(papermeta);

        ItemStack p1Skull = new ItemStack(Material.SKULL,1);
        SkullMeta p1SkullMeta = (SkullMeta) p1Skull.getItemMeta();
        p1SkullMeta.setDisplayName("§l§3"+p1.getName());
        OfflinePlayer p1offline = Bukkit.getOfflinePlayer(p1.getUniqueId());
        p1SkullMeta.setOwningPlayer(p1offline);
        p1Skull.setItemMeta(p1SkullMeta);

        ItemStack p2Skull = new ItemStack(Material.SKULL, 1);
        SkullMeta p2SkullMeta = (SkullMeta) p2Skull.getItemMeta();
        p2SkullMeta.setDisplayName("§l§c"+p2.getName());
        OfflinePlayer p2offline = Bukkit.getOfflinePlayer(p2.getUniqueId());
        p2SkullMeta.setOwningPlayer(p2offline);
        p2Skull.setItemMeta(p2SkullMeta);

        plugin.p1inv.setItem(3, rock);
        plugin.p1inv.setItem(12, scissor);
        plugin.p1inv.setItem(21, paper);
        plugin.p1inv.setItem(0, p1Skull);
        plugin.p1inv.setItem(26, p2Skull);

        plugin.p2inv.setItem(3, rock);
        plugin.p2inv.setItem(12, scissor);
        plugin.p2inv.setItem(21, paper);
        plugin.p2inv.setItem(0, p2Skull);
        plugin.p2inv.setItem(26, p1Skull);

        p1.openInventory(plugin.p1inv);
        p2.openInventory(plugin.p2inv);
    }
    public void endgame() {
        p1.closeInventory();
        p2.closeInventory();
        plugin.p1score -= plugin.p1finger;
        plugin.p2score -= plugin.p2finger;
        if(plugin.p1score == plugin.p2score) {
            getServer().broadcastMessage(plugin.prefix + "§e§l§k§r§e結果発表...§f§ka");
            new BukkitRunnable() {
                public void run() {
                    getServer().broadcastMessage(plugin.prefix + "§e§l"+p1.getName()+"§fと§e"+p2.getName()+"§fは試合をしましたが、あいこでした");
                }
            }.runTaskLater(plugin, 60);
        }
        if(plugin.p1score > plugin.p2score) {
            getServer().broadcastMessage(plugin.prefix + "§e§l§k§r§e結果発表...§f§ka");
            new BukkitRunnable() {
                public void run() {
                    getServer().broadcastMessage(plugin.prefix + "§e§l"+p1.getName()+"§fの勝利！");
                }
            }.runTaskLater(plugin, 60);
        } else {
            getServer().broadcastMessage(plugin.prefix + "§e§l§k§r§e結果発表...§f§ka");
            new BukkitRunnable() {
                public void run() {
                    getServer().broadcastMessage(plugin.prefix + "§e§l"+p2.getName()+"§fの勝利！");
                }
            }.runTaskLater(plugin, 60);
        }
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
    void judge() {
        int result = rpsjudge();
        if(result == 0) {
            getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
            new BukkitRunnable() {
                public void run() {
                    getServer().broadcastMessage(plugin.prefix + "§e§ぽん！");
                }
            }.runTaskLater(plugin,40);
            new BukkitRunnable() {
                public void run() {
                    getServer().broadcastMessage(plugin.prefix + "§f§l第"+plugin.round+"ラウンドは§eあいこ§fでした");
                }
            }.runTaskLater(plugin,40);
            plugin.round++;
        }
        if(result == 1) {
            switch(plugin.round) {
                case 6:
                    getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§e§ぽん！");
                        }
                    }.runTaskLater(plugin,40);
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§f§l第"+plugin.round+"ラウンドは§e"+p1.getName()+"§fの勝利！");
                        }
                    }.runTaskLater(plugin,40);
                    plugin.p1score += 2;
                    plugin.round++;
                    break;
                case 10:
                    getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§e§ぽん！");
                        }
                    }.runTaskLater(plugin,40);
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§f§l第"+plugin.round+"ラウンドは§e"+p1.getName()+"§fの勝利！");
                        }
                    }.runTaskLater(plugin,40);
                    plugin.p1score += 2;
                    //TODO:ゲーム終了、スコアを計算して勝敗判定させる
                    endgame();
                    return;
                default:
                    getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§e§ぽん！");
                        }
                    }.runTaskLater(plugin,40);
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§f§l第"+plugin.round+"ラウンドは§e"+p1.getName()+"§fの勝利！");
                        }
                    }.runTaskLater(plugin,40);
                    plugin.p1score++;
                    plugin.round++;
                    break;
            }
        }
        if(result == 2) {
            switch(plugin.round) {
                case 6:
                    getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§e§ぽん！");
                        }
                    }.runTaskLater(plugin,40);
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§f§l第"+plugin.round+"ラウンドは§e"+p2.getName()+"§fの勝利！");
                        }
                    }.runTaskLater(plugin,40);
                    plugin.p2score += 2;
                    plugin.round++;
                    break;
                case 10:
                    getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§e§ぽん！");
                        }
                    }.runTaskLater(plugin,40);
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§f§l第"+plugin.round+"ラウンドは§e"+p2.getName()+"§fの勝利！");
                        }
                    }.runTaskLater(plugin,40);
                    plugin.p2score += 2;
                    //TODO:ゲーム終了、スコアを計算して勝敗判定させる
                    return;
                default:
                    getServer().broadcastMessage(plugin.prefix + "§f§lじゃんけん...§k");
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§e§ぽん！");
                        }
                    }.runTaskLater(plugin,40);
                    new BukkitRunnable() {
                        public void run() {
                            getServer().broadcastMessage(plugin.prefix + "§f§l第"+plugin.round+"ラウンドは§e"+p2.getName()+"§fの勝利！");
                        }
                    }.runTaskLater(plugin,40);
                    plugin.p1score++;
                    plugin.round++;
                    break;
            }
        }
        p1.updateInventory();
        p2.updateInventory();
    }
    void restart() {
        p1.openInventory(plugin.p1inv);
        p2.openInventory(plugin.p2inv);
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

