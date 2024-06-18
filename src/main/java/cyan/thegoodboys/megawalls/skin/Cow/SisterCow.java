package cyan.thegoodboys.megawalls.skin.Cow;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesSkin;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.skin.SkinProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SisterCow implements SkinProvider {
    private final String skinName = "Jumperisko";

    @Override
    public List<ClassesSkin> getSkinsForClass(Classes classes, GamePlayer gamePlayer) {
        List<ClassesSkin> skins = new ArrayList<>();
        if (classes.getName().equalsIgnoreCase("Cow")) {
            ClassesSkin skin = new ClassesSkin("§a妹妹牛", "§a妹妹牛", Arrays.asList("", "§7萌萌的妹子，却是一头牛，但她丝毫不畏惧！", "",this.isSkinSelected(gamePlayer) ? "§a已选择！" : "§e点击选择！"), "ewogICJ0aW1lc3RhbXAiIDogMTcxNDQ4NzU2NjMwMywKICAicHJvZmlsZUlkIiA6ICJiODYyZjM4OTNlN2E0ODUwYmNiNzRjYTY0YTRmNGE0MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJKdW1wZXJpc2tvIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVkMTA5NWU5NzFkYjAyZTJhYzIxNGU4MjZhMjY5Y2Y3ZDJlNzZmYmJmNGI2NzU1ODg2MDM0NmE5N2QxZjA3NTMiCiAgICB9CiAgfQp9","f9bSE1D9QX95U/vEpzbHLHB8w80pPMu5ItNLmC7MNrEBMoVkXufG4pOAOOUe70vYRro+NH9lccS8uz3yBa5nCpO6JkbHT+a9wQpOfXNV0CFqrEr9A0es3zDab6Qr/kIFPjs42dLFN1IXUD6MMkr9yIvgiAgW4tfOAUuE7UarB1T0BVdHEpsMOl+yVxTFF+NnEfcIyqJXM+4SOZ8+O5eRv1Ac/Lgn3oU3zoRwrav0g6Q132FIpxYRVMh+b1dphTwN6WFMQYaL/l2SJgui+aH5iL+ld/KPWH8FH3L7lGNH/05N5aB/YFETLhhW4GKJnNQrSxdGV/izTb6ZhuHup2qON/gSZd7HBMhgeOdR26TyYqWI9fdYPNJEOGPVy5pbhh6/lG1RgVvsbiCV+knQes/1mSNDNv82Ajl8Wkn/9AAMcm1mGvgQ7g6bRhtzsI8SmVqEMXdF3WNDvbge9g+qziY2y/GcaTTILrybDAHltOejyzJeWqvGENzZ68P6j7Gi1PtqGVZncmeWgiqATg1FHpl95+GdwI9K61PycZVMnZJhlYY+Bq+9/kvME4U7AIliuTuHthnvMexKHDoBM/PSga9Je4a9ydicznNjJtOW1KT8xJAxI6ublyo2VqfbCaBIqV3r+HFZ9G1RgxH6dDKDJIdLJvu5qEKwu4Vuhs3Wc48/FTQ=", skinName);
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
