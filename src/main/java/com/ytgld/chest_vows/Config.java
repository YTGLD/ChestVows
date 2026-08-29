package com.ytgld.chest_vows;


import com.ytgld.chest_vows.config.ChestVowsConfigPluginFinder;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    private static final Pair<Config, ModConfigSpec> BUILDER = new ModConfigSpec.Builder().configure(Config::new);
    public static Config config = BUILDER.getKey();
    public static ModConfigSpec fc = BUILDER.getRight();

    public Config(ModConfigSpec.Builder builder){

        builder.push("Common");
        {
            for (ChestVowsRegisterItemConfig registerItemConfig : ChestVowsConfigPluginFinder.getModPlugins()){
                if (registerItemConfig.theCategory().isEmpty()) {
                    registerItemConfig.config(builder);
                }else {
                    builder.push(registerItemConfig.theCategory());
                    registerItemConfig.config(builder);
                    builder.pop();

                }
            }

        }
        builder.pop();
    }
}
