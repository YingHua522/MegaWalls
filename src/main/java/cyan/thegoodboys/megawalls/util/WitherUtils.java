package cyan.thegoodboys.megawalls.util;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class WitherUtils {


    public static void manipulateGoals(EntityInsentient entity, Predicate<Object> goalPredicate) {
        try {
            Field goalSelectorField = EntityInsentient.class.getDeclaredField("goalSelector");
            goalSelectorField.setAccessible(true);
            PathfinderGoalSelector goalSelector = (PathfinderGoalSelector) goalSelectorField.get(entity);

            Field goalListField = PathfinderGoalSelector.class.getDeclaredField("b");
            goalListField.setAccessible(true);
            List<?> goalList = (List<?>) goalListField.get(goalSelector);

            Iterator<?> it = goalList.iterator();
            while (it.hasNext()) {
                Object goalItem = it.next();
                Field goalField = goalItem.getClass().getDeclaredField("a");
                goalField.setAccessible(true);
                Object goal = goalField.get(goalItem);

                if (goalPredicate.test(goal)) {
                    it.remove();
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void manipulateTargets(EntityInsentient entity, Predicate<Object> targetPredicate) {
        try {
            Field targetSelectorField = EntityInsentient.class.getDeclaredField("targetSelector");
            targetSelectorField.setAccessible(true);
            PathfinderGoalSelector targetSelector = (PathfinderGoalSelector) targetSelectorField.get(entity);

            Field targetListField = PathfinderGoalSelector.class.getDeclaredField("b");
            targetListField.setAccessible(true);
            List<?> targetList = (List<?>) targetListField.get(targetSelector);

            Iterator<?> it = targetList.iterator();
            while (it.hasNext()) {
                Object targetItem = it.next();
                Field targetField = targetItem.getClass().getDeclaredField("a");
                targetField.setAccessible(true);
                Object target = targetField.get(targetItem);

                if (targetPredicate.test(target)) {
                    it.remove();
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void WitherEffect(EntityInsentient entity) {
        if (entity.getBukkitEntity().isDead()) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < 3; i++) {
                // 在半径为2的圆上随机生成位置
                double randomAngle = 2 * Math.PI * Math.random();
                double x = entity.getBukkitEntity().getLocation().getX() + 1.5 * Math.cos(randomAngle);
                double z = entity.getBukkitEntity().getLocation().getZ() + 1.5 * Math.sin(randomAngle);
                double y = entity.getBukkitEntity().getLocation().getY() + 2.3f + (entity.getWorld().random.nextFloat() * 1.1F);
                PacketPlayOutWorldParticles particlePacket = new PacketPlayOutWorldParticles(
                        EnumParticle.SPELL_MOB, true, (float) x, (float) y, (float) z, 0.699999988079071f, 0.699999988079071f, 0.8999999761581421f, 0, 1);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particlePacket);
            }
        }
    }

    public static void setEntityWitherAK(EntityLiving entity) {
        try {
            Field bodyRotationField = EntityLiving.class.getDeclaredField("aK");
            bodyRotationField.setAccessible(true);
            bodyRotationField.set(entity, 0.0F);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void removeAllGoals(EntityInsentient entity) {
        try {
            Field goalSelectorField = EntityInsentient.class.getDeclaredField("goalSelector");
            goalSelectorField.setAccessible(true);
            PathfinderGoalSelector goalSelector = (PathfinderGoalSelector) goalSelectorField.get(entity);

            Field targetSelectorField = EntityInsentient.class.getDeclaredField("targetSelector");
            targetSelectorField.setAccessible(true);
            PathfinderGoalSelector targetSelector = (PathfinderGoalSelector) targetSelectorField.get(entity);

            Field goalListField = PathfinderGoalSelector.class.getDeclaredField("b");
            goalListField.setAccessible(true);
            List<?> goalList = (List<?>) goalListField.get(goalSelector);
            goalList.clear();

            List<?> targetList = (List<?>) goalListField.get(targetSelector);
            targetList.clear();

            Field goalListFields = PathfinderGoalSelector.class.getDeclaredField("c");
            goalListFields.setAccessible(true);
            List<?> goalLists = (List<?>) goalListFields.get(goalSelector);
            goalLists.clear();

            List<?> targetLists = (List<?>) goalListFields.get(targetSelector);
            targetLists.clear();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void removePathfinderGoalHurtByTargets(EntityInsentient entity) {
        manipulateTargets(entity, goal -> goal instanceof PathfinderGoalHurtByTarget);
    }

    public static void removePathfinderGoalNearestAttackableTargets(EntityInsentient entity) {
        manipulateTargets(entity, goal -> goal instanceof PathfinderGoalNearestAttackableTarget);
    }

    public static void removeEntityWitherRandomMove(EntityInsentient entity) {
        manipulateGoals(entity, goal -> goal instanceof PathfinderGoalRandomStroll);
    }

    public static void removeEntityPlayer(EntityInsentient entity) {
        manipulateGoals(entity, goal -> goal instanceof PathfinderGoalLookAtPlayer);
    }

    public static void removeEntityAttack(EntityInsentient entity) {
        manipulateGoals(entity, goal -> goal instanceof PathfinderGoalArrowAttack);
    }

    public static void removeEntityAI(EntityInsentient entity) {
        manipulateGoals(entity, goal -> goal instanceof PathfinderGoalFloat);
    }


    public static void removeRandomStroll(EntityInsentient entity) {
        manipulateGoals(entity, goal -> goal instanceof PathfinderGoalRandomStroll);
    }

    public static void removeRandomLook(EntityInsentient entity) {
        manipulateGoals(entity, goal -> goal instanceof PathfinderGoalRandomLookaround);
    }

}
