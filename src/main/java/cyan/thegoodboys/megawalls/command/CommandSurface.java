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

import java.util.ArrayList;
import java.util.List;

public class CommandSurface extends BaseCommand {
    private List<GamePlayer> used = new ArrayList<GamePlayer>();

    public CommandSurface() {
        super("surface");
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
            if (gamePlayer == null || gamePlayer.isSpectator()) {
                return;
            }
            if (!gamePlayer.getPlayerStats().getEffectStats().isSurface()) {
                gamePlayer.sendMessage("§c你没有购买此命令，请到大厅处购买！");
                return;
            }
            if (gamePlayer.getPlayerStats().getEffectStats().isSurface() && !this.used.contains(gamePlayer)) {
                Player player = (Player) sender;
                player.teleport(gamePlayer.getGameTeam().getSpawnLocation());
                this.used.add(gamePlayer);
            }
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return true;
    }
}

