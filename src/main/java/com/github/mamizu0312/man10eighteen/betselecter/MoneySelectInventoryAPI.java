package com.github.mamizu0312.man10eighteen.betselecter;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

// 2019/04/19 created by Mr_IK

public class MoneySelectInventoryAPI implements Listener{

    /*
      Usage

      @EventHandler
      public void onValueSelectedEvent(valueSelectedEvent e){
           if(e.getPluginname().equalsIgnoreCase("MyPluginName")){
               //ここに処理を書く
           }
      }

     */

    JavaPlugin plugin;

    List<UUID> nowOpenPlayers;

    String pluginname;

    public MoneySelectInventoryAPI(JavaPlugin plugin, String pluginname){
        nowOpenPlayers = new ArrayList<>();
        this.plugin = plugin;
        this.pluginname = pluginname;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    public void openInventory(Player p){
        GachaBannerDictionary dic = new GachaBannerDictionary();
        Inventory inv = Bukkit.createInventory(null,54,"§e§l金額設定");
        for(int i = 0;i<54;i++){
            if(i>=9&&i<=17){
                continue;
            }
            inv.setItem(i,new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15));
        }
        ItemStack enter = createUnbitem("§a§l送信",new String[]{},Material.STAINED_GLASS_PANE,5,false);
        //28,29,30 //1,2,3
        inv.setItem(28,renameitem(dic.getItem(1),"1"));
        inv.setItem(29,renameitem(dic.getItem(2),"2"));
        inv.setItem(30,renameitem(dic.getItem(3),"3"));
        //37,38,39 //4,5,6
        inv.setItem(37,renameitem(dic.getItem(4),"4"));
        inv.setItem(38,renameitem(dic.getItem(5),"5"));
        inv.setItem(39,renameitem(dic.getItem(6),"6"));
        //46,47,48 //7,8,9
        inv.setItem(46,renameitem(dic.getItem(7),"7"));
        inv.setItem(47,renameitem(dic.getItem(8),"8"));
        inv.setItem(48,renameitem(dic.getItem(9),"9"));
        //40,0
        inv.setItem(40,renameitem(dic.getItem(0),"0"));
        //32,33,34 //e,.,clear
        inv.setItem(32,renameitem(dic.getSymbol("e"),"e"));
        inv.setItem(33,renameitem(dic.getSymbol("dot"),"."));
        inv.setItem(34,renameitem(new ItemStack(Material.REDSTONE_BLOCK),"§c§lクリア"));
        //51,52 //enter
        inv.setItem(51,enter);
        inv.setItem(52,enter);
        //open inv
        nowOpenPlayers.add(p.getUniqueId());
        p.openInventory(inv);
    }


    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(nowOpenPlayers.contains(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
            if(e.getClickedInventory().getName().equalsIgnoreCase("§e§l金額設定")) {
                if(e.getClickedInventory()==e.getWhoClicked().getInventory()) {
                    return;
                }
                Inventory inv = e.getClickedInventory();
                if (e.getSlot() == 28||e.getSlot() == 29||e.getSlot() == 30
                   ||e.getSlot() == 37||e.getSlot() == 38||e.getSlot() == 39
                   ||e.getSlot() == 46||e.getSlot() == 47||e.getSlot() == 48
                   ||e.getSlot() == 40||e.getSlot() == 32||e.getSlot() == 33) {
                                               //1,2,3,
                                               //4,5,6,
                                               //7,8,9,
                                               //0,e,.
                    for(int i = 9;i<18;i++) {
                        ItemStack item = inv.getItem(i);
                        if(i==9) {
                            inv.setItem(9, null);
                        }else if(i==17){
                            if(item!=null){
                                inv.setItem(i-1,item);
                            }
                            inv.setItem(17,inv.getItem(e.getSlot()));
                            ((Player)e.getWhoClicked()).updateInventory();
                            break;
                        }else{
                            if(item!=null){
                                inv.setItem(i-1,item);
                            }
                        }
                    }
                    ((Player)e.getWhoClicked()).updateInventory();
                } else if (e.getSlot() == 34) {
                    for(int i = 9;i<18;i++) {
                        inv.setItem(i, null);
                    }
                    ((Player)e.getWhoClicked()).updateInventory();
                } else if (e.getSlot() == 51||e.getSlot() == 52) {
                    e.getWhoClicked().closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if(nowOpenPlayers.contains(e.getPlayer().getUniqueId())) {
            Inventory inv = e.getInventory();
            String str = "";
            double value;
            for (int i = 9; i < 18; i++) {
                ItemStack item = inv.getItem(i);
                if (item == null) {
                    continue;
                }
                String add = item.getItemMeta().getDisplayName();
                str = str + add;
            }
            try {
                value = Double.parseDouble(str);
            } catch (NumberFormatException e1) {
                e.getPlayer().sendMessage("§c数にエラーが発生しました: 「" + str + "」 は数字に変換できません");
                nowOpenPlayers.remove(e.getPlayer().getUniqueId());
                return;
            }

            valueSelectedEvent event = new valueSelectedEvent((Player) e.getPlayer(), value, pluginname);
            Bukkit.getServer().getPluginManager().callEvent(event);
            nowOpenPlayers.remove(e.getPlayer().getUniqueId());
        }
    }

    private ItemStack createUnbitem(String name, String[] lore, Material item, int dura, boolean pikapika){
        ItemStack items = new ItemStack(item,1,(short)dura);
        ItemMeta meta = items.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(pikapika){
            meta.addEnchant(Enchantment.ARROW_FIRE,1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setUnbreakable(true);
        items.setItemMeta(meta);
        return items;
    }

    public ItemStack renameitem(ItemStack item,String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    class GachaBannerDictionary{

        /**
         * Created by sho on 2018/07/12.
         */
        HashMap<Integer, ItemStack> banner = new HashMap<>();
        HashMap<String, ItemStack> symbol = new HashMap<>();

        public GachaBannerDictionary(){
            banner.put(0, new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
            banner.put(1, new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.SQUARE_TOP_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_CENTER)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
            banner.put(2, new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.RHOMBUS_MIDDLE)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
            banner.put(3, new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
            banner.put(4, new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
            banner.put(5, new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT)).pattern(new Pattern(DyeColor.WHITE, PatternType.CURLY_BORDER)).pattern(new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
            banner.put(6, new SBannerItemStack((short) 0 ).pattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL_MIRROR)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
            banner.put(7, new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.WHITE, PatternType.DIAGONAL_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNLEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.SQUARE_BOTTOM_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
            banner.put(8, new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
            banner.put(9, new SBannerItemStack((short) 0 ).pattern(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());

            symbol.put("plus",  new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRAIGHT_CROSS)).pattern(new Pattern(DyeColor.BLUE, PatternType.BORDER)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLUE, PatternType.STRIPE_BOTTOM)).build());
            symbol.put("minus",  new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLUE, PatternType.BORDER)).build());
            symbol.put("dot", new SBannerItemStack((short)15).pattern(new Pattern(DyeColor.BLACK, PatternType.CIRCLE_MIDDLE)).build());
            symbol.put("back", new SBannerItemStack((short)15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER)).build());
            symbol.put("e", new SBannerItemStack((short) 15).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE)).pattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_BOTTOM)).pattern(new Pattern(DyeColor.WHITE, PatternType.BORDER)).build());
        }

        public ItemStack getItem(int id){
            return banner.get(id);
        }

        public ItemStack getSymbol(String id){
            return symbol.get(id);
        }
    }

    class SBannerItemStack {
        ItemStack banner = new ItemStack(Material.BANNER);

        public SBannerItemStack(short color) {
            banner.setDurability(color);
        }

        public SBannerItemStack pattern(Pattern pattern) {
            BannerMeta meta = (BannerMeta) banner.getItemMeta();
            meta.addPattern(pattern);
            banner.setItemMeta(meta);
            return this;
        }

        public SBannerItemStack patterns(List<Pattern> patterns) {
            BannerMeta meta = (BannerMeta) banner.getItemMeta();
            for (Pattern pat : patterns) {
                meta.addPattern(pat);
            }
            banner.setItemMeta(meta);
            return this;
        }

        public ItemStack build() {
            ItemMeta item = this.banner.getItemMeta();
            item.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            this.banner.setItemMeta(item);
            return this.banner;
        }
    }
}
