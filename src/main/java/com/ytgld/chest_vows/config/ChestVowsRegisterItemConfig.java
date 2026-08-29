package com.ytgld.chest_vows.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public interface ChestVowsRegisterItemConfig {
    void config(ModConfigSpec.Builder builder);
    List<CIString> theLanguageProvider();
    default String theCategory(){
        return "";
    };

    record CIString(String path, String  doIt,String doName){

    }
}