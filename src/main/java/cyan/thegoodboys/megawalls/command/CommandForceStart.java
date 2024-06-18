/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.command.CommandException
 *  org.bukkit.command.CommandSender
 */
package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.Game;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class CommandForceStart
        extends BaseCommand {
    public CommandForceStart() {
        super("forcestart");
        this.setPermission("MegaWalls.forcestart");
    }

    @Override
    public String getPossibleArguments() {
        return "";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        Game game = MegaWalls.getInstance().getGame();
        if (game == null) {
            sender.sendMessage("§c§l未获取到游戏对象,无法强制开启游戏！");
            return;
        }
        game.forceStart();
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return false;
    }
}

