package com.github.mamizu0312.man10eighteen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EighteenVoteManager {
    Man10Eighteen plugin;
    Inventory voteinv;
    public EighteenVoteManager(Man10Eighteen plugin) {
        this.plugin = plugin;
        voteinv = Bukkit.createInventory(null, 9,plugin.prefix + "§r§e勝者を予想しよう！");
        ItemStack p1buttom = new ItemStack(Material.SKULL_ITEM,1,(short)3);
        ItemMeta p1buttommeta = p1buttom.getItemMeta();
        p1buttommeta.setDisplayName("§3§l"+plugin.onGame.get(0));
        List<String> p1buttomlore = new ArrayList<>();
        p1buttomlore.add("§e§lこのプレイヤーが勝利すると予想！");
        p1buttommeta.setLore(p1buttomlore);
        p1buttom.setItemMeta(p1buttommeta);

        ItemStack p2buttom = new ItemStack(Material.SKULL_ITEM,1,(short)3);
        ItemMeta p2buttommeta = p2buttom.getItemMeta();
        p2buttommeta.setDisplayName("§c§l"+plugin.onGame.get(1));
        List<String> p2buttomlore = new ArrayList<>();
        p2buttomlore.add("§e§lこのプレイヤーが勝利すると予想！");
        p2buttommeta.setLore(p2buttomlore);
        p2buttom.setItemMeta(p2buttommeta);
        voteinv.setItem(2,p1buttom);
        voteinv.setItem(6,p2buttom);
    }
    public void openvoteinv(Player p) {
        p.openInventory(voteinv);
    }
}
