/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package cyan.thegoodboys.megawalls.tellraw;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public abstract class ItemSerialize {
    static ItemSerialize itemSerialize;

    static {
        try {
            itemSerialize = new Automatic();
        } catch (IllegalStateException e) {
            itemSerialize = new Manual();
        }
    }

    public static String $(ItemStack item) {
        return itemSerialize.parse(item);
    }

    public abstract String parse(ItemStack var1);

    public abstract String getName();

    static class Manual
            extends ItemSerialize {
        Manual() {
        }

        @Override
        public String getName() {
            return "Manual";
        }

        @Override
        public String parse(ItemStack item) {
            return this.serialize(item);
        }

        private String getDisplay(ItemMeta im) {
            StringBuilder display = new StringBuilder();
            display.append("{");
            if (im.hasDisplayName()) {
                display.append(String.format("Name:\"%s\",", im.getDisplayName()));
            }
            if (im.hasLore()) {
                display.append("Lore:[");
                int i = 0;
                for (String line : im.getLore()) {
                    display.append(String.format("%s:\"%s\",", i, new JsonBuilder(line).toString()));
                    ++i;
                }
                display.deleteCharAt(display.length() - 1);
                display.append("],");
            }
            display.deleteCharAt(display.length() - 1);
            display.append("}");
            return display.toString();
        }

        private String getEnch(Set<Map.Entry<Enchantment, Integer>> set) {
            StringBuilder enchs = new StringBuilder();
            for (Map.Entry<Enchantment, Integer> ench : set) {
                enchs.append(String.format("{id:%s,lvl:%s},", ench.getKey().getId(), ench.getValue()));
            }
            enchs.deleteCharAt(enchs.length() - 1);
            return enchs.toString();
        }

        private String getTag(ItemMeta im) {
            StringBuilder meta = new StringBuilder("{");
            if (im.hasEnchants()) {
                meta.append(String.format("ench:[%s],", this.getEnch(im.getEnchants().entrySet())));
            }
            if (im.hasDisplayName() || im.hasLore()) {
                meta.append(String.format("display:%s,", this.getDisplay(im)));
            }
            meta.deleteCharAt(meta.length() - 1);
            meta.append("}");
            return meta.toString();
        }

        private String serialize(ItemStack item) {
            StringBuilder json = new StringBuilder("{");
            json.append(String.format("id:\"%s\",Damage:\"%s\"", item.getTypeId(), item.getDurability()));
            if (item.getAmount() > 1) {
                json.append(String.format(",Count:%s", item.getAmount()));
            }
            if (item.hasItemMeta()) {
                json.append(String.format(",tag:%s", this.getTag(item.getItemMeta())));
            }
            json.append("}");
            return json.toString();
        }
    }

    static class Automatic
            extends ItemSerialize {
        private static boolean inited = false;
        private static Method asNMSCopyMethod;
        private static Method nmsSaveNBTMethod;
        private static Class<?> nmsNBTTagCompound;
        private static String ver;

        static {
            ver = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            try {
                Class cis = Automatic.getOBCClass("inventory.CraftItemStack");
                asNMSCopyMethod = cis.getMethod("asNMSCopy", ItemStack.class);
                Class<?> nmsItemStack = asNMSCopyMethod.getReturnType();
                for (Method method : nmsItemStack.getMethods()) {
                    Class<?> rt = method.getReturnType();
                    if (method.getParameterTypes().length != 0 || !"NBTTagCompound".equals(rt.getSimpleName()))
                        continue;
                    nmsNBTTagCompound = rt;
                }
                for (Method method : nmsItemStack.getMethods()) {
                    Class<?>[] paras = method.getParameterTypes();
                    Class<?> rt = method.getReturnType();
                    if (paras.length != 1 || !"NBTTagCompound".equals(paras[0].getSimpleName()) || !"NBTTagCompound".equals(rt.getSimpleName()))
                        continue;
                    nmsSaveNBTMethod = method;
                }
                inited = true;
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public Automatic() {
            if (!inited) {
                throw new IllegalStateException("\u65e0\u6cd5\u521d\u59cb\u5316\u81ea\u52a8\u5904\u7406\u7c7b!");
            }
        }

        private static Class getOBCClass(String cname) throws ClassNotFoundException {
            return Class.forName("org.bukkit.craftbukkit." + ver + "." + cname);
        }

        @Override
        public String getName() {
            return "Automatic";
        }

        @Override
        public String parse(ItemStack item) {
            try {
                return nmsSaveNBTMethod.invoke(asNMSCopyMethod.invoke(null, item), nmsNBTTagCompound.newInstance()).toString();
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException |
                     InvocationTargetException e) {
                itemSerialize = new Manual();
                return itemSerialize.parse(item);
            }
        }
    }
}

