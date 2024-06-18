/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent
 *  net.minecraft.server.v1_8_R3.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_8_R3.Packet
 *  net.minecraft.server.v1_8_R3.PacketPlayOutChat
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.tellraw;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tellraw
        implements Cloneable {
    private List<MessagePart> messageParts = new ArrayList<MessagePart>();
    private String cache;

    public Tellraw(String text) {
        this.messageParts.add(new MessagePart(text));
    }

    public static Tellraw create() {
        return Tellraw.create("");
    }

    public static Tellraw create(String text) {
        return new Tellraw(text);
    }

    public static Tellraw create(String text, Object... objects) {
        return new Tellraw(String.format(text, objects));
    }

    public void broadcast() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.send((CommandSender) player);
        }
    }

    public Tellraw cmd_tip(String command, String... tip) {
        return this.command(command).tip(tip);
    }

    public Tellraw command(String command) {
        return this.onClick("run_command", command);
    }

    public Tellraw file(String path) {
        return this.onClick("open_file", path);
    }

    public Tellraw insertion(String data) {
        this.latest().insertionData = data;
        return this;
    }

    public Tellraw item(ItemStack item) {
        return this.item(ItemSerialize.$(item));
    }

    public Tellraw item(String json) {
        return this.onHover("show_item", json);
    }

    public Tellraw link(String url) {
        return this.onClick("open_url", url);
    }

    public Tellraw openurl(String url) {
        return this.onClick("open_url", url);
    }

    public void send(CommandSender sender) {
        String json = this.toJsonString();
        if (sender instanceof Player && json.getBytes().length < 32000) {
            IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a((String) json);
            PacketPlayOutChat packet = new PacketPlayOutChat(comp);
            ((CraftPlayer) sender).getHandle().playerConnection.sendPacket((Packet) packet);
        } else {
            sender.sendMessage(this.toOldMessageFormat());
        }
    }

    public Tellraw sug_tip(String command, String... tip) {
        return this.suggest(command).tip(tip);
    }

    public Tellraw suggest(String command) {
        return this.onClick("suggest_command", command);
    }

    public Tellraw text(String text) {
        this.latest().text = text;
        return this;
    }

    public Tellraw then(String text) {
        return this.then(new MessagePart(text));
    }

    public Tellraw then(String name, ItemStack item) {
        return this.then(name).item(ItemSerialize.$(item));
    }

    public Tellraw then(String text, Object... objects) {
        return this.then(new MessagePart(String.format(text, objects)));
    }

    public Tellraw tip(List<String> texts) {
        if (texts.isEmpty()) {
            return this;
        }
        StringBuilder text = new StringBuilder();
        for (String t : texts) {
            text.append(t).append("\n");
        }
        return this.tip(text.toString().substring(0, text.length() - 1));
    }

    public Tellraw tip(String text) {
        return this.onHover("show_text", text);
    }

    public Tellraw tip(String... texts) {
        return this.tip(Arrays.asList(texts));
    }

    public String toJsonString() {
        if (this.cache == null) {
            StringBuilder msg = new StringBuilder();
            msg.append("[\"\"");
            for (MessagePart messagePart : this.messageParts) {
                msg.append(",");
                messagePart.writeJson(msg);
            }
            msg.append("]");
            this.cache = msg.toString();
        }
        return this.cache;
    }

    public Tellraw setMessageParts(List<MessagePart> messageParts) {
        this.messageParts = new ArrayList<MessagePart>(messageParts);
        return this;
    }

    public Tellraw clone() throws CloneNotSupportedException {
        return ((Tellraw) super.clone()).setMessageParts(this.messageParts);
    }

    public String toOldMessageFormat() {
        StringBuilder result = new StringBuilder();
        for (MessagePart part : this.messageParts) {
            result.append(part.text);
        }
        return result.toString();
    }

    private MessagePart latest() {
        return this.messageParts.get(this.messageParts.size() - 1);
    }

    private Tellraw onClick(String name, String data) {
        MessagePart latest = this.latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
        return this;
    }

    private Tellraw onHover(String name, String data) {
        MessagePart latest = this.latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
        return this;
    }

    private Tellraw then(MessagePart part) {
        MessagePart last = this.latest();
        if (!last.hasText()) {
            last.text = part.text;
        } else {
            this.messageParts.add(part);
        }
        this.cache = null;
        return this;
    }
}

