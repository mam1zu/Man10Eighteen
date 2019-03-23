package com.github.mamizu0312.man10eighteen;


public class EighteenConfigManager {
    Man10Eighteen plugin;
    public EighteenConfigManager(Man10Eighteen plugin) {
        this.plugin = plugin;
    }
    public void loadConfig() {
        plugin.prefix = plugin.getConfig().getString("prefix");
        plugin.plstatus = plugin.getConfig().getBoolean("pluginstatus");
        plugin.minimumbet = plugin.getConfig().getInt("minimumBetMoney");
        plugin.maximumbet = plugin.getConfig().getInt("maximumBetMoney");
    }
    public void setPluginStatus(boolean plstatus) {
        plugin.plstatus = plstatus;
        plugin.saveConfig();
        plugin.reloadConfig();
    }
    public void writePluginStatus() {
        plugin.getConfig().set("pluginstatus", plugin.plstatus);
        plugin.saveConfig();
    }
}
