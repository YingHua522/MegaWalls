/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.stats;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.database.DataBase;
import cyan.thegoodboys.megawalls.database.KeyValue;
import cyan.thegoodboys.megawalls.effect.EffectManager;
import cyan.thegoodboys.megawalls.effect.HologramEffect;
import cyan.thegoodboys.megawalls.effect.KillMessage;
import cyan.thegoodboys.megawalls.effect.TeamDisplayColor;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.game.team.TeamColor;

public class EffectStatsContainer {
    private static DataBase database = MegaWalls.getInstance().getDataBase();
    private GamePlayer gamePlayer;
    private String hologramEffect = "Default";
    private String killMessage = "Default";
    private String redColor = "RED";
    private String greenColor = "GREEN";
    private String blueColor = "BLUE";
    private String yellowColor = "YELLOW";
    private boolean enablePrefix = true;
    private boolean enableItalic = false;
    private boolean enableBold = false;
    private boolean surface = false;

    public EffectStatsContainer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public HologramEffect getHologramEffect() {
        return EffectManager.getHologramEffect(this.hologramEffect);
    }

    public void setHologramEffect(String hologramEffect) {
        this.hologramEffect = hologramEffect;
    }

    public KillMessage getKillMessage() {
        return EffectManager.getKillMessage(this.killMessage);
    }

    public void setKillMessage(String killMessage) {
        this.killMessage = killMessage;
    }

    public TeamDisplayColor getColor(TeamColor teamColor) {
        switch (teamColor) {
            case RED: {
                return this.getRedColor();
            }
            case GREEN: {
                return this.getGreenColor();
            }
            case BLUE: {
                return this.getBlueColor();
            }
            case YELLOW: {
                return this.getYellowColor();
            }
        }
        return TeamDisplayColor.RED;
    }

    public void setColor(TeamColor teamColor, TeamDisplayColor color) {
        switch (teamColor) {
            case RED: {
                this.updateRedColor(color.name());
                break;
            }
            case GREEN: {
                this.updateGreenColor(color.name());
                break;
            }
            case BLUE: {
                this.updateBlueColor(color.name());
                break;
            }
            case YELLOW: {
                this.updateYellowColor(color.name());
            }
        }
    }

    public TeamDisplayColor getRedColor() {
        return TeamDisplayColor.valueOf(this.redColor);
    }

    public void setRedColor(String redColor) {
        this.redColor = redColor;
    }

    public TeamDisplayColor getGreenColor() {
        return TeamDisplayColor.valueOf(this.greenColor);
    }

    public void setGreenColor(String greenColor) {
        this.greenColor = greenColor;
    }

    public TeamDisplayColor getBlueColor() {
        return TeamDisplayColor.valueOf(this.blueColor);
    }

    public void setBlueColor(String blueColor) {
        this.blueColor = blueColor;
    }

    public TeamDisplayColor getYellowColor() {
        return TeamDisplayColor.valueOf(this.yellowColor);
    }

    public void setYellowColor(String yellowColor) {
        this.yellowColor = yellowColor;
    }

    public void updateHologramEffect(String name) {
        this.hologramEffect = name;
        database.dbUpdate("megawalls_effects", new KeyValue("hologramEffect", name), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateKillMessage(String name) {
        this.killMessage = name;
        database.dbUpdate("megawalls_effects", new KeyValue("killMessage", name), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateRedColor(String name) {
        this.redColor = name;
        database.dbUpdate("megawalls_effects", new KeyValue("redColor", name), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateGreenColor(String name) {
        this.greenColor = name;
        database.dbUpdate("megawalls_effects", new KeyValue("greenColor", name), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateBlueColor(String name) {
        this.blueColor = name;
        database.dbUpdate("megawalls_effects", new KeyValue("blueColor", name), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateYellowColor(String name) {
        this.yellowColor = name;
        database.dbUpdate("megawalls_effects", new KeyValue("yellowColor", name), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateEnablePrefix(boolean enablePrefix) {
        this.enablePrefix = enablePrefix;
        database.dbUpdate("megawalls_effects", new KeyValue("enablePrefix", String.valueOf(enablePrefix)), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateEnableItalic(boolean enableItalic) {
        this.enableItalic = enableItalic;
        database.dbUpdate("megawalls_effects", new KeyValue("enableItalic", String.valueOf(enableItalic)), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateEnableBold(boolean enableBold) {
        this.enableBold = enableBold;
        database.dbUpdate("megawalls_effects", new KeyValue("enableBold", String.valueOf(enableBold)), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public void updateSurface(boolean surface) {
        this.surface = surface;
        database.dbUpdate("megawalls_effects", new KeyValue("surface", String.valueOf(surface)), new KeyValue("uuid", this.gamePlayer.getUuid().toString()));
    }

    public GamePlayer getGamePlayer() {
        return this.gamePlayer;
    }

    public boolean isEnablePrefix() {
        return this.enablePrefix;
    }

    public void setEnablePrefix(boolean enablePrefix) {
        this.enablePrefix = enablePrefix;
    }

    public boolean isEnableItalic() {
        return this.enableItalic;
    }

    public void setEnableItalic(boolean enableItalic) {
        this.enableItalic = enableItalic;
    }

    public boolean isEnableBold() {
        return this.enableBold;
    }

    public void setEnableBold(boolean enableBold) {
        this.enableBold = enableBold;
    }

    public boolean isSurface() {
        return this.surface;
    }

    public void setSurface(boolean surface) {
        this.surface = surface;
    }
}

