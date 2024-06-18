package cyan.thegoodboys.megawalls.nms;

import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.WorldBorder;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class WorldBoards {

    public static void giveRedScreenEffect(Player player) {
        // 创建一个新的世界边界
        WorldBorder worldBorder = new WorldBorder();
        // 设置边界的警告距离，当玩家在这个距离内时，他们的屏幕会变红
        worldBorder.setWarningDistance(Integer.MAX_VALUE);
        // 创建一个新的世界边界包
        PacketPlayOutWorldBorder borderPacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
        // 发送包给玩家
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(borderPacket);
    }

    public static void clearRedScreenEffect(Player player) {
        // 创建一个新的世界边界
        WorldBorder worldBorder = new WorldBorder();
        // 设置边界的警告距离回到正常值
        worldBorder.setWarningDistance(0);
        // 创建一个新的世界边界包
        PacketPlayOutWorldBorder borderPacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
        // 发送包给玩家
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(borderPacket);
    }
}
