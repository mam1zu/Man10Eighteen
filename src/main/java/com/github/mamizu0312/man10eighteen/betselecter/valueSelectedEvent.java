package com.github.mamizu0312.man10eighteen.betselecter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class valueSelectedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private double value;
    private String pluginname;

    public valueSelectedEvent(Player player, double value, String name) {
        this.player = player;
        this.value = value;
        this.pluginname = name;
    }

    public Player getPlayer() {
        return player;
    }

    public String getPluginname() { return pluginname; }

    public double getValue() {
        return value;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
