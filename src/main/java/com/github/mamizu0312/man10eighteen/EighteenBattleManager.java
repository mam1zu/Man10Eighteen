package com.github.mamizu0312.man10eighteen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class EighteenBattleManager {
    Man10Eighteen plugin;
    Player p1;
    Player p2;
    public EighteenBattleManager(Man10Eighteen plugin) {
        this.plugin = plugin;
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
        return;
    }
    public boolean isPlayerP1(Player p) {
        return p1 == p;
    }
    public boolean isPlayerP2(Player p) {
        return p2 == p;
    }
}

