package cyan.thegoodboys.megawalls.skin;


import cyan.thegoodboys.megawalls.skin.Cow.SisterCow;
import cyan.thegoodboys.megawalls.skin.Dreadlord.Dreadlord;
import cyan.thegoodboys.megawalls.skin.herobrine.SnowHeroBrine;

import java.util.HashMap;
import java.util.Map;

public class SkinManager {
    private final Map<String, SkinProvider> providers = new HashMap<>();

    public SkinManager() {
        register("HIM", new SnowHeroBrine());
        register("Dreadlord", new Dreadlord());
        register("Cow",new SisterCow());
    }

    public void register(String className, SkinProvider provider) {
        providers.put(className.toLowerCase(), provider);
    }

    public SkinProvider getProvider(String className) {
        return providers.get(className.toLowerCase());
    }
}
