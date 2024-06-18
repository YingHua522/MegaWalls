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

public class CommandDust extends BaseCommand {
    public CommandDust() {
        super("dust");
        this.setPermission("MegaWalls.dust");
    }

    @Override
    public String getPossibleArguments() {
        return "give <玩家名> <数量>";
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
            sender.sendMessage("§a当前神话之尘: " + gamePlayer.getPlayerStats().getMythicDust());
        } else if (args[0].equalsIgnoreCase("give") && args.length >= 3) {
            Player player = Bukkit.getPlayer((String) args[1]);
            int amount = 0;
            if (player == null) {
                sender.sendMessage("§a该玩家不在线!");
                return;
            }
            try {
                amount = Integer.valueOf(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§c请输入有效的数量!");
                return;
            }
            if (amount <= 0) {
                sender.sendMessage("§c请输入有效的数量!");
                return;
            }
            GamePlayer reciver = GamePlayer.get(player.getUniqueId());
            reciver.getPlayerStats().giveMythicDust(amount);
            sender.sendMessage("§a" + args[1] + "当前神话之尘: " + reciver.getPlayerStats().getMythicDust());
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return false;
    }
}

