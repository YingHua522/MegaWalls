/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandException
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.database.KeyValue;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMaster extends BaseCommand {
    public CommandMaster() {
        super("master");
        this.setPermission("MegaWalls.admin");
    }

    @Override
    public String getPossibleArguments() {
        return "give <\u73a9\u5bb6\u540d> <\u804c\u4e1a> <\u6570\u91cf>";
    }

    @Override
    public int getMinimumArguments() {
        return 4;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        Player player = Bukkit.getPlayer((String) args[1]);
        if (player == null) {
            sender.sendMessage("\u00a7c\u8be5\u73a9\u5bb6\u4e0d\u5728\u7ebf\uff01");
            return;
        }
        Classes classes = ClassesManager.getClassesByName(args[2]);
        if (classes == null) {
            sender.sendMessage("\u00a7c\u8be5\u804c\u4e1a\u4e0d\u5b58\u5728\uff01");
        }
        int amount = 0;
        try {
            amount = Integer.valueOf(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage("\u00a7c\u8bf7\u8f93\u5165\u6709\u6548\u7684\u6570\u5b57\uff01");
            return;
        }
        if (amount <= 0) {
            sender.sendMessage("\u00a7c\u8bf7\u8f93\u5165\u6709\u6548\u7684\u6570\u5b57\uff01");
            return;
        }
        GamePlayer reciver = GamePlayer.get(player.getUniqueId());
        KitStatsContainer kitStats = reciver.getPlayerStats().getKitStats(classes);
        int points = kitStats.getMasterPoints() + amount;
        kitStats.setMasterPoints(points);
        MegaWalls.getInstance().getDataBase().dbUpdate("classes_" + classes.getName(), new KeyValue("masterPoints", points), new KeyValue("uuid", reciver.getUuid().toString()));
        sender.sendMessage("\u00a7a\u7ed9\u4e88\u6210\u529f\uff01");
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return false;
    }
}

