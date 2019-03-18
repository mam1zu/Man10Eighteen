package com.github.mamizu0312.man10eighteen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EighteenCommandManager implements CommandExecutor {
    String prefix = "§7[§aeighteen§7]§r";
    EighteenBattleManager ebm = null;
    public boolean onCommand(CommandSender sender, Command command ,String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if(args.length == 0) {

            Inventory inv = Bukkit.createInventory(null, 9, prefix);
            ItemStack item = new ItemStack(Material.DIAMOND_HOE, 1, (short)1);
            ItemMeta itemm = item.getItemMeta();
            itemm.setDisplayName("プレイ");
            itemm.setUnbreakable(true);
            itemm.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            List<String> lore = new ArrayList<>();
            lore.add("現在準備中");
            itemm.setLore(lore);
            item.setItemMeta(itemm);
            inv.setItem(0, item);

            ItemStack item2 = new ItemStack(Material.DIAMOND_AXE, 1, (short)0);
            ItemMeta itemm2 = item2.getItemMeta();
            itemm2.setDisplayName("COMとプレイを開始します。難易度設定不可");
            List<String> lore2 = new ArrayList<>();
            lore.add("現在準備中");
            itemm2.setLore(lore2);
            inv.setItem(1, item2);

            ItemStack item3 = new ItemStack(Material.DIAMOND_SWORD, 1, (short)0);
            ItemMeta itemm3 = item.getItemMeta();
            itemm3.setDisplayName("現在準備中");
            item3.setItemMeta(itemm3);
            inv.setItem(2, item3);

            p.openInventory(inv);
            return true;
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                HelpCommand(p);
                return true;
            }
            if(args[0].equalsIgnoreCase("game")) {
                p.sendMessage(prefix + "§cプレイヤー名を指定してください。");
                return true;
            }
            if(args[0].equalsIgnoreCase("accept")) {
                if(EighteenStatus.onWait.containsValue(p.getUniqueId())) {
                    List<UUID> onwaitplayeruuid = new ArrayList<UUID>();
                    for(UUID getkey : EighteenStatus.onWait.keySet()) {
                        onwaitplayeruuid.add(getkey);
                    }
                    for(UUID key : onwaitplayeruuid) {
                        if(EighteenStatus.onWait.get(key).equals(p.getUniqueId())) {
                            //TODO:ゲームが始まるコード
                            Player p2 = Bukkit.getServer().getPlayer(key);
                            EighteenStatus.onGame.add(p.getUniqueId());
                            EighteenStatus.onGame.add(p2.getUniqueId());

                        }
                    }
                } else {
                    p.sendMessage(prefix + "§c招待は、何一つ、ありませんでした...");
                    return true;
                }
            }
        }
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("game")) {
                Player p2 = Bukkit.getServer().getPlayer(args[1]);
                if(p2 == null) {
                    p.sendMessage(prefix + "§cプレイヤーが存在しません。");
                    return true;
                }
                EighteenStatus.onWait.put(p.getUniqueId(), p2.getUniqueId());
                p2.sendMessage(prefix + "§e§l"+p.getName()+"§r§eから招待が来ました。/eighteen acceptで承諾します。");
                return true;
            }
        }
        return true;
    }
    public static void HelpCommand(Player p) {
        p.sendMessage("��e---��a18(Eighteen)��e---");
        p.sendMessage("/eighteen :メニューを開きます");
    }
}
