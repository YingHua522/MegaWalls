/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.block.Block
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.game;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.tellraw.Tellraw;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class GameParty {
    private Game game;
    private GamePlayer leader = null;
    private List<GamePlayer> players = null;
    private List<GamePlayer> inviting = null;

    public GameParty(Game game, GamePlayer leader) {
        this.game = game;
        this.leader = leader;
        this.players = new ArrayList<GamePlayer>();
        this.inviting = new ArrayList<GamePlayer>();
        this.players.add(leader);
        game.addParty(this);
    }

    public GamePlayer getLeader() {
        return this.leader;
    }

    public boolean isLeader(GamePlayer p) {
        return p.equals(this.leader);
    }

    public List<GamePlayer> getPlayers() {
        return this.players;
    }

    public boolean isInTeam(GamePlayer p) {
        for (GamePlayer player : this.players) {
            if (!player.equals(p)) continue;
            return true;
        }
        return false;
    }

    public boolean addPlayer(GamePlayer p) {
        if (this.isFull() || this.isInTeam(p)) {
            return false;
        }
        this.players.add(p);
        return true;
    }

    public void removePlayer(GamePlayer p) {
        if (!this.isInTeam(p)) {
            return;
        }
        this.players.remove(p);
        if (this.players.isEmpty()) {
            this.game.removeParty(this);
            return;
        }
        if (this.isLeader(p)) {
            for (GamePlayer player : this.players) {
                if (!player.isOnline()) continue;
                this.leader = player;
                this.broadcast("§aThe party leader left the game,§e" + player.getName() + " §awas promoted to party leader!");
                break;
            }
            return;
        }
        this.broadcast("\u00a7aPlayer \u00a7e" + p.getName() + " \u00a7aleft the party!");
    }

    public boolean isFull() {
        return this.players.size() >= 400;
    }

    public boolean isInviting(GamePlayer p) {
        return this.inviting.contains(p);
    }

    public void invite(final GamePlayer p) {
        if (this.isFull() || this.isInviting(p)) {
            return;
        }
        this.inviting.add(p);
        this.leader.getPlayer().sendMessage("§aYou have invited §e" + p.getName() + "§a,the player has §e30§a seconds to accept invitation！");
        Tellraw.create("§e" + this.leader.getName() + " §ainvited you to team, you have §e30 §aseconds to accept invitation！").then("§b[Click Accept]").command("/team all " + this.leader.getName()).send(p.getPlayer());
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> GameParty.this.inviting.remove(p), 600L);
    }

    public void acceptInvite(GamePlayer p) {
        if (!this.isInviting(p)) {
            return;
        }
        if (this.addPlayer(p)) {
            this.inviting.remove(p);
            this.broadcast("\u00a7e" + p.getName() + " §ajoined the team!");
        }
    }

    public void broadcast(String message) {
        for (GamePlayer player : this.players) {
            player.sendMessage(message);
        }
    }

    public List<GamePlayer> getMates(GamePlayer gamePlayer) {
        ArrayList<GamePlayer> players = new ArrayList<GamePlayer>();
        for (GamePlayer player : this.getPlayers()) {
            if (player.equals(gamePlayer)) continue;
            players.add(player);
        }
        return players;
    }

    public boolean isProtectedBlock(Block block) {
        for (GamePlayer gamePlayer : this.players) {
            if (!gamePlayer.isProtectedBlock(block)) continue;
            return true;
        }
        return false;
    }
}

