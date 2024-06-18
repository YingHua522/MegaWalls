package cyan.thegoodboys.megawalls.classes.normal.creeper;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.util.StringUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SecondSkill extends Skill {
    public SecondSkill(Classes classes) {
        super("分裂之心", classes);
    }

    @Override
    public int maxedLevel() {
        return 3;
    }

    @Override
    public double getAttribute(int level) {
        switch (level) {
            case 1: {
                return 0.62;
            }
            case 2: {
                return 0.75;
            }
            case 3: {
                return 0.87;
            }
        }
        return 0.3333;
    }

    public int getDamage(int level) {
        switch (level) {
            case 1: {
                return 3;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 1;
            }
        }
        return 3;
    }

    @Override
    public List<String> getInfo(int level) {
        ArrayList<String> lore = new ArrayList<>();
        if (level == 1) {
            lore.add(" §8▪ §7被击杀时,有§a" + StringUtils.percent(this.getAttribute(level)) + "§7几率");
            lore.add("   §7生成一个爬行者。");
            lore.add("   §7放置一个速爆TNT并且造成§a" + this.getDamage(level));
            lore.add("   §7点伤害但不破坏方块。");
            return lore;
        }
        lore.add(" §8▪ §7被击杀时,有§8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ §a" + StringUtils.percent(this.getAttribute(level)) + "§7几率");
        lore.add("   §7生成一个爬行者。");
        lore.add("   §7放置一个速爆TNT并且造成§8" + this.getDamage(level - 1) + " ➜ §a" + this.getDamage(level));
        lore.add("   §7点伤害但不破坏方块。");
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

    @Override
    public boolean use(GamePlayer gamePlayer, KitStatsContainer kitStats) {
        int deatch = getDamage(kitStats.getSkill2Level());
        if (!gamePlayer.getGameTeam().isWitherDead()) {
            for (int i = 0; i < deatch; i++) {
                // 获取Creeper实体
                org.bukkit.entity.Creeper creeperEntity = (org.bukkit.entity.Creeper) gamePlayer.getPlayer().getWorld().spawnEntity(gamePlayer.getPlayer().getLocation(), EntityType.CREEPER);
                // 给Creeper实体添加药水效果
                creeperEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3, false, false));
            }
        } else {
            org.bukkit.entity.Creeper creeperEntity = (org.bukkit.entity.Creeper) gamePlayer.getPlayer().getWorld().spawnEntity(gamePlayer.getPlayer().getLocation(), EntityType.CREEPER);
            creeperEntity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3, false, false));
        }
        return true;
    }
}

