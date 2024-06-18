package cyan.thegoodboys.megawalls.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class HideNameTag {

    public static void hideNameTag(Player player) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, player.getEntityId());

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, (byte) (watcher.getByte(0) | 0x20));

        List<WrappedWatchableObject> metadata = watcher.getWatchableObjects();
        packet.getWatchableCollectionModifier().write(0, metadata);

        try {
            for (Player onlinePlayer : player.getWorld().getPlayers()) {
                protocolManager.sendServerPacket(onlinePlayer, packet);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void showNameTag(Player player) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, player.getEntityId());

        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, (byte) (watcher.getByte(0) & 0xDF));

        List<WrappedWatchableObject> metadata = watcher.getWatchableObjects();
        packet.getWatchableCollectionModifier().write(0, metadata);

        try {
            for (Player onlinePlayer : player.getWorld().getPlayers()) {
                protocolManager.sendServerPacket(onlinePlayer, packet);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
