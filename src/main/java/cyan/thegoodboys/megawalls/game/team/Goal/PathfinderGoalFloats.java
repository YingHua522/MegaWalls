package cyan.thegoodboys.megawalls.game.team.Goal;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PathfinderGoal;

public class PathfinderGoalFloats extends PathfinderGoal {
    private final EntityInsentient a;

    public PathfinderGoalFloats(EntityInsentient entityInsentient) {
        this.a = entityInsentient;
        this.a(4);
        ((Navigation)entityInsentient.getNavigation()).d(true);
    }

    public boolean a() {
        return this.a.V() || this.a.ab();
    }

    public void e() {
        this.a.getNavigation().n();
    }
}
