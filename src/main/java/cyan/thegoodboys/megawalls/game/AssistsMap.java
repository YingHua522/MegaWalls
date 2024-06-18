/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssistsMap {
    private GamePlayer gamePlayer;
    private Map<GamePlayer, Long> lastDamage = new HashMap<GamePlayer, Long>();

    public AssistsMap(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setLastDamage(GamePlayer damager, long time) {
        this.lastDamage.put(damager, time);
    }

    public List<GamePlayer> getAssists(long time) {
        ArrayList<GamePlayer> players = new ArrayList<>();
        for (Map.Entry<GamePlayer, Long> entry : this.lastDamage.entrySet()) {
            if (time - entry.getValue() > 60000L) continue; // 修改为60秒
            players.add(entry.getKey());
        }
        return players;
    }
}

