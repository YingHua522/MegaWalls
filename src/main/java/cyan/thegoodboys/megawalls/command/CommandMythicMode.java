package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public class CommandMythicMode extends BaseCommand{
    public CommandMythicMode() {
        super("mythic");
        this.setPermission("MegaWalls.admin");
    }

    public String getPossibleArguments() {
        return null;
    }

    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if (args.length == 1) {
            try {
                int hours = Integer.parseInt(args[0]);
                if (hours >= 0) {
                    long time = System.currentTimeMillis() + 3600000L * (long) hours;
                    MegaWalls.setMythicMode(time);
                    sender.sendMessage("§e神话游戏" + (hours == 0 ? "关闭" : "开启") + "！");
                    if (hours > 0) {
                        for (GamePlayer gamePlayer : GamePlayer.getOnlinePlayers()) {
                            gamePlayer.sendTitle("§e神话游戏", "§6所有职业满级，双倍硬币已开启！", 20, 40, 20);
                            gamePlayer.playSound(Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
                        }
                    }

                }
            } catch (Exception var9) {
                sender.sendMessage("§c请输入有效的时间！");
            }
        } else if (args.length == 2) {
            try {
                int hours = Integer.parseInt(args[0]);
                int booster = Integer.parseInt(args[1]);
                if (hours >= 0) {
                    long time = System.currentTimeMillis() + 3600000L * (long) hours;
                    MegaWalls.setMythicMode(time);
                    sender.sendMessage("§e神话游戏" + (hours == 0 ? "关闭" : "开启") + "！");
                    if (hours > 0) {
                        for (GamePlayer gamePlayer : GamePlayer.getOnlinePlayers()) {
                            gamePlayer.sendTitle("§e神话游戏", "§6所有职业满级，" + booster + "硬币已开启！", 20, 40, 20);
                            gamePlayer.playSound(Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
                        }
                    }

                }
            } catch (Exception var9) {
                sender.sendMessage("§c请输入有效的时间！");
            }
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return false;
    }
}
