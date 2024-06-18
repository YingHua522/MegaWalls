package cyan.thegoodboys.megawalls.nms;

import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.World;

public class CustomZombie extends EntityZombie {
    public CustomZombie(World world) {
        super(world);
    }


    @Override
    public void m() {
        //删除AI
    }
}
