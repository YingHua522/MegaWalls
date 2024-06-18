/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 */
package cyan.thegoodboys.megawalls.scoreboard;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;

public class FixedBody implements Body {
    private final LineListBuilder builder;

    private FixedBody(LineListBuilder builder) {
        this.builder = builder;
    }

    public static List<LinePair> getFixedList(List<Line> list) {
        int size = list.size();
        ImmutableList.Builder b = ImmutableList.builder();
        for (Line line : list) {
            b.add((Object) LinePair.of(line, size--));
        }
        return b.build();
    }

    public static FixedBody of(Line... list) {
        return of(Arrays.asList(list));
    }

    public static FixedBody of(List<Line> list) {
        return of(() -> list);
    }

    public static FixedBody of(LineListBuilder builder) {
        return new FixedBody(builder);
    }

    @Override
    public List<LinePair> getList() {
        return FixedBody.getFixedList(this.builder.build());
    }
}

