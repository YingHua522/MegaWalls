/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MusicMenu {
    public static SmartInventory build() {
        SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("Change Log");
        builder.size(5, 9);
        builder.closeable(true);
        builder.provider(new InventoryProvider() {

            @Override
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                ArrayList<String> lore = new ArrayList<String>();
                lore.add("§f凤凰小奶增加音效");
                lore.add("§f傀儡技能特效优化");
                lore.add("§f爬行者速爆TNT友伤修复");
                lore.add("§f鼹鼠技能优化");
                lore.add("§f旁观者修复");
                lore.add("§f简化组队指令");
                lore.add("§f风格修改");
                lore.add("§f以后更新都会在这里显示");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(0, 8, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a2023.7.17").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));
                lore.clear();
                lore.add("§f凤凰大奶改为精确命中玩家回复失去生命值的50%,周围3名玩家回复失去生命值的35%");
                lore.add("§f凤凰小奶改为精确命中时对命中者及其周围3格玩家造成2点伤害,若命中者为队友则回复命中者及其周围3格内队友(包括自己)2HP");
                lore.add("§f狼人变身改为命中x名玩家(上限7)在变身结束时造成x点伤害并回复xHP");
                lore.add("§f机器人技能现在可以叠加");
                lore.add("§f机器人微积分现在没有血量限制,且每10能量增伤1%");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(0, 7, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a2023.8.4").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));
                lore.clear();
                lore.add("§f恐惧魔王影爆方块破坏修复");
                lore.add("§f洞穴蜘蛛技能破坏方块修复");
                lore.add("§f猎人自然之力修复");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(0, 6, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a2023.8.9").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));
                lore.clear();
                lore.add("§f猪人技能重写");
                lore.add("§f雪人技能重写");
                lore.add("§f烈焰人技能重写");
                lore.add("§f海盗目前是三种模式");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(0, 5, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a2023.9.2").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));
                lore.clear();
                lore.add("§f烈焰人技能范围调整");
                lore.add("§f不法者箭矢更改为命中40秒后消失");
                lore.add("§f不法者抓钩重写&优化");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(0, 4, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a2023.9.8").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));
                lore.clear();
                lore.add("§f部分职业特效音效调整");
                lore.add("§f修复了退出重连后在别人视角隐形的问题（现在仅在自己的tab列表隐藏）");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(0, 3, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a2023.11.4").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));
                lore.clear();
                lore.add("§f恐惧技能速度调整");
                lore.add("§f初始金苹果8->4 鱿鱼药6->3");
                lore.add("§f修复了不法者拔箭后箭数为负数的问题");
                lore.add("§f修复了狼人嗜血不清零的问题");
                lore.add("§f修复了猪人卡隐形盔甲架的问题");
                lore.add("§f末影人穿墙tp会给予虚弱效果");
                lore.add("§f牛奶可以被堆叠");
                lore.add("§f鼹鼠职业装备增加5个垃圾苹果和40个垃圾派");
                lore.add("§f爬行者职业装备增加64个TNT");
                lore.add("§f牛职业装备增加21桶奶");
                lore.add("§f凤凰职业装备增加9瓶凤凰药");
                lore.add("§f不法者职业装备增加5瓶不法者药");
                lore.add("§f傀儡职业装备增加2瓶缓慢虚弱药水");
                lore.add("§f牛职业装备增加21桶奶（职业装备）");
                lore.add("§f单血药职业血药回复8->10❤");
                lore.add("§f修正翻译");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(0, 2, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a2023.12.7").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));
                lore.clear();
                lore.add("§f末影人移除穿墙虚弱 瞬移CD5->8(Hypixel)");
                lore.add("§f不法者抓钩优化");
                lore.add("§f傀儡药水音效修复");
                lore.add("§f烈焰人技能判定范围增加");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(0, 1, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a2024.2.1").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));
                lore.clear();
                lore.add("§f修复鼹鼠可以破坏城堡的BUG");
                lore.add("§f修复狼人变身无法使用");
                lore.add("§f现在开始箱子只会爆一个斧头和4个熔炉");
                lore.add("§f修复了世界区块管理会卡凋灵的问题");
                lore.add("§f现在游戏开始刺客和鼹鼠开局会给予能量了");
                lore.add("§f添加了雪人暴风雪特效，并且修复暴风雪后的方块不会复原");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(0, 0, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a2024.2.18").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));
                lore.clear();
                lore.add("§f猎人没有猪");
                lore.add("§f萨满的风不能破坏方块且移动");
                lore.add("§f凤凰阳炎射线不能斩杀1血敌人");
                lore.add("§f雪人暴风雪不回变回原来的方块");
                lore.add("§e Mega Walls §5By: §eTheGoodBoys 重置版");
                contents.set(4, 8, ClickableItem.of(new ItemBuilder(Material.PAPER).setDisplayName("§a目前存在的问题").setLore(lore).build(), e -> {
                    gamePlayer.playSound(Sound.CLICK, 1.0f, 1.0f);
                }));

            }

            @Override
            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }

}

