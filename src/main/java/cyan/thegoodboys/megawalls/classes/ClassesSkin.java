package cyan.thegoodboys.megawalls.classes;

import java.util.List;

public class ClassesSkin {
    private final String name;
    private final String displayName;
    private final List<String> info;
    private final String value;
    private final String signature;
    private String owner;

    public ClassesSkin(String name, String displayName, List<String> info, String value, String signature, String onwer) {
        this.name = name;
        this.displayName = displayName;
        this.info = info;
        this.value = value;
        this.signature = signature;
        this.owner = onwer;
    }

    public String getName() {
        return this.name;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getInfo() {
        return this.info;
    }

    public String getValue() {
        return this.value;
    }

    public String getSignature() {
        return this.signature;
    }
}

