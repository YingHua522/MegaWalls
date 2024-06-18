package cyan.thegoodboys.megawalls.util;

import net.luckperms.api.LuckPermsProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class LuckPremsUtils {
    public static String getPrefix(Player p) {
        String prefix = Objects.requireNonNull(LuckPermsProvider.get().getUserManager().getUser(p.getUniqueId())).getCachedData().getMetaData().getPrefix();
        return prefix == null ? "" : ChatColor.translateAlternateColorCodes('&', prefix);
    }
}
