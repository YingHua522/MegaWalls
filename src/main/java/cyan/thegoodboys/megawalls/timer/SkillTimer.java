/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Sound
 */
package cyan.thegoodboys.megawalls.timer;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.mythic.automaton.Automaton;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ParticleEffect;
import org.bukkit.Material;
import org.bukkit.Sound;

public class SkillTimer implements Runnable {
    private int tick = 0;

    @Override
    public void run() {

        for (GamePlayer gamePlayer : MegaWalls.getIngame()) {
            if (gamePlayer.getEnergy() == (ClassesManager.getSelected(gamePlayer) instanceof Automaton ? 160 : 100)) {
                gamePlayer.getPlayer().setLevel(gamePlayer.getEnergy());
                gamePlayer.getPlayer().setExp(gamePlayer.getPlayer().getExp() >= 1.0f ? 0.0f : 1.0f);
                if (this.tick % 600 == 0) {
                    gamePlayer.sendMessage("§aYour §b§l" + gamePlayer.getPlayerStats().getSelected().getMainSkill().getName() + " §aSkill is ready!");
                    gamePlayer.sendMessage("§bClick§a your sword or bow to activate your skill!");
                    gamePlayer.playSound(Sound.LAVA_POP, 1.0f, 1.0f);
                }
                if (this.tick % 10 == 0 && gamePlayer.getPlayer().getInventory().getItemInHand().getType() == Material.IRON_SWORD || gamePlayer.getPlayer().getInventory().getItemInHand().getType() == Material.DIAMOND_SWORD || gamePlayer.getPlayer().getInventory().getItemInHand().getType() == Material.BOW) {
                    ParticleEffect.NOTE.display(0.0f, 0.0f, 0.0f, 1.0f, 1, gamePlayer.getPlayer().getLocation(), 10.0);
                }
                this.tick += 5;
                continue;
            }
            gamePlayer.getPlayer().setLevel(gamePlayer.getEnergy());
            gamePlayer.getPlayer().setExp((float) gamePlayer.getEnergy() / 100.0f);
        }
        this.tick += 5;
    }
}

