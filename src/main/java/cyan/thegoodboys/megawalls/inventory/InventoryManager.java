/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.inv.SmartInventory;

public class InventoryManager {
    public static SmartInventory SPECTATORSETTINGS = null;
    public static SmartInventory CLASSESSELECTOR = null;
    public static SmartInventory CLASSESBAN = null;
    public static SmartInventory SHOPMENU = null;
    public static SmartInventory CLASSESCONTAINER = null;
    public static SmartInventory UNLOCKCLASSES_NORMAL = null;
    public static SmartInventory UNLOCKCLASSES_MYTHIC = null;
    public static SmartInventory MYTHICDUSTSHOP = null;
    public static SmartInventory EFFECTMENU = null;
    public static SmartInventory EFFECTMENU_HOLOGRAMEFFECT = null;
    public static SmartInventory EFFECTMENU_KILLMESSAGE = null;
    public static SmartInventory TASKMENU = null;
    public static SmartInventory MUSICMENU = null;
    public static SmartInventory GAMEMENU = null;
    public static SmartInventory SKINMENU = null;

    public static void registerAll() {
        SPECTATORSETTINGS = SpectatorInventory.buildSettings();
        CLASSESSELECTOR = ClassesSelector.build();
        CLASSESBAN = ClassesBan.build();
        SHOPMENU = ShopMenu.build();
        CLASSESCONTAINER = ClassesContainer.build();
        UNLOCKCLASSES_NORMAL = UnlockClassesMenu.buildNormal();
        UNLOCKCLASSES_MYTHIC = UnlockClassesMenu.buildMythic();
        MYTHICDUSTSHOP = MythicDustShop.build();
        EFFECTMENU = EffectMenu.build();
        EFFECTMENU_HOLOGRAMEFFECT = EffectMenu.buildHologramEffect();
        EFFECTMENU_KILLMESSAGE = EffectMenu.buildKillMessage();
        TASKMENU = TaskMenu.build();
        MUSICMENU = MusicMenu.build();
        SKINMENU = Skin.build();
        GAMEMENU = GameMenu.build();
    }
}

