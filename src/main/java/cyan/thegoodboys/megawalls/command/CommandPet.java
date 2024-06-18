/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.command.CommandException
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.pet.PetFollowRunner;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandPet extends BaseCommand {
    private List<GamePlayer> used = new ArrayList<GamePlayer>();

    public CommandPet() {
        super("spawnpet");
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
        GamePlayer gamePlayer = GamePlayer.get(((Player) sender).getUniqueId());
        assert gamePlayer != null;
        new PetFollowRunner(gamePlayer.getPlayer());
        sender.sendMessage("§aSpawned your Pet");
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return true;
    }
}

