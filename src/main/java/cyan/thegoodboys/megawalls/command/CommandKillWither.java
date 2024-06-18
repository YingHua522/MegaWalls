package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.Game;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class CommandKillWither extends BaseCommand {
    public CommandKillWither() {
        super("killwither");
        this.setPermission("MegaWalls.admin");
    }

    @Override
    public String getPossibleArguments() {
        return null;
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender var1, String var2, String[] var3) throws CommandException {
        Game game = MegaWalls.getInstance().getGame();
        if (game == null) {
            var1.sendMessage("§c§l未获取到游戏对象,无法强制开启游戏！");
        } else if (game.isStarted()) {
            game.getTeams().forEach(gameTeam -> {
                gameTeam.getTeamWither().setHealth(-99999999999999999999999999999.999999999999999999999999f);
                gameTeam.setWitherDead(true);
            });
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return false;
    }
}
