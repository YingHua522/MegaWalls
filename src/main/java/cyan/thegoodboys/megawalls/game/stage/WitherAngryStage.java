/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.game.stage;

import cyan.thegoodboys.megawalls.game.Game;

public class WitherAngryStage extends GameStage {
    public WitherAngryStage() {
        super("凋灵暴怒消失", 480, 0, 2);
    }


    @Override
    public void excute(Game game) {
        game.getStageManager().setCurrentStage(3);
        game.broadcastTitle("", "§a凋灵不再暴怒！", 10, 20, 10);
        game.broadcastMessage("§a凋灵不再暴怒！§7(特殊攻击已经禁用)");
        game.setWitherAngry(false);
    }

    @Override
    public void excuteLeftSeconds(Game game, int left) {
    }
}

