package com.ytgld.chest_vows.config;


import com.ytgld.chest_vows.ChestVows;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ChestVowsLanguageProvider extends LanguageProvider {

    public ChestVowsLanguageProvider(PackOutput output) {
        super(output, ChestVows.MODID, "vows_test_lang");
    }

    @Override
    protected void addTranslations() {
        for (ChestVowsRegisterItemConfig registerItemConfig : ChestVowsConfigPluginFinder.getModPlugins()){
            for (ChestVowsRegisterItemConfig.CIString theLanguageProvider : registerItemConfig.theLanguageProvider()) {
                add("chest_vows.configuration." + theLanguageProvider.path(), theLanguageProvider.doIt());
                add("chest_vows.config." + theLanguageProvider.path(), theLanguageProvider.doName());
            }
        }
    }
}