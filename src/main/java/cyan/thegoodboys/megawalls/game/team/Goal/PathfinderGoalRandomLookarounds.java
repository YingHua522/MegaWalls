package cyan.thegoodboys.megawalls.game.team.Goal;

import cyan.thegoodboys.megawalls.util.PlayerUtils;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PathfinderGoalRandomLookarounds extends PathfinderGoal {
    private final EntityInsentient a;
    private int d;
    public PathfinderGoalRandomLookarounds(EntityInsentient var1) {
        this.a = var1;
        this.a(3);
    }
    public boolean a() {
        return this.a.bc().nextFloat() < 0.02F;
    }

    public boolean b() {
        return this.d >= 0;
    }

    public void c() {
        double var1 = 6.283185307179586 * this.a.bc().nextDouble();
        double b = Math.cos(var1);
        double c = Math.sin(var1);
        this.d = 20 + this.a.bc().nextInt(20);
    }

    public void e() {
        for (Player player : PlayerUtils.getNearbyPlayers(this.a.getBukkitEntity().getLocation(), 25.0D)) {
            Location location = player.getLocation();
            this.a.getControllerLook().a(location.getX(),location.getY() +3.0,location.getZ(), 165.0F, 180.0F);
        }
    }
}
