/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.command.CommandException
 *  org.bukkit.command.CommandSender
 */
package cyan.thegoodboys.megawalls.command;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

public abstract class BaseCommand {
    private String name;
    private String permission;

    public BaseCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public final boolean hasPermission(CommandSender sender) {
        if (this.permission == null) {
            return true;
        }
        return sender.hasPermission(this.permission);
    }

    public abstract String getPossibleArguments();

    public abstract int getMinimumArguments();

    public abstract void execute(CommandSender var1, String var2, String[] var3) throws CommandException;

    public abstract boolean isOnlyPlayerExecutable();

    public final boolean isValidTrigger(String name) {
        return this.name.equalsIgnoreCase(name);
    }
}

