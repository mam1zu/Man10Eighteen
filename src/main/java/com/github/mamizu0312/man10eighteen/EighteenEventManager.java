package com.github.mamizu0312.man10eighteen;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

public class EighteenEventManager implements Listener {
    Man10Eighteen plugin;
    public EighteenEventManager(Man10Eighteen plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();

    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        Player p = (Player)e.getPlayer();
        if(EighteenStatus.onMenu.contains(p.getUniqueId())) {
            EighteenStatus.onMenu.remove(p.getUniqueId());
        }
        if(EighteenStatus.onGame.contains(p.getUniqueId())) {
            EighteenStatus.onGame.remove(p.getUniqueId());
            //TODO:閉じたやつが負け！なコードを書く
        }
    }
}
