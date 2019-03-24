package com.github.mamizu0312.man10eighteen;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;


public class EighteenEventManager implements Listener {
    Man10Eighteen plugin;
    EighteenBattleManager battle;
    public EighteenEventManager(Man10Eighteen plugin) {
        this.plugin = plugin;
        battle = new EighteenBattleManager(plugin);
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
                  if (e.getCurrentItem().getType() == Material.STONE) {
                      plugin.p1putoutfinger = plugin.rockfinger;
                      ItemMeta rockmeta = e.getCurrentItem().getItemMeta();
                      rockmeta.addEnchant(Enchantment.FROST_WALKER, 1, false);
                      rockmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                      p.updateInventory();
                      plugin.p1canchooserps = false;
                  }
                  if (e.getCurrentItem().getType() == Material.SHEARS) {
                      if(plugin.p1finger < plugin.scissorfinger) {
                          p.sendMessage(plugin.prefix + "§c指の本数が足りないため、チョキをだすことはできません。");
                          return;
                      }
                      plugin.p1putoutfinger = plugin.scissorfinger;
                      ItemMeta scissormeta = e.getCurrentItem().getItemMeta();
                      scissormeta.addEnchant(Enchantment.FROST_WALKER,1,false);
                      scissormeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                      plugin.p1finger -= 2;
                      p.updateInventory();
                      plugin.p1canchooserps = false;
                      }
                  }
                  if (e.getCurrentItem().getType() == Material.PAPER) {
                      if(plugin.p1finger < plugin.paperfinger) {
                          p.sendMessage(plugin.prefix + "§c指の本数が足りないため、パーをだすことはできません。");
                      }
                      plugin.p1putoutfinger = plugin.paperfinger;
                      ItemMeta papermeta = e.getCurrentItem().getItemMeta();
                      papermeta.addEnchant(Enchantment.FROST_WALKER,1,false);
                      papermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                      plugin.p1finger -= 5;
                      p.updateInventory();
                      plugin.p1canchooserps = false;
                      }
                  if(!plugin.p1canchooserps && !plugin.p2canchooserps) {
                      //TODO:両方とも出し終わった判定なのでジャッジして次のラウンドへ
                      battle.judge();
                  }
                  return;
              }
            if(plugin.onGame.get(1) == p.getUniqueId()) {
                if(plugin.p2canchooserps) {
                    if(e.getCurrentItem().getType() == Material.STONE) {
                        plugin.p2putoutfinger = plugin.rockfinger;
                        ItemMeta rockmeta = e.getCurrentItem().getItemMeta();
                        rockmeta.addEnchant(Enchantment.FROST_WALKER,1,false);
                        rockmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        p.updateInventory();
                        plugin.p2canchooserps = false;
                    }
                    if(e.getCurrentItem().getType() == Material.SHEARS) {
                        if(plugin.p2finger < plugin.scissorfinger) {
                            p.sendMessage(plugin.prefix + "§c指の本数が足りないため、チョキをだすことはできません。");
                            return;
                        }
                        plugin.p2putoutfinger = plugin.scissorfinger;
                        ItemMeta scissormeta = e.getCurrentItem().getItemMeta();
                        scissormeta.addEnchant(Enchantment.FROST_WALKER,1,false);
                        scissormeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        plugin.p2finger -= 2;
                        p.updateInventory();
                        plugin.p2canchooserps = false;
                    }
                    if(e.getCurrentItem().getType() == Material.PAPER) {
                        if(plugin.p2finger < plugin.paperfinger) {
                            p.sendMessage(plugin.prefix + "§c指の本数が足りないため、パーをだすことはできません。");
                        }
                        plugin.p2putoutfinger = plugin.paperfinger;
                        ItemMeta papermeta = e.getCurrentItem().getItemMeta();
                        papermeta.addEnchant(Enchantment.FROST_WALKER,1,false);
                        papermeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        plugin.p2finger -= 5;
                        p.updateInventory();
                        plugin.p2canchooserps = false;
                    }
                    if(!plugin.p1canchooserps && !plugin.p2canchooserps) {
                        //TODO: 両方とも出し終わった判定なのでジャッジして次のラウンドへ
                        battle.judge();
                    }
                }
            }
        }
    }
}
