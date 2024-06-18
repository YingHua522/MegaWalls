/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandException
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.MegaWalls;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler
        implements CommandExecutor,
        TabCompleter {
    private final List<BaseCommand> commands = new ArrayList<>();

    public CommandHandler() {
        this.registerCommand(MegaWalls.getInstance(), new CommandStage());
        this.registerCommand(MegaWalls.getInstance(), new CommandKill());
        this.registerCommand(MegaWalls.getInstance(), new CommandKillWither());
        this.registerCommand(MegaWalls.getInstance(), new CommandSurface());
        this.registerCommand(MegaWalls.getInstance(), new CommandMaster());
        this.registerCommand(MegaWalls.getInstance(), new CommandCoins());
        this.registerCommand(MegaWalls.getInstance(), new CommandDust());
        this.registerCommand(MegaWalls.getInstance(), new CommandParty());
        this.registerCommand(MegaWalls.getInstance(), new CommandWalls());
        this.registerCommand(MegaWalls.getInstance(), new CommandForceStart());
        this.registerCommand(MegaWalls.getInstance(), new CommandPet());
        this.registerCommand(MegaWalls.getInstance(),new CommandMythicMode());
        this.registerCommand(MegaWalls.getInstance(),new CommandShout());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (BaseCommand command : this.commands) {
            if (!command.isValidTrigger(cmd.getName())) continue;
            if (!command.hasPermission(sender)) {
                sender.sendMessage("§c你没有此命令的权限！");
                return true;
            }
            if (command.isOnlyPlayerExecutable() && !(sender instanceof Player)) {
                sender.sendMessage("§c控制台无法使用此命令！");
                return true;
            }
            if (args.length >= command.getMinimumArguments()) {
                try {
                    command.execute(sender, label, args);
                    return true;
                } catch (CommandException commandException) {
                    continue;
                }
            }
            sender.sendMessage("§c错误的参数: /" + command.getName() + " " + command.getPossibleArguments());
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }

    public void registerCommand(JavaPlugin plugin, BaseCommand command) {
        this.commands.add(command);
        plugin.getCommand(command.getName()).setExecutor(this);
        plugin.getCommand(command.getName()).setTabCompleter(this);
    }
}

