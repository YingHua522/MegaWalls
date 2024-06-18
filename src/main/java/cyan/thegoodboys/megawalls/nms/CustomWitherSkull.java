package cyan.thegoodboys.megawalls.nms;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CustomWitherSkull extends EntityArmorStand {
    private double damage;

    public CustomWitherSkull(org.bukkit.World world, double damage) {
        super(((CraftWorld) world).getHandle());
        this.damage = damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setVelocity(Vector velocity) {
        this.getBukkitEntity().setVelocity(velocity);
    }

    public void launch(Player player) {
        this.setVelocity(player.getLocation().getDirection().multiply(1.5));
        this.setGravity(true);
    }

    public double getDamage() {
        return this.damage;
    }
}
