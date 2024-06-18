/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.Entity
 *  net.minecraft.server.v1_8_R3.World
 *  org.bukkit.Bukkit
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 */
package cyan.thegoodboys.megawalls.classes.normal.pirate;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("愤怒的小鸟", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 50.0;
            }
            case 2: {
                return 40.0;
            }
            case 3: {
                return 30.0;
            }
        }
        return 50.0;
    }


    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<String>();
        if (level == 1) {
            lore.add(" §8▪ §7每§a" + this.getAttribute(level) + "§7秒召唤一只鹦鹉。当你受到");
            lore.add("   §7攻击时,你的鹦鹉会在敌人处爆");
            lore.add("   §7炸并造成2点真实伤害。");
            return lore;
        }
        lore.add(" §8▪ §7每§8" + this.getAttribute(level - 1) + " ➜ §a" + this.getAttribute(level) + "§7秒召唤一只鹦鹉。当你受到");
        lore.add("   §7攻击时,你的鹦鹉会在敌人处爆");
        lore.add("   §7炸并造成2点真实伤害。");
        return lore;
    }

    @Override
    public void upgrade(GamePlayer gamePlayer) {
        KitStatsContainer kitStats = gamePlayer.getPlayerStats().getKitStats(this.getClasses());
        kitStats.addSkill2Level();
    }

    @Override
    public int getPlayerLevel(GamePlayer gamePlayer) {
        return gamePlayer.getPlayerStats().getKitStats(this.getClasses()).getSkill2Level();
    }

    public String getSkillTip(GamePlayer gamePlayer) {
        return this.getClasses().getNameColor() + "§l" + this.getName().toUpperCase() + " §7" + Pirate.bird.getOrDefault(gamePlayer, 0) + "/6" + " §c" + Pirate.skill2.getOrDefault(gamePlayer, 20);
    }

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        return Pirate.bird.getOrDefault(gamePlayer, (int) this.getAttribute(kitStats.getSkill2Level())) <= 0 && MegaWalls.getInstance().getGame().isWallsFall();
    }
}

