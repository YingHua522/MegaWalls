/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandException
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GameParty;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandParty extends BaseCommand {
    public CommandParty() {
        super("team");
        this.setPermission("MegaWalls.default");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        Game game = MegaWalls.getInstance().getGame();
        if (game == null || game.isStarted()) {
            return;
        }
        Player p = (Player) sender;
        GamePlayer gameP = GamePlayer.get(p.getUniqueId());
        if (args[0].equalsIgnoreCase("invite") && args.length >= 2) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("§c该玩家不存在！");
                return;
            }
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            GameParty party = game.getPlayerParty(gameP);
            GameParty playerParty = game.getPlayerParty(gamePlayer);
            if (playerParty != null) {
                sender.sendMessage("§c该玩家已有队伍！");
                return;
            }
            if (party == null) {
                party = new GameParty(game, gameP);
                party.invite(gamePlayer);
            } else {
                if (gameP != null && !party.isLeader(gameP)) {
                    sender.sendMessage("§c你不是该队伍的队长！");
                    return;
                }
                if (party.isInviting(gamePlayer)) {
                    sender.sendMessage("§c你已邀请过该玩家！");
                    return;
                }
                if (party.isInTeam(gamePlayer)) {
                    sender.sendMessage("§a该玩家已在队伍中！");
                    return;
                }
                if (!party.isFull() && !party.isInTeam(gamePlayer)) {
                    party.invite(gamePlayer);
                }
            }
        } else if (args[0].equalsIgnoreCase("all") && args.length >= 2) {
            Player player = Bukkit.getPlayer(args[1]);
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            GameParty party = game.getPlayerParty(gameP);
            if (party != null) {
                sender.sendMessage("§a你已在队伍中！");
                return;
            }

            GameParty playerParty = game.getPlayerParty(gamePlayer);
            if (playerParty == null) {
                sender.sendMessage("§a该玩家没有队伍！");
                return;
            }
            if (playerParty.getPlayers().size() > 4) {
                sender.sendMessage("§a该队伍已满！");
                return;
            }
            if (gamePlayer != null && !playerParty.isLeader(gamePlayer)) {
                sender.sendMessage("§a该玩家不是队伍队长！");
                return;
            }
            if (!playerParty.isInviting(gameP)) {
                sender.sendMessage("§a该队伍队长没有邀请你！");
                return;
            }
            if (playerParty.isInTeam(gameP)) {
                sender.sendMessage("§a你已在队伍中！");
                return;
            }
            playerParty.acceptInvite(gameP);
        } else if (args[0].equalsIgnoreCase("leave")) {
            GameParty party = game.getPlayerParty(gameP);
            if (party == null) {
                sender.sendMessage("§c你不在一个队伍中！");
                return;
            }
            party.removePlayer(gameP);
            party.broadcast("§a玩家§e" + p.getName() + "§a离开了队伍！");
            p.sendMessage("§a你离开了队伍！");
            return;
        }
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public String getPossibleArguments() {
        return " invite/all/leave";
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return true;
    }
}

