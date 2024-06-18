package cyan.thegoodboys.megawalls.game.team.Goal;

import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PathfinderGoalLookAtPlayers extends PathfinderGoal {
    protected EntityInsentient a;
    protected Entity b;
    protected float c;
    private int e;
    private final float f;
    protected Class<? extends Entity> d;
    public PathfinderGoalLookAtPlayers(EntityInsentient var1, Class<? extends Entity> var2, float var3) {
        this.a = var1;
        this.d = var2;
        this.c = var3;
        this.f = 0.02F;
        this.a(2);
    }

    public PathfinderGoalLookAtPlayers(EntityInsentient var1, Class<? extends Entity> var2, float var3, float var4) {
        this.a = var1;
        this.d = var2;
        this.c = var3;
        this.f = var4;
        this.a(2);
    }
    public boolean a() {
        if (this.a.bc().nextFloat() >= this.f) {
            return false;
        } else {
            if (this.a.getGoalTarget() != null) {
                this.b = this.a.getGoalTarget();
            }

            if (this.d == EntityHuman.class) {
                this.b = this.a.world.findNearbyPlayer(this.a, this.c);
            } else {
                this.b = this.a.world.a(this.d, this.a.getBoundingBox().grow(this.c, 3.0, this.c), this.a);
            }

            return this.b != null;
        }
    }

    public boolean b() {
        if (!this.b.isAlive()) {
            return false;
        } else if (this.a.h(this.b) > (double)(this.c * this.c)) {
            return false;
        } else {
            return this.e > 0;
        }
    }

    public void c() {
        this.e = 40 + this.a.bc().nextInt(40);
    }

    public void d() {
        this.b = null;
    }

    public void e() {
        for (Player player : PlayerUtils.getNearbyPlayers(this.b.getBukkitEntity().getLocation(), 25.0D)) {
            Location location = player.getLocation();
            this.a.getControllerLook().a(location.getX(),location.getY() +3.0,location.getZ(), 165.0F, 180.0F);
        }
    }
}
