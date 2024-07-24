package cyan.thegoodboys.megawalls.game.team;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameTeam;
import cyan.thegoodboys.megawalls.game.stage.BattleStage;
import cyan.thegoodboys.megawalls.reward.RewardManager;
import cyan.thegoodboys.megawalls.stats.CurrencyPackage;
import cyan.thegoodboys.megawalls.stats.EffectStatsContainer;
import cyan.thegoodboys.megawalls.util.PlayerUtils;
import lombok.Getter;
import net.citizensnpcs.api.CitizensAPI;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TeamWither extends EntityWither implements IRangedEntity {

    @Getter
    private final Game game = MegaWalls.getInstance().getGame();
    private final Map<GamePlayer, Float> damagerList = new HashMap<>();
    private int bp;
    private final float[] a = new float[2];
    private final float[] b = new float[2];
    private final float[] c = new float[2];
    private final float[] bm = new float[2];
    private GameTeam gameTeam = null;
    private boolean warning = false;
    private boolean deepRed = true;

    public TeamWither(World world) {
        super(world);
        this.a(0.9F, 4.0F);
        this.maxNoDamageTicks = 11;
        this.noDamageTicks = 11;
    }


    public void setTeam(GameTeam gameTeam) {
        if (gameTeam == null) {
            this.gameTeam = null;
        }
        if (gameTeam != null) {
            this.setCustomName(gameTeam.getTeamColor().getChatColor() + gameTeam.getTeamColor().getText() + "队凋灵");
        }
        this.setCustomNameVisible(true);
    }

    public GameTeam getGameTeam() {
        if (this.gameTeam != null) {
            return this.gameTeam;
        } else {
            String name = ChatColor.stripColor(this.getCustomName()).substring(0, 1);
            GameTeam gameTeam = this.game.getTeamByName(name);
            if (this.gameTeam == null) {
                this.gameTeam = gameTeam;
            }

            return gameTeam;
        }
    }

    @Override
    public EntityLiving getGoalTarget() {
        for (Player player : PlayerUtils.getNearbyPlayers(this.getBukkitEntity().getLocation(), 30.0D)) {
            if (!CitizensAPI.getNPCRegistry().isNPC(player)) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                if (gamePlayer != null && !gamePlayer.isSpectator() && !this.getGameTeam().isInTeam(gamePlayer) && game.isWallsFall()) {
                    return ((CraftPlayer) player).getHandle();
                }
            }
        }

        return null;
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(1024.0);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.6000000238418579);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(40.0);
    }


    protected void E() {
        int i;
        int j1;
        if (this.cl() > 0) {
            i = this.cl() - 1;
            if (i <= 0) {
                ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 7.0F, false);
                this.world.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    this.world.createExplosion(this, this.locX, this.locY + (double) this.getHeadHeight(), this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
                }

                j1 = this.world.getServer().getViewDistance() * 16;
                Iterator<EntityPlayer> var4 = MinecraftServer.getServer().getPlayerList().players.iterator();

                label65:
                while (true) {
                    EntityPlayer player;
                    double deltaX;
                    double deltaZ;
                    double distanceSquared;
                    do {
                        if (!var4.hasNext()) {
                            break label65;
                        }

                        player = var4.next();
                        deltaX = this.locX - player.locX;
                        deltaZ = this.locZ - player.locZ;
                        distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                    } while (this.world.spigotConfig.witherSpawnSoundRadius > 0 && distanceSquared > (double) (this.world.spigotConfig.witherSpawnSoundRadius * this.world.spigotConfig.witherSpawnSoundRadius));

                    if (distanceSquared > (double) (j1 * j1)) {
                        double deltaLength = Math.sqrt(distanceSquared);
                        double relativeX = player.locX + deltaX / deltaLength * (double) j1;
                        double relativeZ = player.locZ + deltaZ / deltaLength * (double) j1;
                        player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1013, new BlockPosition((int) relativeX, (int) this.locY, (int) relativeZ), 0, true));
                    } else {
                        player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1013, new BlockPosition((int) this.locX, (int) this.locY, (int) this.locZ), 0, true));
                    }
                }
            }

            this.r(i);
            if (this.ticksLived % 10 == 0) {
                this.heal(10.0F, EntityRegainHealthEvent.RegainReason.WITHER_SPAWN);
            }
        } else {
            if (this.getGoalTarget() != null) {
                this.b(0, this.getGoalTarget().getId());
            } else {
                this.b(0, 0);
            }

            if (this.bp > 0) {
                --this.bp;
                if (this.bp == 0 && this.world.getGameRules().getBoolean("mobGriefing")) {
                    i = MathHelper.floor(this.locY);
                    int j = MathHelper.floor(this.locX);
                    j1 = MathHelper.floor(this.locZ);
                    boolean flag = false;
                    int k1 = -1;

                    while (true) {
                        if (k1 > 1) {
                            if (flag) {
                                this.world.a(null, 1012, new BlockPosition(this), 0);
                            }
                            break;
                        }

                        for (int l1 = -1; l1 <= 1; ++l1) {
                            for (int i2 = 0; i2 <= 3; ++i2) {
                                int j2 = j + k1;
                                int k2 = i + i2;
                                int l2 = j1 + l1;
                                BlockPosition blockposition = new BlockPosition(j2, k2, l2);
                                Block block = this.world.getType(blockposition).getBlock();
                                if (block.getMaterial() != Material.AIR && a(block) && !CraftEventFactory.callEntityChangeBlockEvent(this, j2, k2, l2, Blocks.AIR, 0).isCancelled()) {
                                    flag = this.world.setAir(blockposition, true) || flag;
                                }
                            }
                        }

                        ++k1;
                    }
                }
            }

            if (this.ticksLived % 20 == 0 && this.getHealth() < 1) {
                this.heal(1.0F, EntityRegainHealthEvent.RegainReason.REGEN);
            }
        }
    }




    public void m() {
        this.motY *= 0.6000000238418579;
        double d0;
        double d1;
        double d2;
        if (!this.world.isClientSide && this.s(0) > 0) {
            Entity entity = this.world.a(this.s(0));
            if (entity != null) {
                if (this.locY < entity.locY || !this.cm() && this.locY < entity.locY + 5.0) {
                    if (this.motY < 0.0) {
                        this.motY = 0.0;
                    }

                    this.motY += (0.5 - this.motY) * 0.6000000238418579;
                }

                double d3 = entity.locX - this.locX;
                d0 = entity.locZ - this.locZ;
                d1 = d3 * d3 + d0 * d0;
                if (d1 > 9.0) {
                    d2 = MathHelper.sqrt(d1);
                    this.motX += (d3 / d2 * 0.5 - this.motX) * 0.6000000238418579;
                    this.motZ += (d0 / d2 * 0.5 - this.motZ) * 0.6000000238418579;
                }
            }
        }

        if (this.motX * this.motX + this.motZ * this.motZ > 0.05000000074505806) {
            this.yaw = (float)MathHelper.b(this.motZ, this.motX) * 57.295776F - 90.0F;
        }
        super.m();
        int i;
        for(i = 0; i < 2; ++i) {
            this.bm[i] = this.b[i];
            this.c[i] = this.a[i];
        }

        int j;
        double d8;
        double d9;
        double d10;
        for(i = 0; i < 2; ++i) {
            j = this.s(i + 1);
            Entity entity1 = null;
            if (j > 0) {
                entity1 = this.world.a(j);
            }

            if (entity1 != null) {
                d0 = this.t(i + 1);
                d1 = this.u(i + 1);
                d2 = this.v(i + 1);
                d8 = entity1.locX - d0;
                d9 = entity1.locY + (double)entity1.getHeadHeight() - d1;
                d10 = entity1.locZ - d2;
                double d7 = MathHelper.sqrt(d8 * d8 + d10 * d10);
                float f = (float)(MathHelper.b(d10, d8) * 180.0 / 3.1415927410125732) - 90.0F;
                float f1 = (float)(-(MathHelper.b(d9, d7) * 180.0 / 3.1415927410125732));
                this.a[i] = this.b(this.a[i], f1, 40.0F);
                this.b[i] = this.b(this.b[i], f, 10.0F);
            } else {
                this.b[i] = this.b(this.b[i], this.aI, 10.0F);
            }
        }

        boolean flag = this.cm();

        for(j = 0; j < 3; ++j) {
            d8 = this.t(j);
            d9 = this.u(j);
            d10 = this.v(j);
            for (Player player : Bukkit.getOnlinePlayers()) {
                // 检查玩家是否在实体10格外
                if (player.getLocation().distance(this.getBukkitEntity().getLocation()) >= 15) {
                    continue;
                }
                // 在半径为2的圆上随机生成位置
                PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles(
                        EnumParticle.SPELL_MOB, true, (float) ((float) d8 + this.random.nextGaussian() * 0.30000001192092896), (float) ((float) d9 + this.random.nextGaussian() * 0.30000001192092896), (float) ((float) d10 + this.random.nextGaussian() * 0.30000001192092896), 0.699999988079071f, 0.699999988079071f, 0.8999999761581421f, 0, 1);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particlePacket);
            }
            this.world.addParticle(EnumParticle.SMOKE_NORMAL, d8 + this.random.nextGaussian() * 0.30000001192092896, d9 + this.random.nextGaussian() * 0.30000001192092896, d10 + this.random.nextGaussian() * 0.30000001192092896, 0.0, 0.0, 0.0);
            if (flag && this.world.random.nextInt(4) == 0) {
                this.world.addParticle(EnumParticle.SPELL_MOB, d8 + this.random.nextGaussian() * 0.30000001192092896, d9 + this.random.nextGaussian() * 0.30000001192092896, d10 + this.random.nextGaussian() * 0.30000001192092896, 0.699999988079071, 0.699999988079071, 0.5);
            }
        }

        if (this.cl() > 0) {
            for(j = 0; j < 3; ++j) {
                this.world.addParticle(EnumParticle.SPELL_MOB, this.locX + this.random.nextGaussian(), this.locY + (double)(this.random.nextFloat() * 3.3F), this.locZ + this.random.nextGaussian(), 0.699999988079071, 0.699999988079071, 0.8999999761581421);
            }
        }
        if (gameTeam != null && gameTeam.getWitherLocation() != null && this.getBukkitEntity().getLocation().distance(gameTeam.getWitherLocation()) > 15.0D) {
            this.getBukkitEntity().teleport(gameTeam.getWitherLocation());
        }
        this.aK = 0.0f;
    }

    private float b(float f, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f);
        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    private double t(int i) {
        if (i <= 0) {
            return this.locX;
        } else {
            float f = (this.aI + (float)(180 * (i - 1))) / 180.0F * 3.1415927F;
            float f1 = MathHelper.cos(f);
            return this.locX + (double)f1 * 1.3;
        }
    }

    private double u(int i) {
        return i <= 0 ? this.locY + 3.0 : this.locY + 2.2;
    }

    private double v(int i) {
        if (i <= 0) {
            return this.locZ;
        } else {
            float f = (this.aI + (float)(180 * (i - 1))) / 180.0F * 3.1415927F;
            float f1 = MathHelper.sin(f);
            return this.locZ + (double)f1 * 1.3;
        }
    }


    @Override
    public void move(double d0, double d1, double d2) {
        // 如果玩家不在附近，那么就不执行任何操作
//        if (!this.world.isPlayerNearby(locX,locY,locZ,2.0) && this.passenger == null && this.vehicle == null && this.world.getCubes(this, this.getBoundingBox().grow(d0, d1, d2)).isEmpty()){
//            super.move(d0, d1, d2);
//        } else {
//            super.move(0,0,0);
//        }
    }

    protected void dropDeathLoot(boolean flag, int i) {
    }

    public void die() {
        game.witherAlive--;
        GamePlayer.getOnlinePlayers().forEach((viewer) -> {
            EffectStatsContainer effectStats = viewer.getPlayerStats().getEffectStats();
            viewer.sendMessage(effectStats.getColor(this.getGameTeam().getTeamColor()).getChatColor() + this.getGameTeam().getTeamColor().getText() + "队凋灵§e死亡！");
            viewer.sendMessage(effectStats.getColor(this.getGameTeam().getTeamColor()).getChatColor() + this.getGameTeam().getTeamColor().getText() + "队§e玩家无法再复活");
        });
        this.getGameTeam().setWitherDead(true);
        this.game.broadcastSound(Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F, this.getGameTeam().getAlivePlayers().toArray(new GamePlayer[0]));
        this.game.broadcastMessage("§b凋灵已死,你无法再复活！", this.getGameTeam().getAlivePlayers().toArray(new GamePlayer[0]));
        this.game.broadcastTitle("§c你的凋灵死亡！", "§e你无法再复活了！", 5, 20, 5, this.getGameTeam().getAlivePlayers().toArray(new GamePlayer[0]));
        AtomicBoolean allDead = new AtomicBoolean(true);
        this.game.getTeams().forEach(gameTeam -> {
            if (!gameTeam.isWitherDead()) {
                allDead.set(false);
            }
        });

        if (!this.game.isDeathMatch() && allDead.get()) {
            ((BattleStage) this.game.getStageManager().getStage(3)).setCacheLeft(this.game.getStageManager().getSeconds());
            this.game.getStageManager().setSeconds(0);
            this.game.getStageManager().setCurrentStage(99);
            this.game.broadcastTitle("§c所有的凋灵都死了！", "§f死亡竞赛将在§b10§f秒后开启！", 10, 40, 10);
            this.game.broadcastMessage("§c§l所有的凋灵已经死亡！还有§b§l10§c§l秒开启死亡竞赛！");
        }

        this.damagerList.keySet().forEach(gamePlayer -> {
            ((BattleStage) this.game.getStageManager().getStage(3)).setCacheLeft(this.game.getStageManager().getSeconds());
            int coins = (int) (this.damagerList.getOrDefault(gamePlayer, 0.0F) / 2);
            gamePlayer.getPlayerStats().giveCoins(new CurrencyPackage(coins, "(凋灵伤害奖励)"));
            if (this.damagerList.getOrDefault(gamePlayer, 0.0F) >= 200.0F) {
                RewardManager.addChallenge(gamePlayer, 1, 1);
            }
        });
        super.die();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (damagesource != null && damagesource.getEntity() != null && damagesource.getEntity().getBukkitEntity() != null && damagesource.getEntity().getBukkitEntity() instanceof CraftPlayer) {
            if (damagesource.getEntity().getBukkitEntity() instanceof CraftArrow) {
                // 如果伤害来源是箭矢，那么不对凋灵造成伤害
                return false;
            }
            Player player = (Player) damagesource.getEntity().getBukkitEntity();
            GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
            if (gamePlayer == null || gamePlayer.isSpectator() || this.getGameTeam().isInTeam(gamePlayer)) {
                return false;
            }
            if (damagesource.equals(DamageSource.GENERIC)) {
                return super.damageEntity(damagesource, f);
            }

            if (CitizensAPI.getNPCRegistry().isNPC(player)) {
                return false;
            }


            if (!this.warning) {
                broadcastWitherUnderAttack();
                scheduleBroadcasts();
            }

            if (!this.game.isWallsFall()) {
                this.setHealth(this.getHealth());
                return super.damageEntity(damagesource, 0.0f);
            }

            this.damagerList.put(gamePlayer, this.damagerList.getOrDefault(gamePlayer, 0.0F) + f / 5.0F);
            super.damageEntity(damagesource, f / 5.0F);
        }
        return true;
    }

    @Override
    public void a(Entity entity, float f, double d0, double d1) {
        // 不执行任何操作，从而取消击退效果
    }

    private void broadcastWitherUnderAttack() {
        this.warning = true;
        this.deepRed = !this.deepRed;
        String messageColor = this.deepRed ? "§4" : "§c";
        String message = messageColor + "§l你的凋灵正在被攻击！";
        GamePlayer[] players = this.getGameTeam().getAlivePlayers().toArray(new GamePlayer[0]);

        this.game.broadcastMessage(message, players);
        this.game.broadcastTitle("", message, 0, 40, 0, players);
        this.game.broadcastAction(message, players);
        this.game.broadcastSound(Sound.NOTE_PLING, 1.0F, 2.0F, players);
    }

    private void scheduleBroadcasts() {
        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
            this.game.broadcastSound(Sound.NOTE_PLING, 1.0F, 3.0F, this.getGameTeam().getAlivePlayers().toArray(new GamePlayer[0]));
            this.game.broadcastAction((this.deepRed ? "§4" : "§c") + "§l你的凋灵正在被攻击！", this.getGameTeam().getAlivePlayers().toArray(new GamePlayer[0]));
        }, 11L);

        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> {
            this.game.broadcastSound(Sound.NOTE_PLING, 1.0F, 3.0F, this.getGameTeam().getAlivePlayers().toArray(new GamePlayer[0]));
            this.game.broadcastAction((this.deepRed ? "§4" : "§c") + "§l你的凋灵正在被攻击！", this.getGameTeam().getAlivePlayers().toArray(new GamePlayer[0]));
        }, 22L);

        Bukkit.getScheduler().runTaskLater(MegaWalls.getInstance(), () -> this.warning = false, 60L);
    }
}

