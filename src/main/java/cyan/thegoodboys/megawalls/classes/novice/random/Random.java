package cyan.thegoodboys.megawalls.classes.novice.random;

import cyan.thegoodboys.megawalls.classes.Classes;
import cyan.thegoodboys.megawalls.classes.ClassesInfo;
import cyan.thegoodboys.megawalls.classes.ClassesType;
import cyan.thegoodboys.megawalls.classes.EquipmentPackage;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Random extends Classes {
    private final List<Classes> purchasedClasses;

    public Random() {
        super("Random", "随机", "Random", ChatColor.DARK_PURPLE, Material.NETHER_STAR, (byte) 0, ClassesType.NOVICE, ClassesInfo.Orientation.values(), ClassesInfo.Difficulty.FOUR);
        this.setEquipmentPackage(new EquipmentPackage(this) {
            public List<ItemStack> getEquipments(int var1) {
                return new ArrayList<>();
            }

            public List<String> getInfo(int var1) {
                return null;
            }
        });
        this.purchasedClasses = new ArrayList<>(); // 初始化已购买职业列表
    }

    // 添加一个方法来获取玩家已购买的职业
    public void addPurchasedClass(Classes purchasedClass) {
        this.purchasedClasses.add(purchasedClass);
    }

    // 游戏开始时，随机选择一个已购买的职业
    public Classes getRandomPurchasedClass() {
        if (purchasedClasses.isEmpty()) {
            return null; // 如果玩家没有购买任何职业，返回null
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(purchasedClasses.size());
        return purchasedClasses.get(randomIndex);
    }

    public String getSkillTip(GamePlayer var1) {
        return "随机选择一个职业";
    }

    public List<String> getInfo() {
        List<String> lore = new ArrayList<>();
        lore.add("§a随机选择一个职业");
        lore.add(" ");
        lore.add("§a本局获得§6硬币§bx2");
        return lore;
    }

    public int unlockCost() {
        return 0;
    }
}
