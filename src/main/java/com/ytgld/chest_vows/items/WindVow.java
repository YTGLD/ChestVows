package com.ytgld.chest_vows.items;

import com.ytgld.chest_item.items.evil_mother.IEvil;
import com.ytgld.chest_item.renderer.light.Light;

public abstract class WindVow extends BaseVows{
    public WindVow(Properties properties) {
        super(properties);
    }

    @Override
    public int backColor() {
        return Light.ARGB.color(255,100,100,255);
    }

    public static final String windVow = "WindVow";
}
