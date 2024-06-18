/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandException
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandCoins extends BaseCommand {
    public CommandCoins() {
        super("coins");
        this.setPermission("MegaWalls.coins");
    }

    @Override
    public String getPossibleArguments() {
        return "give <\u73a9\u5bb6\u540d> <\u6570\u91cf>";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if (args.length == 0 & !(sender instanceof ConsoleCommandSender)) {
            Player p = (Player) sender;
            GamePlayer gamePlayer = GamePlayer.get(p.getUniqueId());
            p.sendMessage("\u00a7a\u5f53\u524d\u786c\u5e01: " + gamePlayer.getPlayerStats().getCoins());
        } else if (args[0].equalsIgnoreCase("give") && args.length >= 3) {
            Player player = Bukkit.getPlayer((String) args[1]);
            int amount = 0;
            if (player == null) {
                sender.sendMessage("\u00a7a\u8be5\u73a9\u5bb6\u4e0d\u5728\u7ebf!");
                return;
            }
            try {
                amount = Integer.valueOf(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("\u00a7c\u8bf7\u8f93\u5165\u6709\u6548\u7684\u6570\u91cf!");
                return;
            }
            if (amount <= 0) {
                sender.sendMessage("\u00a7c\u8bf7\u8f93\u5165\u6709\u6548\u7684\u6570\u91cf!");
                return;
            }
            GamePlayer reciver = GamePlayer.get(player.getUniqueId());
            reciver.getPlayerStats().giveCoins(amount);
            sender.sendMessage("\u00a7a" + args[1] + "\u5f53\u524d\u786c\u5e01: " + reciver.getPlayerStats().getCoins());
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return false;
    }
}

