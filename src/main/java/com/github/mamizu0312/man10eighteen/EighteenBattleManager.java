package com.github.mamizu0312.man10eighteen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EighteenBattleManager {
    static int p1Finger = 18;
    static int p2Finger = 18;
    static int p1putoutFinger;
    static int p2putoutFinger;

    int p1Score;
    int p2Score;
    int p1LastScore;
    int p2LastScore;
    int round = 1;
    static int background;
    public void onGame(Player p1, Player p2) {
        Inventory p1inv = Bukkit.createInventory(null, 27, p1.getName()+" VS "+p2.getName());
        ItemStack item = new ItemStack(Material.STONE, 1 ,(short)1);
        ItemMeta itemm = item.getItemMeta();
        itemm.setDisplayName("グー");
        List<String> lore = new ArrayList<>();
        lore.add("グーを選択します。使う指:0");
        itemm.setLore(lore);
        item.setItemMeta(itemm);

        ItemStack item2 = new ItemStack(Material.SHEARS, 1, (short)1);
        ItemMeta itemm2 = item2.getItemMeta();
        itemm2.setDisplayName("チョキ");
        List<String> lore2 = new ArrayList<>();
        lore2.add("チョキを選択します。使う指:2");
        itemm2.setLore(lore2);
        item2.setItemMeta(itemm2);

        ItemStack item3 = new ItemStack(Material.PAPER, 1, (short)1);
        ItemMeta itemm3 = item3.getItemMeta();
        itemm3.setDisplayName("パー");
        List<String> lore3 = new ArrayList<>();
        lore3.add("パーを選択します。使う指:5");
        itemm3.setLore(lore3);
        item3.setItemMeta(itemm3);

        ItemStack p1Skull = new ItemStack(Material.SKULL, 1);
        SkullMeta p1SkullMeta = (SkullMeta) p1Skull.getItemMeta();
        p1SkullMeta.setDisplayName(p1.getName());
        OfflinePlayer p1offline = Bukkit.getOfflinePlayer(p1.getUniqueId());
        p1SkullMeta.setOwningPlayer(p1offline);
        List<String> p1SkullLore = new ArrayList<>();
        p1SkullLore.add("Score: "+ p1Score);
        p1SkullMeta.setLore(p1SkullLore);
        p1Skull.setItemMeta(p1SkullMeta);

        ItemStack p2Skull = new ItemStack(Material.SKULL, 1);
        SkullMeta p2SkullMeta = (SkullMeta) p2Skull.getItemMeta();
        p2SkullMeta.setDisplayName(p2.getName());
        OfflinePlayer p2offline = Bukkit.getOfflinePlayer(p2.getUniqueId());
        p2SkullMeta.setOwningPlayer(p2offline);
        List<String> p2SkullLore = new ArrayList<>();
        p2SkullLore.add("Score: "+ p2Score);
        p2SkullMeta.setLore(p2SkullLore);

        ItemStack item4 = new ItemStack(Material.WATCH, 1, (short)1);
        ItemMeta itemm4 = item4.getItemMeta();
        itemm4.setDisplayName(round+ "回戦");
        item4.setItemMeta(itemm4);
        Inventory p2inv = Bukkit.createInventory(null,27,p2.getName()+" VS "+p1.getName());
        p1inv.setItem(3, item);
        p1inv.setItem(12, item2);
        p1inv.setItem(21, item3);
        p1inv.setItem(0, p1Skull);
        p1inv.setItem(8, p2Skull);
        p1inv.setItem(26, item4);

        p2inv.setItem(3, item);
        p2inv.setItem(12, item2);
        p2inv.setItem(21, item3);
        p2inv.setItem(0, p2Skull);
        p2inv.setItem(8, p1Skull);
        p2inv.setItem(26, item4);
        p1.openInventory(p1inv);
        p2.openInventory(p2inv);

    }
}
