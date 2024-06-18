/*
 * Decompiled with CFR 0.152.
 */
package cyan.thegoodboys.megawalls.stats;

public class CurrencyPackage {
    private int currency;
    private StringBuffer tip;
    private double boost = 0.0;

    public CurrencyPackage(int currency, String tip) {
        this.currency = currency;
        this.tip = new StringBuffer(tip == null ? "" : tip);
    }

    public void appendTip(String text) {
        this.tip.append(text);
    }

    public String getTip() {
        return this.tip.toString();
    }

    public boolean isBoosted() {
        return this.boost > 0.0;
    }

    public void booster(double boost, String boostReason) {
        this.boost += boost;
        this.appendTip(" " + boostReason + "x" + (boost + 1.0) + "\u500d");
    }

    public int getCurrency() {
        return this.boost == 0.0 ? this.currency : (int) ((double) this.currency * this.boost) + this.currency;
    }

    public double getBoost() {
        return this.boost;
    }
}

