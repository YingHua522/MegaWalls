/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.command.CommandException
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandKill extends BaseCommand {
    public CommandKill() {
        super("kill");
        this.setPermission("MegaWalls.default");
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
        if (game.isStarted()) {
            GamePlayer gamePlayer = GamePlayer.get(((Player) sender).getUniqueId());
            if (gamePlayer != null && gamePlayer.isSpectator()) {
                return;
            }
            if (!game.isWallsFall()) {
                sender.sendMessage("§c§l你不能在此阶段自杀！");
                return;
            }
            if (gamePlayer != null && gamePlayer.getGameTeam().isWitherDead() && gamePlayer.getGameTeam().getTeamWither().getBukkitEntity().isDead()) {
                sender.sendMessage("§c§l你的凋灵死了，此命令禁用！");
                return;
            }
            if (gamePlayer != null && gamePlayer.getGameTeam() != null) {
                gamePlayer.getPlayer().setHealth(0.0);
            }
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return true;
    }
}

