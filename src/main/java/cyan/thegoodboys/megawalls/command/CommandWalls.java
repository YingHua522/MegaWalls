/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandException
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.command;

import cyan.thegoodboys.megawalls.MegaWalls;
import cyan.thegoodboys.megawalls.config.FileConfig;
import cyan.thegoodboys.megawalls.game.Game;
import cyan.thegoodboys.megawalls.game.GameType;
import cyan.thegoodboys.megawalls.game.team.TeamColor;
import cyan.thegoodboys.megawalls.listener.SetupListener;
import cyan.thegoodboys.megawalls.inv.opener.SQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class CommandWalls extends BaseCommand {
    private final FileConfig config;

    public CommandWalls() {
        super("walls");
        this.setPermission("MegaWalls.admin");
        this.config = new FileConfig(MegaWalls.getInstance(), "game_冰冻.yml");
    }

    @Override
    public String getPossibleArguments() {
        return "<setlobby/settype/setmapname/setup>";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }


    @Override
    public void execute(CommandSender sender, String label, String[] args) throws CommandException {
        if (args.length == 0) {
            try {
                String text = SQL.fetchTextFromGitee();
                sender.sendMessage("§a" + text);
            } catch (Exception e) {
                sender.sendMessage("§c无法从云端获取文本内容");
            }
            return;
        }
        if (MegaWalls.getInstance().getConfig().getBoolean("setup", false)) {
            return;
        }
        if (args[0].equalsIgnoreCase("setup")) {
            if (Game.build(this.config, sender)) {
                sender.sendMessage("§a游戏安装成功!正在重启服务器!");
                MegaWalls.getInstance().getConfig().set("setup", true);
                MegaWalls.getInstance().getConfig().save();
                Bukkit.shutdown();
                return;
            }
            sender.sendMessage("§c游戏安装失败!");
            return;
        }
        if (args[0].equalsIgnoreCase("setlobby")) {
            Location location = ((Player) sender).getLocation();
            this.config.setLocation(location, "lobby");
            this.config.save();
            sender.sendMessage("§a等待大厅设置成功!");
            return;
        }
        if (args[0].equalsIgnoreCase("settype")) {
            if (args.length >= 2) {
                GameType type;
                try {
                    type = GameType.valueOf(args[1].toUpperCase());
                } catch (Exception e) {
                    sender.sendMessage("§c类型只有 DUEL 或 NORMAL!");
                    return;
                }
                this.config.set("type", type.name());
                this.config.save();
                sender.sendMessage("§a类型设置成功!");
                return;
            }
            sender.sendMessage("§c/walls settype <类型只有DUEL/NORMAL>");
            return;
        }
        if (args[0].equalsIgnoreCase("setmapname")) {
            if (args.length >= 2) {
                this.config.set("mapname", args[1]);
                this.config.save();
                sender.sendMessage("§a成功设置地图名: " + args[1]);
                return;
            }
            sender.sendMessage("§c/walls setmapname <mapname>");
            return;
        }
        if (args[0].equalsIgnoreCase("setspawn") && args.length >= 2) {
            TeamColor teamColor;
            try {
                teamColor = TeamColor.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage("§c队伍颜色错误(RED/YELLOW/BLUE/GREEN)");
                return;
            }
            Location location = ((Player) sender).getLocation();
            this.config.setLocation(location, "teams." + teamColor.name() + ".spawn");
            this.config.save();
            sender.sendMessage("§a队伍出生地设置成功!");
            return;
        }
        if (args[0].equalsIgnoreCase("setwither") && args.length >= 2) {
            TeamColor teamColor;
            try {
                teamColor = TeamColor.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage("§c队伍颜色错误(RED/YELLOW/BLUE/GREEN)");
                return;
            }
            Location location = ((Player) sender).getLocation();
            this.config.setLocation(location, "teams." + teamColor.name() + ".wither");
            this.config.save();
            sender.sendMessage("§a队伍凋灵出生点设置成功!");
            return;
        }
        if (args[0].equalsIgnoreCase("setregion") && args.length >= 2) {
            TeamColor teamColor;
            try {
                teamColor = TeamColor.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage("§c队伍颜色错误(RED/YELLOW/BLUE/GREEN)");
                return;
            }
            if (SetupListener.left == null || SetupListener.right == null) {
                sender.sendMessage("§c请先选择区域!");
                return;
            }
            this.config.setBlockLocation(SetupListener.left, "teams." + teamColor.name() + ".region.loc1");
            this.config.setBlockLocation(SetupListener.right, "teams." + teamColor.name() + ".region.loc2");
            this.config.save();
            sender.sendMessage("§a队伍区域设置成功!");
            return;
        }
        if (args[0].equalsIgnoreCase("setcastle") && args.length >= 2) {
            TeamColor teamColor;
            try {
                teamColor = TeamColor.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage("§c队伍颜色错误(RED/YELLOW/BLUE/GREEN)");
                return;
            }
            if (SetupListener.left == null || SetupListener.right == null) {
                sender.sendMessage("§c请先选择区域!");
                return;
            }
            this.config.setBlockLocation(SetupListener.left, "teams." + teamColor.name() + ".castle.loc1");
            this.config.setBlockLocation(SetupListener.right, "teams." + teamColor.name() + ".castle.loc2");
            this.config.save();
            sender.sendMessage("§a队伍城堡设置成功!");
            return;
        }
        if (args[0].equalsIgnoreCase("setteamreseve") && args.length >= 2) {
            TeamColor teamColor;
            try {
                teamColor = TeamColor.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage("§c队伍颜色错误(RED/YELLOW/BLUE/GREEN)");
                return;
            }
            if (SetupListener.left == null || SetupListener.right == null) {
                sender.sendMessage("§c请先选择区域!");
                return;
            }
            this.config.setBlockLocation(SetupListener.left, "teams." + teamColor.name() + ".teamreserve.loc1");
            this.config.setBlockLocation(SetupListener.right, "teams." + teamColor.name() + ".teamreserve.loc2");
            this.config.save();
            sender.sendMessage("§a队伍保护区设置成功!");
            return;
        }
        if (args[0].equalsIgnoreCase("setreserve") && args.length >= 2) {
            TeamColor teamColor;
            try {
                teamColor = TeamColor.valueOf(args[1].toUpperCase());
            } catch (Exception e) {
                sender.sendMessage("§c队伍颜色错误(RED/YELLOW/BLUE/GREEN)");
                return;
            }
            if (SetupListener.left == null || SetupListener.right == null) {
                sender.sendMessage("§c请先选择区域!");
                return;
            }
            this.config.setBlockLocation(SetupListener.left, "teams." + teamColor.name() + ".reserve.loc1");
            this.config.setBlockLocation(SetupListener.right, "teams." + teamColor.name() + ".reserve.loc2");
            this.config.save();
            sender.sendMessage("§a队伍区域门设置成功!");
            return;
        }
        if (args[0].equalsIgnoreCase("setlobbyclear")) {
            if (SetupListener.left == null || SetupListener.right == null) {
                sender.sendMessage("§c请先选择区域!");
                return;
            }
            this.config.setBlockLocation(SetupListener.left, "lobby-clear.loc1");
            this.config.setBlockLocation(SetupListener.right, "lobby-clear.loc2");
            this.config.save();
            sender.sendMessage("§a清除大厅区域设置成功!");
            return;
        }
        if (args[0].equalsIgnoreCase("setcenterarea")) {
            if (SetupListener.left == null || SetupListener.right == null) {
                sender.sendMessage("§c请先选择区域!");
                return;
            }
            this.config.setBlockLocation(SetupListener.left, "center-area.loc1");
            this.config.setBlockLocation(SetupListener.right, "center-area.loc2");
            this.config.save();
            sender.sendMessage("§a中心区设置成功!");
            return;
        }
        if (args[0].equalsIgnoreCase("addwall")) {
            if (SetupListener.left == null || SetupListener.right == null) {
                sender.sendMessage("§c请先选择区域!");
                return;
            }
            int id = new Random().nextInt(100000);
            this.config.setBlockLocation(SetupListener.left, "walls." + id + ".loc1");
            this.config.setBlockLocation(SetupListener.right, "walls." + id + ".loc2");
            this.config.save();
            sender.sendMessage("§a墙添加成功!");
        }
    }

    @Override
    public boolean isOnlyPlayerExecutable() {
        return true;
    }
}

