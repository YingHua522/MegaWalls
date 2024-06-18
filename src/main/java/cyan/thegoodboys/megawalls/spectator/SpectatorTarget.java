/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.GameMode
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.spectator;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class SpectatorTarget {
    private final DecimalFormat df = new DecimalFormat("0.0");
    private GamePlayer gamePlayer;
    private GamePlayer gameTarget;
    private Player player;
    private Player target;

    public SpectatorTarget(GamePlayer gamePlayer, GamePlayer gameTarget) {
        this.gamePlayer = gamePlayer;
        this.player = gamePlayer.getPlayer();
        this.gameTarget = gameTarget;
    }

    public GamePlayer getPlayer() {
        return this.gamePlayer;
    }

    public GamePlayer getTarget() {
        return this.gameTarget;
    }

    public void setTarget(GamePlayer gameTarget) {
        this.gameTarget = gameTarget;
        this.target = gameTarget.getPlayer();
    }

    public void sendTip() {
        if (!this.check()) {
            return;
        }
        if (this.player.getSpectatorTarget() != null && this.player.getSpectatorTarget().equals(this.target)) {
            this.gamePlayer.sendActionBar(this.formatSpectatorTip(this.player, this.target, true) + "  §aLeft Click to open the menu  §cPress Shift to Quit");
            return;
        }
        if (!this.player.getWorld().equals(this.target.getWorld())) {
            this.gamePlayer.sendActionBar("§c§lNone Target");
            return;
        }
        this.gamePlayer.sendActionBar(this.formatSpectatorTip(this.player, this.target, false));
    }

    public void autoTp() {
        if (!this.check()) {
            return;
        }
        if (SpectatorSettings.get(this.gamePlayer).getOption(SpectatorSettings.Option.AUTOTP) && (!this.player.getWorld().equals(this.target.getWorld()) || this.player.getLocation().distance(this.target.getLocation()) >= 20.0)) {
            this.player.teleport(this.target);
            if (SpectatorSettings.get(this.gamePlayer).getOption(SpectatorSettings.Option.FIRSTPERSON)) {
                this.gamePlayer.sendTitle("§aNow Spectating §7" + this.target.getName(), "§aLeft Click to open the menu  §cPress Shift to Quit", 0, 20, 0);
                this.player.setGameMode(GameMode.SPECTATOR);
                this.player.setSpectatorTarget(this.target);
            }
        }
    }

    public void tp() {
        if (!this.check()) {
            return;
        }
        if (SpectatorSettings.get(this.gamePlayer).getOption(SpectatorSettings.Option.FIRSTPERSON)) {
            this.player.teleport(this.target);
            this.gamePlayer.sendTitle("§aNow Spectating§7" + this.target.getName(), "§aLeft Click to open the menu  §cPress Shift to Quit", 0, 20, 0);
            this.player.setGameMode(GameMode.SPECTATOR);
            this.player.setSpectatorTarget((Entity) this.target);
            return;
        }
        this.player.teleport((Entity) this.target);
    }

    public boolean check() {
        if (this.gameTarget == null || this.target == null) {
            return false;
        }
        if (this.gameTarget.isSpectator() || !this.target.isOnline()) {
            this.gameTarget = null;
            this.target = null;
            return false;
        }
        return true;
    }

    private String formatSpectatorTip(Player player, Player target, boolean firstMode) {
        GamePlayer targetPlayer = GamePlayer.get(target.getUniqueId());
        if (firstMode) {
            return "§fTarget: §a§l" + target.getName() + "  §fHealths: §a§l" + (int) target.getHealth() + " §c§l❤  §fClass: §a§l" + targetPlayer.getPlayerStats().getSelected().getDisplayName();
        }
        String distance = this.df.format(player.getLocation().distance(target.getLocation()));
        return "§fTarget: §a§l" + target.getName() + "  §fHealths: §a§l" + (int) target.getHealth() + " §c§l❤  §fClass: §a§l" + targetPlayer.getPlayerStats().getSelected().getDisplayName() + "  §fDistance: §a§l" + distance + "m";
    }
}

