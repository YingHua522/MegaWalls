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
import cyan.thegoodboys.megawalls.game.stage.GameStage;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class CommandStage
        extends BaseCommand {
    public CommandStage() {
        super("stage");
        this.setPermission("MegaWalls.admin");
    }

    @Override
    public String getPossibleArguments() {
        return "<stage>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        Game game = MegaWalls.getInstance().getGame();
        if (game == null) {
            sender.sendMessage("\u00a7c\u00a7l\u672a\u83b7\u53d6\u5230\u6e38\u620f\u5bf9\u8c61,\u65e0\u6cd5\u5f3a\u5236\u5f00\u542f\u6e38\u620f\uff01");
            return;
        }
        try {
            int priority = Integer.parseInt(args[0]);
            GameStage stage = game.getStageManager().getStage(priority);
            if (stage == null) {
                sender.sendMessage("§c找不到游戏阶段！");
                return;
            }
            game.getStageManager().setCurrentStage(stage.getPriority() + 1);
            stage.excute(game);
            sender.sendMessage("\u00a7a\u6267\u884c\u6210\u529f\uff01");
        } catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return false;
    }
}

