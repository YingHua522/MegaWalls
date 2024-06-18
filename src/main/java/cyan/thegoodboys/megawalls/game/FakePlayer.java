/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.gmail.filoghost.holographicdisplays.api.Hologram
 *  com.gmail.filoghost.holographicdisplays.api.HologramsAPI
 *  net.citizensnpcs.api.CitizensAPI
 *  net.citizensnpcs.api.npc.NPC
 *  net.citizensnpcs.api.trait.trait.Equipment
 *  net.citizensnpcs.api.trait.trait.Equipment$EquipmentSlot
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package cyan.thegoodboys.megawalls.game;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import cyan.thegoodboys.megawalls.MegaWalls;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class FakePlayer {
    private static final Map<GamePlayer, FakePlayer> fakePlayerMap = new HashMap<>();
    private static final List<GamePlayer> list = new ArrayList<>();
    private final GamePlayer gamePlayer;
    private final Location location;
    private final List<ItemStack> drops = new ArrayList<>();
    private final NPC npc;
    private final Hologram hologram;
    private boolean killed = false;

    public FakePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = Objects.requireNonNull(gamePlayer, "gamePlayer cannot be null");
        Player player = this.gamePlayer.getPlayer();
        this.location = player.getLocation().clone();
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                this.drops.add(itemStack);
            }
        }
        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                this.drops.add(itemStack);
            }
        }
        this.hologram = HologramsAPI.createHologram(MegaWalls.getInstance(), this.location.add(0.0, 3.0, 0.0));
        this.hologram.appendTextLine(this.gamePlayer.getGameTeam().getTeamColor().getChatColor() + "§l已登出");
        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, this.gamePlayer.getUuid(), new Random().nextInt(100000), this.gamePlayer.getName());
        this.npc.setName(this.gamePlayer.getGameTeam().getTeamColor().getChatColor() + this.gamePlayer.getGameTeam().getTeamColor().getTag() + " " + this.gamePlayer.getName());
        this.npc.spawn(this.location);
        Equipment trait = this.npc.getTrait(Equipment.class);
        trait.set(Equipment.EquipmentSlot.HELMET, player.getInventory().getHelmet());
        trait.set(Equipment.EquipmentSlot.CHESTPLATE, player.getInventory().getChestplate());
        trait.set(Equipment.EquipmentSlot.LEGGINGS, player.getInventory().getLeggings());
        trait.set(Equipment.EquipmentSlot.BOOTS, player.getInventory().getBoots());
        trait.set(Equipment.EquipmentSlot.HAND, player.getItemInHand());
        gamePlayer.setInventoryContents(player.getInventory().getContents());
        fakePlayerMap.put(this.gamePlayer, this);
        list.add(this.gamePlayer);
        MegaWalls.updateRejoin(this.gamePlayer, MegaWalls.getInstance().getConfig().getString("servername"), System.currentTimeMillis() + 3600000L);
    }

    public static FakePlayer getFakePlayer(NPC npc) {
        for (FakePlayer fakePlayer : fakePlayerMap.values()) {
            if (fakePlayer.getNpc().equals(npc)) {
                return fakePlayer;
            }
        }
        return null;
    }

    public static Map<GamePlayer, FakePlayer> getFakePlayerMap() {
        return Collections.unmodifiableMap(fakePlayerMap);
    }

    public static List<GamePlayer> getList() {
        return Collections.unmodifiableList(list);
    }

    public void teleport(Location location) {
        this.npc.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        this.hologram.teleport(location.add(0.0, 3.0, 0.0));
    }

    public void delete() {
        this.drops.clear();
        this.npc.destroy();
        this.hologram.delete();
        fakePlayerMap.remove(this);
    }

    public GamePlayer getGamePlayer() {
        return this.gamePlayer;
    }

    public boolean isKilled() {
        return this.killed;
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    public Location getLocation() {
        return this.location;
    }

    public List<ItemStack> getDrops() {
        return this.drops;
    }

    public NPC getNpc() {
        return this.npc;
    }

    public Hologram getHologram() {
        return this.hologram;
    }
}
