package com.github.mamizu0312.man10eighteen;


public class EighteenConfigManager {
    Man10Eighteen plugin;
    public EighteenConfigManager(Man10Eighteen plugin) {
        this.plugin = plugin;
    }
    public void loadConfig() {
        plugin.prefix = plugin.getConfig().getString("prefix");
        plugin.plstatus = plugin.getConfig().getBoolean("pluginstatus");
        plugin.specialbonus = plugin.getConfig().getInt("specialbonusstock");
        plugin.chance = plugin.getConfig().getInt("chance");
    }
    public void setPluginStatus(boolean plstatus) {
        plugin.plstatus = plstatus;
        plugin.getConfig().set("pluginstatus", plugin.plstatus);
        plugin.saveConfig();
        plugin.reloadConfig();
    }
    public void reload() {
        plugin.reloadConfig();
    }
    public void save() {
        plugin.getConfig().set("pluginstatus", plugin.plstatus);
        plugin.getConfig().set("specialbonusstock",plugin.specialbonus);
    }
}
