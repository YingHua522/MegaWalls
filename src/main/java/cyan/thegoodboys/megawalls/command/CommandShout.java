package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.LuckPremsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShout extends BaseCommand{
    public CommandShout() {
        super("shout");
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
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sender.sendMessage("§c请输入你要发送的信息");
                return;
            }
            StringBuilder messageBuilder = new StringBuilder();
            for (String arg : args) {
                messageBuilder.append(arg).append(" ");
            }
            Player player = ((Player) sender).getPlayer();
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            if (gamePlayer == null) return;
            String message = messageBuilder.toString().trim();
            Bukkit.getServer().broadcastMessage("§6[喊话] " + gamePlayer.getGameTeam().getTeamColor().getChatPrefix() + LuckPremsUtils.getPrefix(gamePlayer.getPlayer()) + gamePlayer.getPlayer().getDisplayName() + ": " + message);
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return false;
    }
}
