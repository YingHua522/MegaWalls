/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package cyan.thegoodboys.megawalls.inv.content;

import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface InventoryContents {
    SmartInventory inventory();

    Pagination pagination();

    Optional<SlotIterator> iterator(String var1);

    SlotIterator newIterator(String var1, SlotIterator.Type var2, int var3, int var4);

    SlotIterator newIterator(SlotIterator.Type var1, int var2, int var3);

    SlotIterator newIterator(String var1, SlotIterator.Type var2, SlotPos var3);

    SlotIterator newIterator(SlotIterator.Type var1, SlotPos var2);

    ClickableItem[][] all();

    Optional<SlotPos> firstEmpty();

    Optional<ClickableItem> get(int var1, int var2);

    Optional<ClickableItem> get(SlotPos var1);

    InventoryContents set(int var1, int var2, ClickableItem var3);

    InventoryContents set(SlotPos var1, ClickableItem var2);

    InventoryContents add(ClickableItem var1);

    InventoryContents fill(ClickableItem var1);

    InventoryContents fillRow(int var1, ClickableItem var2);

    InventoryContents fillColumn(int var1, ClickableItem var2);

    InventoryContents fillBorders(ClickableItem var1);

    InventoryContents fillRect(int var1, int var2, int var3, int var4, ClickableItem var5);

    InventoryContents fillRect(SlotPos var1, SlotPos var2, ClickableItem var3);

    <T> T property(String var1);

    <T> T property(String var1, T var2);

    InventoryContents setProperty(String var1, Object var2);

    class Impl
            implements InventoryContents {
        private final SmartInventory inv;
        private final Player player;
        private final ClickableItem[][] contents;
        private final Pagination pagination = new Pagination.Impl();
        private final Map<String, SlotIterator> iterators = new HashMap<>();
        private final Map<String, Object> properties = new HashMap<>();

        public Impl(SmartInventory inv, Player player) {
            this.inv = inv;
            this.player = player;
            this.contents = new ClickableItem[inv.getRows()][inv.getColumns()];
        }

        @Override
        public SmartInventory inventory() {
            return this.inv;
        }

        @Override
        public Pagination pagination() {
            return this.pagination;
        }

        @Override
        public Optional<SlotIterator> iterator(String id) {
            return Optional.ofNullable(this.iterators.get(id));
        }

        @Override
        public SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn) {
            SlotIterator.Impl iterator = new SlotIterator.Impl(this, this.inv, type, startRow, startColumn);
            this.iterators.put(id, iterator);
            return iterator;
        }

        @Override
        public SlotIterator newIterator(String id, SlotIterator.Type type, SlotPos startPos) {
            return this.newIterator(id, type, startPos.getRow(), startPos.getColumn());
        }

        @Override
        public SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn) {
            return new SlotIterator.Impl(this, this.inv, type, startRow, startColumn);
        }

        @Override
        public SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos) {
            return this.newIterator(type, startPos.getRow(), startPos.getColumn());
        }

        @Override
        public ClickableItem[][] all() {
            return this.contents;
        }

        @Override
        public Optional<SlotPos> firstEmpty() {
            for (int row = 0; row < this.contents.length; ++row) {
                for (int column = 0; column < this.contents[0].length; ++column) {
                    if (this.get(row, column).isPresent()) continue;
                    return Optional.of(new SlotPos(row, column));
                }
            }
            return Optional.empty();
        }

        @Override
        public Optional<ClickableItem> get(int row, int column) {
            if (row >= this.contents.length) {
                return Optional.empty();
            }
            if (column >= this.contents[row].length) {
                return Optional.empty();
            }
            return Optional.ofNullable(this.contents[row][column]);
        }

        @Override
        public Optional<ClickableItem> get(SlotPos slotPos) {
            return this.get(slotPos.getRow(), slotPos.getColumn());
        }

        @Override
        public InventoryContents set(int row, int column, ClickableItem item) {
            if (row >= this.contents.length) {
                return this;
            }
            if (column >= this.contents[row].length) {
                return this;
            }
            this.contents[row][column] = item;
            this.update(row, column, item != null ? item.getItem() : null);
            return this;
        }

        @Override
        public InventoryContents set(SlotPos slotPos, ClickableItem item) {
            return this.set(slotPos.getRow(), slotPos.getColumn(), item);
        }

        @Override
        public InventoryContents add(ClickableItem item) {
            for (int row = 0; row < this.contents.length; ++row) {
                for (int column = 0; column < this.contents[0].length; ++column) {
                    if (this.contents[row][column] != null) continue;
                    this.set(row, column, item);
                    return this;
                }
            }
            return this;
        }

        @Override
        public InventoryContents fill(ClickableItem item) {
            for (int row = 0; row < this.contents.length; ++row) {
                for (int column = 0; column < this.contents[row].length; ++column) {
                    this.set(row, column, item);
                }
            }
            return this;
        }

        @Override
        public InventoryContents fillRow(int row, ClickableItem item) {
            if (row >= this.contents.length) {
                return this;
            }
            for (int column = 0; column < this.contents[row].length; ++column) {
                this.set(row, column, item);
            }
            return this;
        }

        @Override
        public InventoryContents fillColumn(int column, ClickableItem item) {
            for (int row = 0; row < this.contents.length; ++row) {
                this.set(row, column, item);
            }
            return this;
        }

        @Override
        public InventoryContents fillBorders(ClickableItem item) {
            this.fillRect(0, 0, this.inv.getRows() - 1, this.inv.getColumns() - 1, item);
            return this;
        }

        @Override
        public InventoryContents fillRect(int fromRow, int fromColumn, int toRow, int toColumn, ClickableItem item) {
            for (int row = fromRow; row <= toRow; ++row) {
                for (int column = fromColumn; column <= toColumn; ++column) {
                    if (row != fromRow && row != toRow && column != fromColumn && column != toColumn) continue;
                    this.set(row, column, item);
                }
            }
            return this;
        }

        @Override
        public InventoryContents fillRect(SlotPos fromPos, SlotPos toPos, ClickableItem item) {
            return this.fillRect(fromPos.getRow(), fromPos.getColumn(), toPos.getRow(), toPos.getColumn(), item);
        }

        @Override
        public <T> T property(String name) {
            return (T) this.properties.get(name);
        }

        @Override
        public <T> T property(String name, T def) {
            return (T) (this.properties.getOrDefault(name, def));
        }

        @Override
        public InventoryContents setProperty(String name, Object value) {
            this.properties.put(name, value);
            return this;
        }

        private void update(int row, int column, ItemStack item) {
            if (!this.inv.getManager().getOpenedPlayers(this.inv).contains(this.player)) {
                return;
            }
            Inventory topInventory = this.player.getOpenInventory().getTopInventory();
            topInventory.setItem(this.inv.getColumns() * row + column, item);
        }
    }
}

