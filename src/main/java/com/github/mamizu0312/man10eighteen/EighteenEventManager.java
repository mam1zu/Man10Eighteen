package com.github.mamizu0312.man10eighteen;

import com.github.mamizu0312.man10eighteen.betselecter.valueSelectedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EighteenEventManager implements Listener {
    Man10Eighteen plugin;
    EighteenBattleManager battle;
    public EighteenEventManager(Man10Eighteen plugin) {
        this.plugin = plugin;
        battle = plugin.battle;
    }
    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(p == null) {
            return;
        }
        if(plugin.prewait) {
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§e§l"+p.getName()+"§r§lさんがログアウトしたため、試合を中止しました");
            plugin.reset();
            return;
        }
        if(plugin.votep1.contains(p.getUniqueId())) {
            plugin.votep1.remove(p.getUniqueId());
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§l"+p.getName()+"はログアウトしたため、抽選対象から除外されました");
            return;
        }
        if(plugin.votep2.contains(p.getUniqueId())) {
            plugin.votep2.remove(p.getUniqueId());
            Bukkit.getServer().broadcastMessage(plugin.prefix + "§l"+p.getName()+"はログアウトしたため、抽選対象から除外されました");
            return;
        }
        if(plugin.onGame.contains(p.getUniqueId())) {
            if(plugin.onGame.get(0) == p.getUniqueId()) {
                plugin.p1status = false;
                battle.winp2();
                plugin.reset();
                return;
            }
            if(plugin.onGame.get(1) == p.getUniqueId()) {
                plugin.p2status = false;
                battle.winp1();
                plugin.reset();
            }
        }
    }
    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        if(plugin.onGame.isEmpty()) {
            return;
        }
        Player p = (Player)e.getPlayer();
        if(p == null) {
            return;
        }
        if(plugin.onVoting.contains(p.getUniqueId())) {
            plugin.onVoting.remove(p.getUniqueId());
            return;
        }
        if(plugin.onGame.contains(p.getUniqueId())) {
            if(plugin.prewait) {
                return;
            }
            if(plugin.onGame.get(0) == p.getUniqueId() && plugin.p1canchooserps) {
                p.sendMessage(plugin.prefix + "§f§l誤って閉じてしまった場合は、§e§l/mer reopen§f§lで再度開くことができます");
                    return;
            }
            if (plugin.onGame.get(1) == p.getUniqueId() && plugin.p2canchooserps) {
                p.sendMessage(plugin.prefix + "§f§l誤って閉じてしまった場合は、§e§l/mer reopen§f§lで再度開くことができます");
            }
        }
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if(p == null) {
            return;
        }
        if(plugin.onVoting.contains(p.getUniqueId())) {
            if(plugin.votep1.contains(p.getUniqueId()) || plugin.votep2.contains(p.getUniqueId())) {
                return;
            }
            e.setCancelled(true);
            if(e.getSlot() == 2) {
                plugin.votep1.add(p.getUniqueId());
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§l"+p.getName()+"は§3§l"+Bukkit.getPlayer(plugin.onGame.get(0)).getName()+"§r§lが勝つと予想しました！");
                p.closeInventory();
                return;
            }
            if(e.getSlot() == 6) {
                plugin.votep2.add(p.getUniqueId());
                Bukkit.getServer().broadcastMessage(plugin.prefix + "§l"+p.getName()+"は§c§l"+Bukkit.getPlayer(plugin.onGame.get(1)).getName()+"§r§lが勝つと予想しました！");
                p.closeInventory();
            }
            return;
        }
        if(plugin.onGame.contains(p.getUniqueId())) {
            if(plugin.prewait) {
                return;
            }
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
                      plugin.p1canchooserps = false;
                      p.closeInventory();
                      }
                    if (e.getCurrentItem().getType() == Material.PAPER) {
                        if(plugin.p1finger < plugin.paperfinger) {
                            p.sendMessage(plugin.prefix + "§c指の本数が足りないため、パーをだすことはできません。");
                            return;
                        }
                        plugin.p1putoutfinger = plugin.paperfinger;
                        plugin.p1finger -= 5;
                        plugin.p1canchooserps = false;
                        p.closeInventory();
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
                        plugin.p2canchooserps = false;
                        p.closeInventory();
                    }
                    if(e.getCurrentItem().getType() == Material.SHEARS) {
                        if(plugin.p2finger < plugin.scissorfinger) {
                            p.sendMessage(plugin.prefix + "§c指の本数が足りないため、チョキをだすことはできません。");
                            return;
                        }
                        plugin.p2putoutfinger = plugin.scissorfinger;
                        plugin.p2finger -= 2;
                        plugin.p2canchooserps = false;
                        p.closeInventory();
                    }
                    if(e.getCurrentItem().getType() == Material.PAPER) {
                        if(plugin.p2finger < plugin.paperfinger) {
                            p.sendMessage(plugin.prefix + "§c指の本数が足りないため、パーをだすことはできません。");
                            return;
                        }
                        plugin.p2putoutfinger = plugin.paperfinger;
                        plugin.p2finger -= 5;
                        plugin.p2canchooserps = false;
                        p.closeInventory();
                    }
                    if(!plugin.p1canchooserps && !plugin.p2canchooserps) {
                        battle.judge();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onValueSelectedEvent(valueSelectedEvent e){
        if(e.getPluginname().equalsIgnoreCase("Man10Eighteen")){
            //ここに処理を書く
            e.getPlayer().sendMessage(plugin.prefix+"§e§l検出した金額: "+Man10Eighteen.getJpBal(e.getValue())+"円");
            Man10Eighteen.sendHoverText(e.getPlayer(),plugin.prefix+"§6§l§kaa§e§lここをクリックで募集します！§6§l§kaa","§a募集開始","/mer game "+e.getValue());
        }
    }
}
