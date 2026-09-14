package com.ytgld.chest_vows.items;

import com.ytgld.chest_item.renderer.light.Light;

public abstract class MagicVow  extends BaseVows{
    public MagicVow(Properties properties) {
        super(properties);
    }

    @Override
    public int backColor() {
        return Light.ARGB.color(255,100,50,255);
    }

    public static final String magicVow = "MagicVow";

}

