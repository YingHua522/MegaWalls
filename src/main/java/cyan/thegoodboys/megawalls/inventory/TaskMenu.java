/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 */
package cyan.thegoodboys.megawalls.inventory;

import cyan.thegoodboys.megawalls.classes.Skill;
import cyan.thegoodboys.megawalls.game.GamePlayer;
import cyan.thegoodboys.megawalls.reward.Challenge;
import cyan.thegoodboys.megawalls.reward.RewardManager;
import cyan.thegoodboys.megawalls.reward.Task;
import cyan.thegoodboys.megawalls.stats.KitStatsContainer;
import cyan.thegoodboys.megawalls.stats.TaskStatsContainer;
import cyan.thegoodboys.megawalls.util.ItemBuilder;
import cyan.thegoodboys.megawalls.util.StringUtils;
import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import cyan.thegoodboys.megawalls.inv.content.InventoryContents;
import cyan.thegoodboys.megawalls.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TaskMenu {
    public static SmartInventory build() {
        SmartInventory.Builder builder = SmartInventory.builder();
        builder.title("超级战墙");
        builder.size(5, 9);
        builder.closeable(true);
        builder.provider(new InventoryProvider() {
            public void init(Player player, InventoryContents contents) {
                GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
                contents.set(0, 4, ClickableItem.of((new ItemBuilder(Material.SOUL_SAND)).setDisplayName("§a超级战墙 任务与挑战").setLore("§7查看所有参与超级战墙", "§7可完成的任务和挑战。").build(), (e) -> {
                }));
                int column = 2;

                int id;
                for (id = 1; id <= 5; ++id) {
                    Task task = RewardManager.getTask(id);
                    TaskStatsContainer container = gamePlayer.getPlayerStats().getTaskStats(task);
                    ItemBuilder itemBuilderx = (new ItemBuilder(task.getId() == 3 ? Material.SKULL_ITEM : Material.PAPER, 1, (byte) (task.getId() == 3 ? 3 : 0))).setDisplayName("§a" + task.getType().getName() + "任务: " + task.getName());
                    List<String> lorex = new ArrayList();
                    lorex.addAll(task.getInfo(container));
                    lorex.add(" ");
                    lorex.add("§7奖励:");
                    lorex.addAll(task.getRewardInfo());
                    lorex.add(" ");
                    lorex.add(task.getType().getInfo());
                    lorex.add(" ");
                    if (!Task.isLater(Task.current(), container.getLastFinished(), task.getType())) {
                        lorex.add("§c还不能开始此任务！");
                    } else if (container.isAccept() && !task.isFinish(container)) {
                        lorex.add("§a已开始此任务！");
                        itemBuilderx.addGlow();
                    } else {
                        lorex.add("§e点击开始此任务！");
                    }

                    itemBuilderx.setLore(lorex);
                    contents.set(id == 3 ? 2 : 1, column, ClickableItem.of(itemBuilderx.build(), (e) -> {
                        if (!Task.isLater(Task.current(), container.getLastFinished(), task.getType())) {
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                            player.sendMessage("§c还不能开始此任务！");
                        } else if (container.isAccept() && !task.isFinish(container)) {
                            gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                            player.sendMessage("§c你已经开始了 §6" + task.getType().getName() + "任务: " + task.getName() + " §c任务！");
                            player.sendMessage("§e状态:");

                            for (String message : task.getInfo(container)) {
                                player.sendMessage(message);
                            }
                        } else {
                            gamePlayer.playSound(Sound.NOTE_PLING, 1.0F, 3.0F);
                            container.accept();
                            player.sendMessage("§a你开始了 §6" + task.getType().getName() + "任务: " + task.getName() + " §a任务！");
                            contents.inventory().open(player);
                        }

                    }));
                    ++column;
                }

                column = 2;

                for (id = 1; id <= 4; ++id) {
                    Challenge challenge = RewardManager.getChallenge(id);
                    ItemBuilder itemBuilder = (new ItemBuilder(Material.EMPTY_MAP)).setDisplayName("§a" + challenge.getName() + "挑战");
                    List<String> lore = new ArrayList<>();
                    lore.addAll(challenge.getInfo());
                    lore.add(" ");
                    lore.add("§7奖励:");
                    lore.addAll(challenge.getRewardInfo());
                    lore.add(" ");
                    lore.add("§8§o你每天可以完成多个");
                    lore.add("§8§o相同的挑战,但每局");
                    lore.add("§8§o游戏只能完成一次。");
                    lore.add(" ");
                    if (!Challenge.isLater(Challenge.current(), gamePlayer.getPlayerStats().getChallengeLastFinished(challenge.getId()))) {
                        lore.add("§c今天已经完成了该挑战！");
                    } else {
                        lore.add("§7今天剩余可完成挑战次数: §a" + (10 - gamePlayer.getPlayerStats().getChallenge(challenge.getId())));
                    }

                    itemBuilder.setLore(lore);
                    contents.set(4, id >= 3 ? column + 1 : column, ClickableItem.of(itemBuilder.build(), (e) -> {
                        gamePlayer.playSound(Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        player.sendMessage("§c你不需要手动启用这些挑战！加入游戏可自动启用！");
                    }));
                    ++column;
                }

            }

            public void update(Player player, InventoryContents contents) {
            }
        });
        return builder.build();
    }

    public static String getUpgradeBar(Skill skill, KitStatsContainer kitStats, int level, int max) {
        return StringUtils.upgradeBar(level, max) + " §7" + skill.getName();
    }

    public static String getUpgradeBar(String name, KitStatsContainer kitStats, int level, int max) {
        return StringUtils.upgradeBar(level, max) + " §7" + name;
    }

    public static String getMasterStar(int level) {
        if (level < 2) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer(" §6");

            for (int i = 2; i <= level; ++i) {
                sb.append("✫");
            }

            return sb.toString();
        }
    }
}

