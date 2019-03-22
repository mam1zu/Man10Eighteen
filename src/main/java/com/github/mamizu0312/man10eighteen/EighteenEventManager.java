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
        this.battle = new EighteenBattleManager(plugin);
    }
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        if(p==null) {
            return;
        }
        if(plugin.onGame.contains(p.getUniqueId())) {
            if(battle.isPlayerP1(p)) {
                if(e.getCurrentItem().getType() == Material.STONE) {

                }
            }
        }
    }
}
