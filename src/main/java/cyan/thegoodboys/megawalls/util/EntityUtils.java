package cyan.thegoodboys.megawalls.util;

import cyan.thegoodboys.megawalls.game.team.TeamWither;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class EntityUtils {

    private static TeamWither teamWither;

    public EntityUtils(TeamWither teamWither) {
        EntityUtils.teamWither = teamWither;
    }

    public static void manipulateGoals(Predicate<Object> goalPredicate) {
        try {
            Field goalSelectorField = EntityInsentient.class.getDeclaredField("goalSelector");
            goalSelectorField.setAccessible(true);
            PathfinderGoalSelector goalSelector = (PathfinderGoalSelector) goalSelectorField.get(teamWither);

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

    public static void manitargetGoals(Predicate<Object> goalPredicate) {
        try {
            Field goalSelectorField = EntityInsentient.class.getDeclaredField("targetSelector");
            goalSelectorField.setAccessible(true);
            PathfinderGoalSelector goalSelector = (PathfinderGoalSelector) goalSelectorField.get(teamWither);

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

    private void EntityLookPlayer(Player player) {
        try {
            Field field = EntityInsentient.class.getDeclaredField("lookController");
            field.setAccessible(true); // 设置为true以访问私有字段
            ControllerLook controllerLook = new ControllerLook(teamWither);
            Location location = player.getLocation();
            controllerLook.a(location.getX(),location.getY()+3,location.getZ(), 180F, 180.0F);
            field.set(this, controllerLook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void PathfinderGoalHurtByTargets() {
        manitargetGoals(goal -> goal instanceof PathfinderGoalHurtByTarget);
    }

    public static void PathfinderGoalNearestAttackableTargets() {
        manitargetGoals(goal -> goal instanceof PathfinderGoalNearestAttackableTarget);
    }

    public static void EntityWitherRandomMove() {
        manipulateGoals(goal -> goal instanceof PathfinderGoalRandomStroll);
    }

    public static void EntityPlayer() {
        manipulateGoals(goal -> goal instanceof PathfinderGoalLookAtPlayer);
    }

    public static void EntityAttack() {
        manipulateGoals(goal -> goal instanceof PathfinderGoalArrowAttack);
    }

    public static void EntityAI() {
        manipulateGoals(goal -> goal instanceof PathfinderGoalFloat);
    }

    public static void RandomStroll() {
        manipulateGoals(goal -> goal instanceof PathfinderGoalRandomStroll);
    }

    public static void EntityLook() {
        manipulateGoals(goal -> goal instanceof PathfinderGoalRandomLookaround);
    }
}
