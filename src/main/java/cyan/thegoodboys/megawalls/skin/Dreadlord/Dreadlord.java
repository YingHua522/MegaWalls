package cyan.thegoodboys.megawalls.skin.Dreadlord;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesSkin;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.skin.SkinProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dreadlord implements SkinProvider {

    private final String skinName = "Dreadlord";

    @Override
    public List<ClassesSkin> getSkinsForClass(Classes classes, GamePlayer gamePlayer) {
        List<ClassesSkin> skins = new ArrayList<>();
        if (classes.getName().equalsIgnoreCase("Dreadlord")) {
            ClassesSkin skin = new ClassesSkin("§aDreadlord", "§a恐惧魔 (默认)", Arrays.asList("", "§7原版恐惧魔,", "",this.isSkinSelected(gamePlayer) ? "§a已选择！" : "§e点击选择！"), "ewogICJ0aW1lc3RhbXAiIDogMTcwOTM3NDEwODk5NSwKICAicHJvZmlsZUlkIiA6ICIwYTM4MDlhOWEyNzE0YTFkYjIzNDg2MjJiYTJlZWU1ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNZWdhV2FsbHMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTgyZDQ3ZGU2OGEyOTBlZTMxODJiZWM3N2YwMjMwNzExZTcxOWZjNmYyZTQ5NTU3ZTU2MzViNWE2NDRhYzUyNCIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0MGMwZTAzZGQyNGExMWIxNWE4YjMzYzJhN2U5ZTMyYWJiMjA1MWIyNDgxZDBiYTdkZWZkNjM1Y2E3YTkzMyIKICAgIH0KICB9Cn0=", "VE+TUXwPidgTZCL+siT1rMInwtxXbLxY+OrhTSSSbTgYup8vfiggmKgDq+Rf3ZyRVB4CHZbOrQSZ8/zNRyM7A9hcNQTzxi7wE2j5/1YASzki3441hIO27RYQpXZb725cLPqAYgNEW5hXu8tDk3TKArn/J6XUgkt+S8qWOPyHvZLX6yyGz64J7JjraKTRESwiDu/y1NSZTOTCzG813AQkaDQklg7Gldo07dKqF/+lvNnIMuZUhaV8gUJkCGztrEiAcRNGkogT3vU1k/PzKgsuj7Ix+/MNCpAKNthLCruH2EzyVRFkoLNULz1HvFL8Kx9bxAeydJJzU2x1fd9vdBvF/vVTHnyWQwPFNo3W0+clWfjHqHdjp//Id6Csb7TX1xqDoJL4hyr1sDzFQK60QEkbRkIlA8T9pc/xkI95IXgYkYeTNfPEHdnnSPq6N1iLHrGC6aEK9lhb4pPtZwzgWTji+n4SFQdx37bdx7L/9wMN2okqzqU6v/0J1b1UMZfK2dL66hK2wGEy7iP8vKtlxF5diMnKT38nOawd8wpRLTFfaAG+MQnRcHGi1Ciz3apo3TBxYszUsReMxveei2pgtNavLNmwPffFfoHegsdjXajnyaKC8TTe5AeOP+XzadajiJUjeQ2+S6yHG3MO4/4SlqaSHRtUY1yBupRJe9pxGtzuTM0=", skinName);
            skins.add(skin);
        }
        return skins;
    }

    @Override
    public String getSkinName() {
        return skinName;
    }

    @Override
    public boolean isSkinSelected(GamePlayer gamePlayer) {
        ClassesSkin selectedSkin = gamePlayer.getPlayerStats().getSelectedSkin();
        return selectedSkin != null && gamePlayer.getPlayerStats().getSelectedSkin().getName().equalsIgnoreCase(skinName);
    }
}
