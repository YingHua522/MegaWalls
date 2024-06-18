/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.apache.commons.lang3.builder.EqualsBuilder
 *  org.apache.commons.lang3.builder.HashCodeBuilder
 */
package cyan.thegoodboys.megawalls.inv.content;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SlotPos {
    private final int row;
    private final int column;

    public SlotPos(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public static SlotPos of(int row, int column) {
        return new SlotPos(row, column);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        SlotPos slotPos = (SlotPos) object;
        return new EqualsBuilder().append(this.row, slotPos.row).append(this.column, slotPos.column).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.row).append(this.column).toHashCode();
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }
}

