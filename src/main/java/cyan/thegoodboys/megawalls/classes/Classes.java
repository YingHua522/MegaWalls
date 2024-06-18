/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.classes;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.api.event.PlayerEnergyChangeEvent;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Classes implements Listener, Runnable {
    private static final Map<GamePlayer, Integer> addingEnergy = new HashMap<>();
    private static final Map<GamePlayer, Integer> energyPerSeconds = new HashMap<>();

    static {
        Bukkit.getScheduler().runTaskTimer(MegaWalls.getInstance(), () -> {
            for (Map.Entry entry : addingEnergy.entrySet()) {
                if ((Integer) entry.getValue() <= 0) continue;
                ((GamePlayer) entry.getKey()).addEnergy(energyPerSeconds.getOrDefault(entry.getKey(), 1), PlayerEnergyChangeEvent.ChangeReason.MAGIC);
                entry.setValue((Integer) entry.getValue() - 1);
            }
        }, 0L, 20L);
    }

    private final String name;
    protected ChatColor nameColor;
    protected Skill mainSkill;
    protected Skill secondSkill;
    protected Skill thirdSkill;
    protected CollectSkill collectSkill;
    private String displayName;
    private String ing;
    private Material iconType;
    private byte iconData;
    private ClassesType classesType;
    private ClassesInfo.Orientation[] orientations;
    private ClassesInfo.Difficulty difficulty;
    private EquipmentPackage equipmentPackage;
    private ClassesSkin defaultSkin;

    public Classes(String name, String displayName, String ing, ChatColor nameColor, Material iconType, byte iconData, ClassesType classesType, ClassesInfo.Orientation[] orientations, ClassesInfo.Difficulty difficulty) {
        this.name = name;
        this.displayName = displayName;
        this.ing = ing;
        this.nameColor = nameColor;
        this.iconType = iconType;
        this.iconData = iconData;
        this.classesType = classesType;
        this.orientations = orientations;
        this.difficulty = difficulty;
    }

    public static void startAddEnergyTimer(GamePlayer gamePlayer, int seconds, int energy) {
        if (addingEnergy.getOrDefault(gamePlayer, 0) > 0) {
            return;
        }
        energyPerSeconds.put(gamePlayer, energy);
        addingEnergy.put(gamePlayer, seconds);
    }

    public static void stopAddEnergyTimer(GamePlayer gamePlayer) {
        energyPerSeconds.put(gamePlayer, 0);
        addingEnergy.put(gamePlayer, 0);
    }

    public int energyMelee() {
        return 0;
    }

    public int energyBow() {
        return 0;
    }

    public abstract String getSkillTip(GamePlayer var1);

    @Override
    public void run() {
    }

    public abstract List<String> getInfo();

    public abstract int unlockCost();

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getING() {
        return this.ing;
    }

    public ChatColor getNameColor() {
        return this.nameColor;
    }

    public Material getIconType() {
        return this.iconType;
    }

    public byte getIconData() {
        return this.iconData;
    }

    public ClassesType getClassesType() {
        return this.classesType;
    }

    public ClassesInfo.Orientation[] getOrientations() {
        return this.orientations;
    }

    public ClassesInfo.Difficulty getDifficulty() {
        return this.difficulty;
    }

    public EquipmentPackage getEquipmentPackage() {
        return this.equipmentPackage;
    }

    public void setEquipmentPackage(EquipmentPackage equipmentPackage) {
        this.equipmentPackage = equipmentPackage;
    }

    public Skill getMainSkill() {
        return this.mainSkill;
    }

    public void setMainSkill(Skill mainSkill) {
        this.mainSkill = mainSkill;
    }

    public Skill getSecondSkill() {
        return this.secondSkill;
    }

    public void setSecondSkill(Skill secondSkill) {
        this.secondSkill = secondSkill;
    }

    public Skill getThirdSkill() {
        return this.thirdSkill;
    }

    public void setThirdSkill(Skill thirdSkill) {
        this.thirdSkill = thirdSkill;
    }

    public CollectSkill getCollectSkill() {
        return this.collectSkill;
    }

    public void setCollectSkill(CollectSkill collectSkill) {
        this.collectSkill = collectSkill;
    }

    public ClassesSkin getDefaultSkin() {
        return this.defaultSkin;
    }

    public void setDefaultSkin(String value, String signature) {
        this.defaultSkin = new ClassesSkin("Default", this.getDisplayName(), Collections.singletonList("§7" + this.getDisplayName() + "的默认皮肤"), value, signature, null);
    }
}

