package cyan.thegoodboys.megawalls.game.stage;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.WallFallEvent;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameType;
import cyan.thegoodboys.megawalls.game.GameWall;
import cyan.thegoodboys.megawalls.timer.ScoreBoardTimer;
import cyan.thegoodboys.megawalls.timer.WitherSpecialAttackTimer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffectType;

public class WallFallStage extends GameStage {
    public WallFallStage() {
        super("战墙倒塌", (MegaWalls.getInstance().getGame().getGameType() == GameType.NORMAL ? 6 : 10) * 60, 0, 1);
    }

    public void excute(final Game game) {

        for (GameWall gameWall : game.getWalls()) {
            gameWall.falldown();
        }

        Bukkit.getPluginManager().callEvent(new WallFallEvent(game));
        game.setWallsFall(true);
        game.setWitherAngry(true);
        game.getStageManager().setCurrentStage(2);
        game.broadcastMessage("§e§l巨墙倒下了！§c§l开始战斗！");
        game.broadcastTitle("§c巨墙倒塌！", "§e准备战斗！", 10, 40, 10);
        game.broadcastSound(Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
        ScoreBoardTimer.scoreboards.clear();
        Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), new WitherSpecialAttackTimer(game), 0L, 20L);
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), game::registerScoreboardTeams, 30L);
        for (GamePlayer gamePlayer : MegaWalls.getIngame()) {
            if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                gamePlayer.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
            }
        }
        game.getTeams().forEach(gameTeam -> {
            if (gameTeam.getAlivePlayers().isEmpty()) {
                gameTeam.getTeamWither().setHealth(-999999999.99999999f);
                gameTeam.setWitherDead(true);
            }
        });
    }

    public void excuteLeftSeconds(Game game, int left) {
    }
}
