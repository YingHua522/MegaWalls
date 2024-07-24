package cyan.thegoodboys.megawalls.listener;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.classes.ClassesManager;
import cyan.thegoodboys.megawalls.classes.mythic.automaton.Automaton;
import cyan.thegoodboys.megawalls.game.FakePlayer;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.GameTeam;
import cyan.thegoodboys.megawalls.game.team.TeamWither;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWither;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.*;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EntityListener extends BaseListener {
    private final Game game;

    public EntityListener(MegaWalls plugin) {
        super(plugin);
        this.game = plugin.getGame();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent e) {
        for (LivingEntity entity : e.getAffectedEntities()) {
            if (!(entity instanceof Player) || Objects.requireNonNull(GamePlayer.get(entity.getUniqueId())).isSpectator()) {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (!this.game.isStarted()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void damagebyDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof TeamWither)) {
            return;
        }
        if (event.getDamager() instanceof Arrow || event.getDamager() instanceof WitherSkull) {
            event.setCancelled(true);
        }
    }



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (!this.game.isStarted() || e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
            if (e.getEntity().hasMetadata(MegaWalls.getMetadataValue())) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityTarget(EntityTargetEvent e) {
        if (!this.game.isStarted() || this.game.isOver() || e.getTarget() instanceof Player && GamePlayer.get(e.getTarget().getUniqueId()).isSpectator()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent e) {
        if (!this.game.isStarted() || this.game.isOver()) {
            e.setCancelled(true);
            return;
        }
        if (this.game.isStarted()) {
            ArrayList<Block> toRemove = new ArrayList<>();
            for (Block block : e.blockList()) {
                if (!this.game.isProtected(block.getLocation()) && !this.game.isUnbreakable(block.getLocation()))
                    continue;
                toRemove.add(block);
            }
            for (Block block : e.blockList()) {
                if (!e.getEntity().hasMetadata("cretnt")) continue;
                toRemove.add(block);
            }
            e.blockList().removeAll(toRemove);
            if (e.getEntity() instanceof Player) {
                Player player = (Player) e.getEntity();
                GamePlayer gp = GamePlayer.get(player.getUniqueId());
                if (gp != null) {
                    if (e.getEntity() instanceof WitherSkull && e.getEntity().hasMetadata(MegaWalls.getMetadataValue())) {
                        List<MetadataValue> metadataValues = e.getEntity().getMetadata(MegaWalls.getMetadataValue());
                        for (MetadataValue value : metadataValues) {
                            if (value.getOwningPlugin().equals(MegaWalls.getInstance()) && value.value() instanceof GameTeam) {
                                GameTeam gameTeam = (GameTeam) value.value();
                                if (gameTeam.equals(gp.getGameTeam())) {
                                    e.blockList().clear();
                                }
                            }
                        }
                    }
                }
            }
            if (e.getEntity().hasMetadata(MegaWalls.getMetadataValue()) && e.getEntity() instanceof TNTPrimed) {
                e.blockList().clear();
                e.setYield(0.5F);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNPCClick(NPCLeftClickEvent e) {
        GamePlayer gamePlayer = GamePlayer.get(e.getClicker().getUniqueId());
        if (gamePlayer != null && gamePlayer.isSpectator()) {
            return;
        }
        FakePlayer fakePlayer = FakePlayer.getFakePlayer(e.getNPC());
        if (fakePlayer != null) {
            GamePlayer player = fakePlayer.getGamePlayer();
            if (gamePlayer != null && gamePlayer.getGameTeam().isInTeam(player)) {
                e.setCancelled(true);
                return;
            }
            for (ItemStack itemStack : fakePlayer.getDrops()) {
                e.getClicker().getWorld().dropItem(fakePlayer.getLocation(), itemStack);
            }
            if (gamePlayer != null) {
                gamePlayer.handleKill(player, e.getNPC().getEntity());
            }
            fakePlayer.setKilled(true);
            fakePlayer.delete();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
            return;
        }
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            GamePlayer gamePlayer = GamePlayer.get(e.getEntity().getUniqueId());
            assert gamePlayer != null;
            if (gamePlayer.isSpectator()) {
                e.setCancelled(true);
                if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                    gamePlayer.getSpectatorTarget().tp();
                }
                return;
            }
            if (gamePlayer.isProtect()) {
                e.setCancelled(true);
                return;
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                e.setCancelled(true);
                return;
            }
            if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                e.setCancelled(true);
                return;
            }
            if (this.game.isWaiting() || !this.game.isStarted()) {
                if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    player.teleport(this.game.getLobbyLocation());
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }

        UUID damagerId = e.getDamager().getUniqueId();
        UUID entityId = e.getEntity().getUniqueId();
        GamePlayer damager = GamePlayer.get(damagerId);
        GamePlayer gamePlayer = GamePlayer.get(entityId);

        if (e.getDamager().hasMetadata("cretnt")) {
            Player player = Bukkit.getPlayer(e.getDamager().getCustomName());
            if (gamePlayer == null || player == null || !gamePlayer.getGameTeam().isInTeam(GamePlayer.get(player.getUniqueId()))) {
                e.setDamage(e.getDamage() * 0.5);
            } else if (player == gamePlayer.getPlayer()) {
                e.setDamage(e.getDamage() * 0.5);
            }
            return;
        }

        if (e.getDamager().hasMetadata("CREEPER") || e.getDamager().hasMetadata("DREAD") || e.getDamager().hasMetadata("REN")) {
            e.setCancelled(true);
            return;
        }

        if (e.getDamager() instanceof Player) {
            if (damager != null && damager.isSpectator()) {
                e.setCancelled(true);
                return;
            }
            if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
                return;
            }
            if (e.getEntity() instanceof Player) {
                GameTeam team = gamePlayer.getGameTeam();
                if (!this.game.isStarted()) {
                    return;
                }
                if (gamePlayer.isSpectator() || gamePlayer.isProtect() || gamePlayer.getPlayer().getNoDamageTicks() > 10) {
                    e.setCancelled(true);
                    return;
                }
                if (team != null && team.isInTeam(damager)) {
                    e.setCancelled(true);
                    return;
                }
                int damage = (int) e.getFinalDamage();
                damager.addDamage(damage);
                gamePlayer.addDef(damage);
                double energyMelee = damager.getPlayerStats().getSelected().energyMelee();
                damager.addEnergy(ClassesManager.getSelected(gamePlayer) instanceof Automaton ? (int) (damager.getPlayerStats().getSelected().energyMelee() * 0.75) : damager.getPlayerStats().getSelected().energyMelee(), PlayerEnergyChangeEvent.ChangeReason.MELLEE);
                if (ClassesManager.getSelected(gamePlayer) instanceof Automaton) {
                    gamePlayer.addEnergy((int) (energyMelee * 0.25), PlayerEnergyChangeEvent.ChangeReason.MELLEE);
                }
                if (Automaton.skill2.getOrDefault(gamePlayer, 0) <= 45) {
                    Automaton.skill2.put(gamePlayer, Math.min(Automaton.skill2.getOrDefault(gamePlayer, 0) + (int) (energyMelee * 0.25), 45));
                }
            } else if (e.getEntity().hasMetadata(MegaWalls.getMetadataValue())) {
                if (!(e.getEntity().getMetadata(MegaWalls.getMetadataValue()).get(0).value() instanceof GameTeam)) {
                    return;
                }
                GameTeam gameTeam = (GameTeam) e.getEntity().getMetadata(MegaWalls.getMetadataValue()).get(0).value();
                if (gameTeam.isInTeam(GamePlayer.get(damagerId))) {
                    e.setCancelled(true);
                    return;
                }
            }
        } else if (e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            if (projectile.getShooter() instanceof Player) {
                GamePlayer shooter = GamePlayer.get(((Player) projectile.getShooter()).getUniqueId());
                if (shooter != null && shooter.isSpectator()) {
                    e.setCancelled(true);
                    return;
                }
                if (e.getEntity() instanceof Player) {
                    if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
                        return;
                    }
                    if (gamePlayer != null) {
                        GameTeam team = gamePlayer.getGameTeam();
                        if (!this.game.isStarted()) {
                            return;
                        }
                        if (gamePlayer.isSpectator()) {
                            e.setCancelled(true);
                            return;
                        }
                        if (gamePlayer.isProtect()) {
                            e.setCancelled(true);
                            return;
                        }
                        if (team != null && team.isInTeam(shooter)) {
                            e.setCancelled(true);
                            return;
                        }
                        if (projectile.getType() == EntityType.ARROW) {
                            Player player = gamePlayer.getPlayer();
                            double health = player.getHealth();
                            double damage = e.getFinalDamage();
                            if (!player.isDead()) {
                                final double ahp = ((CraftPlayer) player).getHandle().getAbsorptionHearts();
                                Double realHealth = (health + ahp) - (int) damage;
                                NumberFormat nf = NumberFormat.getInstance();
                                nf.setMaximumFractionDigits(1);
                                if (realHealth.intValue() > 0) {
                                    if (shooter != null) {
                                        shooter.addDamage((int) damage);
                                    }
                                    gamePlayer.addDef((int) damage);
                                    shooter.sendMessage("\u00a77" + player.getDisplayName() + " \u00a7eis on \u00a7c" + nf.format(realHealth) + " \u00a7eHP!");
                                }
                            }
                        }
                        gamePlayer.setLastDamage(shooter, System.currentTimeMillis());
                        if (!projectile.hasMetadata("NoAddEnergy!")) {
                            if (shooter != null) {
                                shooter.addEnergy(ClassesManager.getSelected(gamePlayer) instanceof Automaton ? (int) (shooter.getPlayerStats().getSelected().energyBow() * 0.75) : shooter.getPlayerStats().getSelected().energyBow(), PlayerEnergyChangeEvent.ChangeReason.BOW);
                            }
                            if (Automaton.skill2.getOrDefault(gamePlayer, 0) <= 45) {
                                if (shooter != null) {
                                    Automaton.skill2.put(gamePlayer, Math.min(Automaton.skill2.getOrDefault(gamePlayer, 0) + (int) (shooter.getPlayerStats().getSelected().energyMelee() * 0.25), 45));
                                }
                            }
                            if (ClassesManager.getSelected(gamePlayer) instanceof Automaton) {
                                if (shooter != null) {
                                    gamePlayer.addEnergy((int) (shooter.getPlayerStats().getSelected().energyBow() * 0.25), PlayerEnergyChangeEvent.ChangeReason.BOW);
                                }
                            }
                        }
                    } else if (e.getEntity().hasMetadata(MegaWalls.getMetadataValue())) {
                        if (!(e.getEntity().getMetadata(MegaWalls.getMetadataValue()).get(0).value() instanceof GameTeam)) {
                            return;
                        }
                        GameTeam gameTeam = (GameTeam) e.getEntity().getMetadata(MegaWalls.getMetadataValue()).get(0).value();
                        if (gameTeam.isInTeam(GamePlayer.get(e.getDamager().getUniqueId()))) {
                            e.setCancelled(true);
                        }
                    }
                } else if (projectile.getShooter() instanceof Wither) {
                    Wither wither = (Wither) projectile.getShooter();
                    TeamWither teamWither = (TeamWither) ((CraftWither) wither).getHandle();
                    if (teamWither.getGameTeam().isInTeam(GamePlayer.get(e.getEntity().getUniqueId()))) {
                        e.setCancelled(true);
                    }
                } else if (projectile.getShooter() instanceof Entity && e.getEntity() instanceof Player && ((Entity) projectile.getShooter()).hasMetadata(MegaWalls.getMetadataValue())) {
                    if (!(((Entity) projectile.getShooter()).getMetadata(MegaWalls.getMetadataValue()).get(0).value() instanceof GameTeam)) {
                        return;
                    }
                    GameTeam gameTeam = (GameTeam) ((Entity) projectile.getShooter()).getMetadata(MegaWalls.getMetadataValue()).get(0).value();
                    if (gameTeam.isInTeam(GamePlayer.get(e.getEntity().getUniqueId()))) {
                        e.setCancelled(true);
                    }
                }
            } else if (e.getDamager().hasMetadata(MegaWalls.getMetadataValue()) && e.getEntity() instanceof Player) {
                if (!(e.getDamager().getMetadata(MegaWalls.getMetadataValue()).get(0).value() instanceof GameTeam)) {
                    return;
                }
                GameTeam gameTeam = (GameTeam) e.getDamager().getMetadata(MegaWalls.getMetadataValue()).get(0).value();
                if (gameTeam.isInTeam(GamePlayer.get(e.getEntity().getUniqueId()))) {
                    e.setCancelled(true);
                }
            } else if (e.getDamager() instanceof TNTPrimed) {
                TNTPrimed tnt = (TNTPrimed) e.getDamager();
                if (tnt.hasMetadata("SKELETON")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntitys(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Wither) {
            event.setCancelled(true);
        }
    }
}

