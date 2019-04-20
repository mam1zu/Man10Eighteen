package com.github.mamizu0312.man10eighteen;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class EighteenCommandManager implements CommandExecutor {
    Man10Eighteen plugin;
    EighteenConfigManager config;
    EighteenBattleManager battle;
    VaultManager vault;
    int prechance;
    double premaximumbet;
    double preminimumbet;
    EighteenMySQLManager mysql;
    EighteenVoteManager vote;
    public EighteenCommandManager(Man10Eighteen plugin) {
        this.plugin = plugin;
        config = new EighteenConfigManager(plugin);
        vault = new VaultManager(plugin);
        mysql = new EighteenMySQLManager(plugin,"Man10Eighteen");
        vote = new EighteenVoteManager(plugin);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if(!p.hasPermission("mer.play")) {
            p.sendMessage(plugin.prefix + "§4§l権限がありません");
            return true;
        }
        if(args.length == 0) {
            p.sendMessage("=========="+plugin.prefix+"==========");
            if(plugin.prewait) {
                p.sendMessage("§e§l現在参加者を募集しています！");
                p.sendMessage("§e賭け金: §l"+moneyformat(plugin.betmoney));
            } else {
                p.sendMessage("§c現在試合は開催されていません");
            }
            p.sendMessage("§e§l現在のストック: "+plugin.specialbonus+"円");
            TextComponent ruletc = new TextComponent();
            ruletc.setColor(ChatColor.MAGIC);
            ruletc.addExtra("a");
            ruletc.setColor(ChatColor.RESET);
            ruletc.addExtra("ルールはここをクリック");
            ruletc.setColor(ChatColor.MAGIC);
            ruletc.addExtra("a");
            ruletc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mer rule"));
            p.spigot().sendMessage(ruletc);
            TextComponent helptc = new TextComponent();
            helptc.setColor(ChatColor.MAGIC);
            helptc.addExtra("a");
            helptc.setColor(ChatColor.RESET);
            helptc.addExtra("ヘルプはここをクリック");
            helptc.setColor(ChatColor.MAGIC);
            helptc.addExtra("a");
            helptc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mer help"));
            p.spigot().sendMessage(helptc);
            p.sendMessage("§3§lDeveloped by Mamizu0312");
            return true;
        }
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("rule")) {
                p.sendMessage("=========="+plugin.prefix+"§e§lルール解説§r==========");
                p.sendMessage("じゃんけんを10回行います。使える指は18本です。");
                p.sendMessage("パーは5本、チョキは2本、グーは0本です。");
                p.sendMessage("勝てば1ポイント、あいこは特典なし、6ラウンドと10ラウンドは2ポイント獲得できます。");
                p.sendMessage("指が余った場合、スコアからマイナスされるので気を付けてください。");
                p.sendMessage("得点が多いほうが勝ち、同点は引き分けです。");
                p.sendMessage("勝負するたびに賭け金の5%がストックに貯まっていきます。");
                p.sendMessage("§e§l§ka§r§e§lBonusTime§e§l§ka§rについて");
                p.sendMessage("BonusTimeが始まると、どちらが勝つか予想し、投票できるようになります。");
                p.sendMessage("ゲームが終わると、勝者に投票した人の中から抽選でプレイヤーが選ばれます。");
                p.sendMessage("選ばれたプレイヤーは、勝者とストックを山分けできます。(半分ずつもらえます)");
            }
            if (args[0].equalsIgnoreCase("on")) {
                if(!p.hasPermission("mer.staff")) {
                    p.sendMessage(plugin.prefix + "§4§l権限がありません");
                    return true;
                }
                if (!plugin.plstatus) {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを起動しています...");
                    plugin.reset();
                    config.setPluginStatus(true);
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを起動しました！");
                    return true;
                }
                p.sendMessage(plugin.prefix + "§c§lプラグインはすでに起動しています");
                return true;
            }
            if (args[0].equalsIgnoreCase("game")) {
                plugin.msi.openInventory(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("off")) {
                if(!p.hasPermission("mer.staff")) {
                    p.sendMessage(plugin.prefix + "§4§l権限がありません");
                    return true;
                }
                if (plugin.plstatus) {
                    if (!plugin.onGame.isEmpty()) {
                        Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しています...");
                        config.setPluginStatus(false);
                        if(plugin.prewait) {
                            p.sendMessage(plugin.prefix + "§c§l試合がキャンセルされました。賭け金は返金されます");
                            vault.deposit(p.getUniqueId(), plugin.betmoney);
                            mysql.senddepositinfo(p,plugin.betmoney);
                            plugin.onGame.clear();
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しました。");
                            return true;
                        }
                        battle.emergencystop();
                        Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しました。");
                        return true;
                    }
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しています...");
                    plugin.reset();
                    config.setPluginStatus(false);
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§c§lプラグインを停止しました。");
                    return true;
                }
                p.sendMessage(plugin.prefix + "§c§lプラグインはすでに停止しています");
                return true;
            }
            if(args[0].equalsIgnoreCase("reload")) {
                if(!p.hasPermission("mer.op")) {
                    p.sendMessage(plugin.prefix + "§4§l権限がありません");
                    return true;
                }
                plugin.reloadConfig();
                p.sendMessage(plugin.prefix + "§e§lconfigをリロードしました。");
                return true;
            }
            if(args[0].equalsIgnoreCase("help")) {
                HelpCommand(p);
                return true;
            }
            if(args[0].equalsIgnoreCase("vote")) {
                if(plugin.onGame.isEmpty()) {
                    p.sendMessage(plugin.prefix + "§c現在開催されていません");
                    return true;
                }
                if(!plugin.votetime) {
                    p.sendMessage(plugin.prefix + "§c現在投票は受け付けていません");
                    return true;
                }
                if(plugin.votep1.contains(p.getUniqueId()) || plugin.votep2.contains(p.getUniqueId())) {
                    p.sendMessage(plugin.prefix + "§c§lあなたはすでに投票済みです");
                    return true;
                }
                if(plugin.onGame.contains(p.getUniqueId())) {
                    p.sendMessage(plugin.prefix + "§c§l選手は投票できません");
                    return true;
                }
                plugin.onVoting.add(p.getUniqueId());
                vote.openvoteinv(p);
                return true;
            }
            if(args[0].equalsIgnoreCase("join")) {
                if (plugin.onGame.isEmpty()) {
                    p.sendMessage(plugin.prefix + "§c§l現在開催されていません");
                    return true;
                }
                if(!plugin.prewait) {
                    p.sendMessage(plugin.prefix+"§c§l現在開催中です");
                    return true;
                }
                if (plugin.onGame.get(0) == p.getUniqueId()) {
                    p.sendMessage(plugin.prefix + "§c§lあなたはすでに参加しています");
                    return true;
                }
                if(vault.getBalance(p.getUniqueId()) <= plugin.betmoney) {
                    p.sendMessage(plugin.prefix + "§c§l所持金が足りません");
                    return true;
                }
                Random r = new Random();
                if(r.nextInt(plugin.chance)+1 == 1) {
                    plugin.fevertime = true;
                    getServer().broadcastMessage(plugin.prefix + "§e§l" + p.getName() + "§fさんが参加しました！");
                    getServer().broadcastMessage(plugin.prefix + "§e§ka§r§e§lBonusTime§ka");
                    getServer().broadcastMessage(plugin.prefix + "§e§l大金獲得のチャンス！勝者を予想してストックを手に入れろ！");
                    TextComponent clicktovote = new TextComponent();
                    clicktovote.setColor(ChatColor.YELLOW);
                    clicktovote.setColor(ChatColor.MAGIC);
                    clicktovote.addExtra("a");
                    clicktovote.setColor(ChatColor.RESET);
                    clicktovote.setColor(ChatColor.YELLOW);
                    clicktovote.setBold(true);
                    clicktovote.addExtra("ここをクリックで抽選に参加");
                    clicktovote.setColor(ChatColor.RESET);
                    clicktovote.setColor(ChatColor.YELLOW);
                    clicktovote.setColor(ChatColor.MAGIC);
                    clicktovote.addExtra("a");
                    clicktovote.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mer vote"));
                    Bukkit.spigot().broadcast(clicktovote);
                    vault.withdraw(p.getUniqueId(), plugin.betmoney);
                    mysql.sendwithdrawinfo(p,plugin.betmoney);
                    plugin.onGame.add(p.getUniqueId());
                    plugin.votetime = true;
                    battle = new EighteenBattleManager(plugin, Bukkit.getPlayer(plugin.onGame.get(0)), p);
                    plugin.event.battle =  battle;
                    plugin.prewait = false;
                    plugin.onGame.add(p.getUniqueId());
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§a抽選受付終了まで残り60秒");
                    new BukkitRunnable() {
                        public void run() {
                            if(plugin.onGame.isEmpty()) {
                                return;
                            }
                            Bukkit.broadcastMessage(plugin.prefix + "§a抽選受付終了まで残り30秒");
                            Bukkit.spigot().broadcast(clicktovote);
                        }
                    }.runTaskLater(plugin,600);
                    new BukkitRunnable() {
                        public void run() {
                            if(plugin.onGame.isEmpty()) {
                                return;
                            }
                            Bukkit.broadcastMessage(plugin.prefix + "§a抽選受付終了まで残り10秒");
                            Bukkit.spigot().broadcast(clicktovote);
                        }
                    }.runTaskLater(plugin,1000);
                    new BukkitRunnable() {
                        public void run() {
                            if(plugin.onGame.isEmpty()) {
                                return;
                            }
                            Bukkit.broadcastMessage(plugin.prefix + "§a抽選受付終了まで残り5秒");
                            Bukkit.spigot().broadcast(clicktovote);
                        }
                    }.runTaskLater(plugin,1100);
                    new BukkitRunnable() {
                        public void run() {
                            if(plugin.onGame.isEmpty()) {
                                return;
                            }
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§a抽選受付終了まで残り4秒");
                        }
                    }.runTaskLater(plugin,1120);
                    new BukkitRunnable() {
                        public void run() {
                            if(plugin.onGame.isEmpty()) {
                                return;
                            }
                            Bukkit.broadcastMessage(plugin.prefix + "§a抽選受付終了まで残り3秒");
                        }
                    }.runTaskLater(plugin,1140);
                    new BukkitRunnable() {
                        public void run() {
                            if(plugin.onGame.isEmpty()) {
                                return;
                            }
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§a抽選受付終了まで残り2秒");
                        }
                    }.runTaskLater(plugin,1160);
                    new BukkitRunnable() {
                        public void run() {
                            if(plugin.onGame.isEmpty()) {
                                return;
                            }
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§a抽選受付終了まで残り1秒");
                        }
                    }.runTaskLater(plugin,1180);
                    new BukkitRunnable() {
                        public void run() {
                            if(plugin.onGame.isEmpty()) {
                                return;
                            }
                            Bukkit.getServer().broadcastMessage(plugin.prefix + "§a終了しました");
                            plugin.votetime = false;
                            battle = new EighteenBattleManager(plugin, Bukkit.getPlayer(plugin.onGame.get(0)), p);
                            plugin.event.battle =  battle;
                            battle.game();
                            plugin.p1canchooserps = true;
                            plugin.p2canchooserps = true;
                            plugin.prewait = false;
                            plugin.p2status = true;
                        }
                    }.runTaskLater(plugin, 1200);
                    return true;
                }
                vault.withdraw(p.getUniqueId(), plugin.betmoney);
                mysql.sendwithdrawinfo(p,plugin.betmoney);
                getServer().broadcastMessage(plugin.prefix + "§e§l" + p.getName() + "§fさんが参加しました！ゲームを開始します...");
                battle = new EighteenBattleManager(plugin, Bukkit.getPlayer(plugin.onGame.get(0)), p);
                plugin.event.battle =  battle;
                battle.game();
                plugin.p1canchooserps = true;
                plugin.p2canchooserps = true;
                plugin.p2status = true;
            }
            if(args[0].equalsIgnoreCase("debug")) {
                if(!p.hasPermission("mer.staff")) {
                    p.sendMessage(plugin.prefix + "§4§l権限がありません");
                    return true;
                }
                p.sendMessage("デバッグ情報 内部の数値 開発者用");
                p.sendMessage("round :"+plugin.round);
                p.sendMessage("p1score :"+plugin.p1score);
                p.sendMessage("p2score :"+plugin.p2score);
                p.sendMessage("p1finger :"+plugin.p1finger);
                p.sendMessage("p2finger :"+plugin.p2finger);
                p.sendMessage("minimumbet :"+plugin.minimumbet);
                p.sendMessage("maximumbet +"+plugin.maximumbet);
                p.sendMessage("betmoney :"+plugin.betmoney);
                p.sendMessage("fevertime :"+plugin.fevertime);
                p.sendMessage("specialbonus :"+plugin.specialbonus);
                p.sendMessage("bonuscompetitive :"+plugin.bonuscompetitive);
                p.sendMessage("chance :"+plugin.chance);
                p.sendMessage("p1putoutfinger :"+plugin.p1putoutfinger);
                p.sendMessage("p2putoutfinger :"+plugin.p2putoutfinger);
                p.sendMessage("prewait :"+plugin.prewait);
                if(plugin.onGame.get(0) == null) {
                    p.sendMessage("p1uuid :null");
                } else {
                    p.sendMessage("p1uuid :"+plugin.onGame.get(0));
                }
                if(plugin.onGame.get(1) == null) {
                    p.sendMessage("p1uuid :null");
                } else {
                    p.sendMessage("p2uuid :"+plugin.onGame.get(1));
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("reopen")) {
                if(plugin.onGame.isEmpty()) {
                    p.sendMessage(plugin.prefix + "§c§lあなたは試合に参加していません");
                    return true;
                }
                if(plugin.prewait) {
                    p.sendMessage(plugin.prefix + "§c§l試合はまだ開始されていません");
                    return true;
                }
                if(plugin.onGame.get(0) == p.getUniqueId()) {
                    if(!plugin.p1canchooserps) {
                        p.sendMessage(plugin.prefix + "§cあなたはすでに選択しています");
                        return true;
                    }
                    p.openInventory(plugin.p1inv);
                    return true;
                }
                if(plugin.onGame.get(1) == p.getUniqueId()) {
                    if(!plugin.p2canchooserps) {
                        p.sendMessage(plugin.prefix + "§cあなたはすでに選択しています");
                        return true;
                    }
                    p.openInventory(plugin.p2inv);
                    return true;
                }
            }
            if(args[0].equalsIgnoreCase("stop")) {
                if(!p.hasPermission("mer.staff")) {
                    p.sendMessage(plugin.prefix + "§4§l権限がありません");
                    return true;
                }
                if(plugin.onGame.isEmpty()) {
                    p.sendMessage(plugin.prefix + "§c§l現在開催されていません");
                    return true;
                }
                if(plugin.prewait) {
                    Bukkit.getServer().broadcastMessage(plugin.prefix + "§4§l試合がキャンセルされました");
                    p.sendMessage(plugin.prefix + "§c§l賭け金は返金されます");
                    vault.deposit(plugin.onGame.get(0), plugin.betmoney);
                    mysql.senddepositinfo(Bukkit.getPlayer(plugin.onGame.get(0)),plugin.betmoney);
                    plugin.onGame.clear();
                    plugin.reset();
                    return true;
                }
                battle.emergencystop();
                return true;
            }
            if(args[0].equalsIgnoreCase("confighelp")) {
                if(p.hasPermission("mer.op"))  {
                    confighelp(p);
                    return true;
                }
                p.sendMessage(plugin.prefix + "§4§l権限がありません");
                return true;
            }
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("game")) {
            if(!plugin.plstatus) {
                p.sendMessage(plugin.prefix + "§e§l現在プラグインは停止しています");
                return true;
            }
            if(!plugin.onGame.isEmpty()) {
                p.sendMessage(plugin.prefix+"§c§l現在開催中です");
                return true;
            }
            try {
                plugin.betmoney = Double.parseDouble(args[1]);
            } catch(NumberFormatException e) {
                p.sendMessage(plugin.prefix + "§c§l賭け金が不正です");
                return true;
            }
            if(plugin.betmoney < plugin.minimumbet || plugin.betmoney > plugin.maximumbet) {
                p.sendMessage(plugin.prefix + "§c§l賭け金が少なすぎるまたは多すぎます");
                return true;
            }
            if(vault.getBalance(p.getUniqueId()) <= plugin.betmoney) {
                plugin.betmoney = 0;
                p.sendMessage(plugin.prefix + "§c§l所持金が足りません");
                return true;
            }
            vault.withdraw(p.getUniqueId(), plugin.betmoney);
            mysql.sendwithdrawinfo(p,plugin.betmoney);
            getServer().broadcastMessage(plugin.prefix + "§e§l"+p.getName()+"§fさんが§e"+moneyformat(plugin.betmoney)+"§fで試合を開きました！ §a/mer join§fで参加！");
            plugin.p1status = true;
            plugin.prewait = true;
            plugin.onGame.add(p.getUniqueId());
        }
        if(args.length == 3 && args[0].equalsIgnoreCase("changeconfig")) {
            if (!p.hasPermission("mer.op")) {
                p.sendMessage(plugin.prefix + "§4§l権限がありません");
                return true;
            }
            if (args[1].equalsIgnoreCase("chance")) {
                try {
                    prechance = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    p.sendMessage(plugin.prefix + "§c§l値が不正です");
                    return true;
                }
                if (prechance <= 0) {
                    p.sendMessage(plugin.prefix + "§c§l値は1以上にしてください");
                    return true;
                }
                config.setchance(prechance);
                prechance = 0;
                p.sendMessage(plugin.prefix + "§c§lchanceを変更しました");
                plugin.saveConfig();
                plugin.reloadConfig();
            }
            if (args[1].equalsIgnoreCase("maximumbet")) {
                try {
                    premaximumbet = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    p.sendMessage(plugin.prefix + "§c§l値が不正です");
                    return true;
                }
                config.setmaximumbet(premaximumbet);
                premaximumbet = 0;
                p.sendMessage(plugin.prefix + "§c§lmaximumbetを変更しました");
                plugin.saveConfig();
                plugin.reloadConfig();
                return true;
            }
            if(args[1].equalsIgnoreCase("minimumbet")) {
                try {
                    preminimumbet = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    p.sendMessage(plugin.prefix + "§c§l値が不正です");
                    return true;
                }
                config.setminimumbet(preminimumbet);
                preminimumbet = 0;
                p.sendMessage(plugin.prefix + "§c§lminimumbetを変更しました");
                plugin.saveConfig();
                plugin.reloadConfig();
                return true;
            }
        }
        return true;
    }
    void confighelp(Player p) {
            p.sendMessage("----------" + plugin.prefix + "----------");
            p.sendMessage("§e§l/mer setconfig [関数名] [数値]: 関数名の数値の設定を変更します");
            p.sendMessage("§f§lchance: BonusTimeが発生する確率。n分の1の確立 型:int");
            p.sendMessage("§f§lmaximumbet: 最大賭け金。型:double");
            p.sendMessage("§f§lminimumbet: 最小賭け金。 型:double");
    }
    void HelpCommand(Player p) {
        if(p.hasPermission("mer.op") && p.hasPermission("mer.staff")) {
            p.sendMessage("----------"+plugin.prefix+"----------");
            p.sendMessage("§e§l/mer help§f: このページを開きます");
            p.sendMessage("§e§l/mer game§f: 新しく試合を開きます");
            p.sendMessage("§e§l/mer join§f: 試合に入ります");
            p.sendMessage("§e§l/mer reopen§f: メニューを再度開きます");
            p.sendMessage("§e§l/mer vote§f: 勝者の予想ができます");
            p.sendMessage("§l==========§cSTAFF以上§r§l==========");
            p.sendMessage("§c§l/mer on  §f: プラグインを起動します");
            p.sendMessage("§c§l/mer off §f: プラグインを停止します");
            p.sendMessage("§c§l/mer stop §f: 試合をストップします");
            p.sendMessage("§l==========§cOP以上§r§l==========");
            p.sendMessage("§c§l/mer changeconfig §f:configの値を変更します");
            return;
        }
        if(p.hasPermission("mer.staff")) {
            p.sendMessage("----------"+plugin.prefix+"----------");
            p.sendMessage("§e§l/mer help§f: このページを開きます");
            p.sendMessage("§e§l/mer game§f: 新しく試合を開きます");
            p.sendMessage("§e§l/mer join§f: 試合に入ります");
            p.sendMessage("§e§l/mer reopen§f: メニューを再度開きます");
            p.sendMessage("§e§l/mer vote§f: 勝者の予想ができます");
            p.sendMessage("§l==========§cSTAFF以上§r§l==========");
            p.sendMessage("§c§l/mer on  §f: プラグインを起動します");
            p.sendMessage("§c§l/mer off §f: プラグインを停止します");
            p.sendMessage("§c§l/mer stop §f: 試合をストップします");
            return;
        }
        p.sendMessage("----------"+plugin.prefix+"----------");
        p.sendMessage("§e§l/mer help§f: このページを開きます");
        p.sendMessage("§e§l/mer game§f: 新しく試合を開きます");
        p.sendMessage("§e§l/mer join§f: 試合に入ります");
        p.sendMessage("§e§l/mer reopen§f: メニューを再度開きます");
        p.sendMessage("§e§l/mer vote§f: 勝者の予想ができます");
    }
    String moneyformat(double money) {
        String moneystring;
        long premoney = (long) money;
        String moneybil = null;
        try {
            moneystring = Long.toString(premoney);
        } catch(NumberFormatException e) {
            return null;
        }
        int moneylength = moneystring.length();
        if(moneylength <= 4) {
            moneybil = moneystring.substring(0,moneylength);
            return moneybil + "円";
        }
        if(moneylength <= 8) {
            moneybil = moneystring.substring(0,moneylength-4);
            return moneybil + "万円";
        }
        if(moneylength <= 12) {
            moneybil = moneystring.substring(0,moneylength-8);
            return moneybil + "億円";
        }
        if(moneylength <= 16) {
            moneybil = moneystring.substring(0,moneylength-12);
            return moneybil + "兆円";
        }
        return null;
    }
}