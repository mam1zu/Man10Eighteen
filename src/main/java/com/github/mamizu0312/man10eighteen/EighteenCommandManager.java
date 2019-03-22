package com.github.mamizu0312.man10eighteen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EighteenCommandManager implements CommandExecutor {
    Man10Eighteen plugin;
    public EighteenCommandManager(Man10Eighteen plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if(args.length == 0) {
            plugin.menuinv = Bukkit.createInventory(null, 9, plugin.prefix);
            ItemStack playbuttom = new ItemStack(Material.DIAMOND_HOE, 1, (short)0);
            ItemMeta playbuttommeta = playbuttom.getItemMeta();
            playbuttommeta.setDisplayName("§l[§aプレイ§f]");
            List<String> playbuttomlore = new ArrayList<>();
            playbuttomlore.add("§l現在準備中！");
            playbuttommeta.setLore(playbuttomlore);
            playbuttom.setItemMeta(playbuttommeta);

            ItemStack helpbuttom = new ItemStack(Material.DIAMOND_AXE, 1, (short)0);
            ItemMeta helpbuttommeta = helpbuttom.getItemMeta();
            helpbuttommeta.setDisplayName("&[&eヘルプ&f]");
            List<String> helpbuttomlore = new ArrayList<>();
            helpbuttomlore.add("現在準備中");
            helpbuttommeta.setLore(helpbuttomlore);
            helpbuttom.setItemMeta(helpbuttommeta);

            plugin.menuinv.setItem(0, playbuttom);
            plugin.menuinv.setItem(1, helpbuttom);
            plugin.onMenu.add(p.getUniqueId());
            p.openInventory(plugin.menuinv);
            return true;
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                HelpCommand(p);
                return true;
            }
            if(args[0].equalsIgnoreCase("game")) {
                p.sendMessage("§c§lプレイヤー名を指定してください。");
                return true;
            }
            if(args[0].equalsIgnoreCase("accept")) {
                if(plugin.onWait.containsValue(p.getUniqueId())) {
                    List<UUID> onwaitplayeruuid = new ArrayList<>();
                    for(UUID getkey : plugin.onWait.keySet()) {
                        onwaitplayeruuid.add(getkey);
                    }
                    for(UUID key : onwaitplayeruuid) {
                        if(plugin.onWait.get(key).equals(p.getUniqueId())) {
                            //TODO:ゲームが始まるコード
                            Player p2 = Bukkit.getServer().getPlayer(key);
                            plugin.onGame.add(p.getUniqueId());
                            plugin.onGame.add(p2.getUniqueId());
                            new EighteenBattleManager(plugin, p2, p);
                            return true;
                            //招待送ったほうをp1にするため逆になっている
                        }
                    }
                }
            }
        }
        return true;
    }
    public void HelpCommand(Player p) {
        p.sendMessage("----------"+plugin.prefix+"----------");
        p.sendMessage("/m18 :メニューを開きます");
    }
}