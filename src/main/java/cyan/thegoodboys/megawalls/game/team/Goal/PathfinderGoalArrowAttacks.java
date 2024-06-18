package cyan.thegoodboys.megawalls.game.team.Goal;

import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PathfinderGoalArrowAttacks extends PathfinderGoal {
    private final EntityInsentient a;
    private final IRangedEntity b;
    private EntityLiving c;
    private int d;
    private final double e;
    private final int g;
    private final int h;
    private final float i;
    private final float j;

    public PathfinderGoalArrowAttacks(IRangedEntity var1, double var2, int var4, float var5) {
        this(var1, var2, var4, var4, var5);
    }

    public PathfinderGoalArrowAttacks(IRangedEntity var1, double var2, int var4, int var5, float var6) {
        this.d = -1;
        if (!(var1 instanceof EntityLiving)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.b = var1;
            this.a = (EntityInsentient) var1;
            this.e = var2;
            this.g = var4;
            this.h = var5;
            this.i = var6;
            this.j = var6 * var6;
            this.a(3);
        }
    }

    public boolean a() {
        EntityLiving var1 = this.a.getGoalTarget();
        if (var1 == null) {
            return false;
        } else {
            this.c = var1;
            return true;
        }
    }

    public boolean b() {
        return this.a() || !this.a.getNavigation().m();
    }

    public void d() {
        this.c = null;
        this.d = -1;
    }

    public void e() {
        double var1 = this.a.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
        this.a.getNavigation().a(this.c, this.e);
        for (Player player : PlayerUtils.getNearbyPlayers(this.a.getBukkitEntity().getLocation(), 25.0D)) {
            Location location = player.getLocation();
            this.a.getControllerLook().a(location.getX(),location.getY() +3.0,location.getZ(), 165.0F, 180.0F);
        }
        float var4;
        if (--this.d == 0) {
            if (var1 > (double) this.j) {
                return;
            }

            var4 = MathHelper.sqrt(var1) / this.i;
            float var5 = MathHelper.a(var4, 0.1F, 1.0F);
            this.b.a(this.c, var5);
            this.d = MathHelper.d(var4 * (float) (this.h - this.g) + (float) this.g);
        } else if (this.d < 0) {
            var4 = MathHelper.sqrt(var1) / this.i;
            this.d = MathHelper.d(var4 * (float) (this.h - this.g) + (float) this.g);
        }
    }
}
