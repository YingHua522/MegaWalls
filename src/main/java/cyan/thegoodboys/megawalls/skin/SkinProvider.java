package cyan.thegoodboys.megawalls.skin;


import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesSkin;
import cyan.thegoodboys.megawalls.game.GamePlayer;

import java.util.List;

public interface SkinProvider {
    List<ClassesSkin> getSkinsForClass(Classes classes, GamePlayer gamePlayer);
    String getSkinName();
    //如果玩家已经选择了这个皮肤
    boolean isSkinSelected(GamePlayer gamePlayer);

}
