package com.github.mamizu0312.man10eighteen;


public class EighteenConfigManager {
    Man10Eighteen plugin;
    public EighteenConfigManager(Man10Eighteen plugin) {
        this.plugin = plugin;
    }
    public void load() {
        plugin.prefix = plugin.getConfig().getString("prefix");
        plugin.plstatus = plugin.getConfig().getBoolean("pluginstatus");
        plugin.specialbonus = plugin.getConfig().getDouble("specialbonusstock");
        plugin.chance = plugin.getConfig().getInt("chance");
        plugin.minimumbet = plugin.getConfig().getDouble("minimumbet");
        plugin.maximumbet = plugin.getConfig().getDouble("maximumbet");
        plugin.HOST = plugin.getConfig().getString("mysql.host");
        plugin.DB = plugin.getConfig().getString("mysql.db");
        plugin.USER = plugin.getConfig().getString("mysql.user");
        plugin.PASS = plugin.getConfig().getString("mysql.pass");
        plugin.PORT = plugin.getConfig().getString("mysql.port");
    }
    public void setPluginStatus(boolean plstatus) {
        plugin.plstatus = plstatus;
        plugin.getConfig().set("pluginstatus", plugin.plstatus);
        plugin.saveConfig();
        plugin.reloadConfig();
    }
    public void reload() {
        save();
        load();
        plugin.reloadConfig();
    }
    public void save() {
        plugin.getConfig().set("prefix",plugin.prefix);
        plugin.getConfig().set("pluginstatus",plugin.plstatus);
        plugin.getConfig().set("specialbonusstock",plugin.specialbonus);
        plugin.HOST = plugin.getConfig().getString("mysql.host");
        plugin.DB = plugin.getConfig().getString("mysql.db");
        plugin.USER = plugin.getConfig().getString("mysql.user");
        plugin.PASS = plugin.getConfig().getString("mysql.pass");
        plugin.PORT = plugin.getConfig().getString("mysql.port");
        plugin.chance = plugin.getConfig().getInt("chance");
        plugin.minimumbet = plugin.getConfig().getDouble("minimumbet");
        plugin.maximumbet = plugin.getConfig().getDouble("maximumbet");
        plugin.specialbonus = plugin.getConfig().getDouble("specialbonusstock");
        plugin.saveConfig();
    }
}
