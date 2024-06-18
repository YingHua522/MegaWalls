/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.inv;

import org.bukkit.event.Event;

import java.util.function.Consumer;

public class InventoryListener<T> {
    private final Class<T> type;
    private final Consumer<T> consumer;

    public InventoryListener(Class<T> type, Consumer<T> consumer) {
        this.type = type;
        this.consumer = consumer;
    }

    public void accept(Event t) {
        this.consumer.accept((T) t);
    }

    public Class<T> getType() {
        return this.type;
    }

}

