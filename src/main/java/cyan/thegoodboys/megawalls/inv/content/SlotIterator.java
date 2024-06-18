/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.inv.content;

import cyan.thegoodboys.megawalls.inv.ClickableItem;
import cyan.thegoodboys.megawalls.inv.SmartInventory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public interface SlotIterator {
    Optional<ClickableItem> get();

    SlotIterator set(ClickableItem var1);

    SlotIterator previous();

    SlotIterator next();

    SlotIterator blacklist(int var1, int var2);

    SlotIterator blacklist(SlotPos var1);

    int row();

    SlotIterator row(int var1);

    int column();

    SlotIterator column(int var1);

    boolean started();

    boolean ended();

    boolean doesAllowOverride();

    SlotIterator allowOverride(boolean var1);

    enum Type {
        HORIZONTAL,
        VERTICAL

    }

    class Impl
            implements SlotIterator {
        private final InventoryContents contents;
        private final SmartInventory inv;
        private final Type type;
        private final Set<SlotPos> blacklisted = new HashSet<>();
        private boolean started = false;
        private boolean allowOverride = true;
        private int row;
        private int column;

        public Impl(InventoryContents contents, SmartInventory inv, Type type, int startRow, int startColumn) {
            this.contents = contents;
            this.inv = inv;
            this.type = type;
            this.row = startRow;
            this.column = startColumn;
        }

        public Impl(InventoryContents contents, SmartInventory inv, Type type) {
            this(contents, inv, type, 0, 0);
        }

        @Override
        public Optional<ClickableItem> get() {
            return this.contents.get(this.row, this.column);
        }

        @Override
        public SlotIterator set(ClickableItem item) {
            if (this.canPlace()) {
                this.contents.set(this.row, this.column, item);
            }
            return this;
        }

        @Override
        public SlotIterator previous() {
            if (this.row == 0 && this.column == 0) {
                this.started = true;
                return this;
            }
            do {
                if (!this.started) {
                    this.started = true;
                    continue;
                }
                switch (this.type) {
                    case HORIZONTAL: {
                        --this.column;
                        if (this.column != 0) break;
                        this.column = this.inv.getColumns() - 1;
                        --this.row;
                        break;
                    }
                    case VERTICAL: {
                        --this.row;
                        if (this.row != 0) break;
                        this.row = this.inv.getRows() - 1;
                        --this.column;
                    }
                }
            } while (!this.canPlace() && (this.row != 0 || this.column != 0));
            return this;
        }

        @Override
        public SlotIterator next() {
            if (this.ended()) {
                this.started = true;
                return this;
            }
            do {
                if (!this.started) {
                    this.started = true;
                    continue;
                }
                switch (this.type) {
                    case HORIZONTAL: {
                        ++this.column;
                        this.column %= this.inv.getColumns();
                        if (this.column != 0) break;
                        ++this.row;
                        break;
                    }
                    case VERTICAL: {
                        ++this.row;
                        this.row %= this.inv.getRows();
                        if (this.row != 0) break;
                        ++this.column;
                    }
                }
            } while (!this.canPlace() && !this.ended());
            return this;
        }

        @Override
        public SlotIterator blacklist(int row, int column) {
            this.blacklisted.add(SlotPos.of(row, column));
            return this;
        }

        @Override
        public SlotIterator blacklist(SlotPos slotPos) {
            return this.blacklist(slotPos.getRow(), slotPos.getColumn());
        }

        @Override
        public int row() {
            return this.row;
        }

        @Override
        public SlotIterator row(int row) {
            this.row = row;
            return this;
        }

        @Override
        public int column() {
            return this.column;
        }

        @Override
        public SlotIterator column(int column) {
            this.column = column;
            return this;
        }

        @Override
        public boolean started() {
            return this.started;
        }

        @Override
        public boolean ended() {
            return this.row == this.inv.getRows() - 1 && this.column == this.inv.getColumns() - 1;
        }

        @Override
        public boolean doesAllowOverride() {
            return this.allowOverride;
        }

        @Override
        public SlotIterator allowOverride(boolean override) {
            this.allowOverride = override;
            return this;
        }

        private boolean canPlace() {
            return !this.blacklisted.contains(SlotPos.of(this.row, this.column)) && (this.allowOverride || !this.get().isPresent());
        }
    }
}

