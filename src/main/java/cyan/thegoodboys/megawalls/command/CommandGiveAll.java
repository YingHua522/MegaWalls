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
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandGiveAll
        extends BaseCommand {
    private List<GamePlayer> used = new ArrayList<GamePlayer>();

    public CommandGiveAll() {
        super("giveall");
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
            sender.sendMessage("\u00a7c\u00a7l\u672a\u83b7\u53d6\u5230\u6e38\u620f\u5bf9\u8c61,\u65e0\u6cd5\u5f3a\u5236\u5f00\u542f\u6e38\u620f\uff01");
            return;
        }
        if (game.isStarted()) {
            GamePlayer online = (GamePlayer) GamePlayer.getOnlinePlayers();
            GamePlayer gamePlayer = GamePlayer.get(((Player) sender).getUniqueId());
            if (gamePlayer.isSpectator()) {
                return;
            }
            if (!this.used.contains(gamePlayer)) {
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.LOG, 64).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.LOG, 64).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.LOG, 64).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.LOG, 64).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.DIAMOND_AXE, 1).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.DIAMOND, 64).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.ARROW, 64).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.ARROW, 64).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.ARROW, 64).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.COOKED_BEEF, 64).build());
                online.getPlayer().getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 16).build());
            }
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return true;
    }
}

