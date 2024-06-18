/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.classes;

public class ClassesInfo {

    public static enum Orientation {
        WARRIOR("§2Warrior"),
        HURT("§cHurt"),
        TANK("§1Tank"),
        ASSIST("§dAssist"),
        AGILITY("§bAgility"),
        CHARGER("§4Charger"),
        REMOTE("§3Remote"),
        CONTROL("§6Control");

        private String text;

        private Orientation(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }
    }

    public static enum Difficulty {
        ONE("§a●§7●●●", 1),
        TWO("§e●●§7●●", 2),
        THREE("§c●●●§7●", 3),
        FOUR("§4●●●●", 4);

        private String text;
        private int priority;

        private Difficulty(String text, int priority) {
            this.text = text;
            this.priority = priority;
        }

        public String getText() {
            return this.text;
        }

        public int getPriority() {
            return this.priority;
        }
    }
}

