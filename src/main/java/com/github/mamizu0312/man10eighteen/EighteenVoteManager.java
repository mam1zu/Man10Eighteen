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

public class EighteenVoteManager {
    Man10Eighteen plugin;
    Inventory voteinv;
    public EighteenVoteManager(Man10Eighteen plugin) {
        this.plugin = plugin;
    }
    public void openvoteinv(Player p) {
        voteinv = Bukkit.createInventory(null, 9,plugin.prefix + "§r§e勝者を予想しよう！");
        ItemStack p1buttom = new ItemStack(Material.SKULL_ITEM,1,(short)3);
        SkullMeta p1buttommeta = (SkullMeta) p1buttom.getItemMeta();
        p1buttommeta.setDisplayName("§3§l"+Bukkit.getPlayer(plugin.onGame.get(0)).getName());
        List<String> p1buttomlore = new ArrayList<>();
        p1buttomlore.add("§e§lこのプレイヤーが勝利すると予想！");
        OfflinePlayer p1offline = Bukkit.getOfflinePlayer(plugin.onGame.get(0));
        p1buttommeta.setOwner(p1offline.getName());
        p1buttommeta.setLore(p1buttomlore);
        p1buttom.setItemMeta(p1buttommeta);

        ItemStack p2buttom = new ItemStack(Material.SKULL_ITEM,1,(short)3);
        SkullMeta p2buttommeta = (SkullMeta) p2buttom.getItemMeta();
        p2buttommeta.setDisplayName("§c§l"+Bukkit.getPlayer(plugin.onGame.get(1)).getName());
        List<String> p2buttomlore = new ArrayList<>();
        p2buttomlore.add("§e§lこのプレイヤーが勝利すると予想！");
        OfflinePlayer p2offline = Bukkit.getOfflinePlayer(plugin.onGame.get(1));
        p2buttommeta.setOwner(p2offline.getName());
        p2buttommeta.setLore(p2buttomlore);
        p2buttom.setItemMeta(p2buttommeta);
        voteinv.setItem(2,p1buttom);
        voteinv.setItem(6,p2buttom);
        p.openInventory(voteinv);
    }
}
