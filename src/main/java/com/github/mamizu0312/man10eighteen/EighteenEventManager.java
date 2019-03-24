package com.github.mamizu0312.man10eighteen;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EighteenEventManager implements Listener {
    Man10Eighteen plugin;
    EighteenBattleManager battle;
    public EighteenEventManager(Man10Eighteen plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if(p == null) {
            return;
        }
        if(plugin.onGame.contains(p.getUniqueId())) {
            e.setCancelled(true);
            if(plugin.onGame.get(0) == p.getUniqueId()) {
                if(plugin.p1canchooserps){
                    if(e.getCurrentItem() == null) {
                        return;
                    }
                  if (e.getCurrentItem().getType() == Material.STONE) {
                      plugin.p1putoutfinger = plugin.rockfinger;
                      plugin.p1canchooserps = false;
                      p.closeInventory();
                  }
                  if (e.getCurrentItem().getType() == Material.SHEARS) {
                      if(plugin.p1finger < plugin.scissorfinger) {
                          p.sendMessage(plugin.prefix + "§c指の本数が足りないため、チョキをだすことはできません。");
                          return;
                      }
                      plugin.p1putoutfinger = plugin.scissorfinger;
                      plugin.p1finger -= 2;
                      p.closeInventory();
                      plugin.p1canchooserps = false;
                      }
                    if (e.getCurrentItem().getType() == Material.PAPER) {
                        if(plugin.p1finger < plugin.paperfinger) {
                            p.sendMessage(plugin.prefix + "§c指の本数が足りないため、パーをだすことはできません。");
                            return;
                        }
                        plugin.p1putoutfinger = plugin.paperfinger;
                        plugin.p1finger -= 5;
                        p.closeInventory();
                        plugin.p1canchooserps = false;
                    }
                    if(!plugin.p1canchooserps && !plugin.p2canchooserps) {
                        battle.judge();
                    }
                  }
              }
            if(plugin.onGame.get(1) == p.getUniqueId()) {
                if(plugin.p2canchooserps) {
                    if(e.getCurrentItem() == null) {
                        return;
                    }
                    if(e.getCurrentItem().getType() == Material.STONE) {
                        plugin.p2putoutfinger = plugin.rockfinger;
                        p.closeInventory();
                        plugin.p2canchooserps = false;
                    }
                    if(e.getCurrentItem().getType() == Material.SHEARS) {
                        if(plugin.p2finger < plugin.scissorfinger) {
                            p.sendMessage(plugin.prefix + "§c指の本数が足りないため、チョキをだすことはできません。");
                            return;
                        }
                        plugin.p2putoutfinger = plugin.scissorfinger;
                        plugin.p2finger -= 2;
                        p.closeInventory();
                        plugin.p2canchooserps = false;
                    }
                    if(e.getCurrentItem().getType() == Material.PAPER) {
                        if(plugin.p2finger < plugin.paperfinger) {
                            p.sendMessage(plugin.prefix + "§c指の本数が足りないため、パーをだすことはできません。");
                            return;
                        }
                        plugin.p2putoutfinger = plugin.paperfinger;
                        plugin.p2finger -= 5;
                        p.closeInventory();
                        plugin.p2canchooserps = false;
                    }
                    if(!plugin.p1canchooserps && !plugin.p2canchooserps) {
                        battle.judge();
                    }
                }
            }
        }
    }
}
