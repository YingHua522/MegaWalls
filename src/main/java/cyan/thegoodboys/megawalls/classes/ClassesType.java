/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.classes;

public enum ClassesType {
    NOVICE("起始", 0),
    NORMAL("普通", 1),
    MYTHIC("神话", 2);

    private final String name;
    private final int priority;

    private ClassesType(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() {
        return this.name;
    }

    public int getPriority() {
        return this.priority;
    }
}

