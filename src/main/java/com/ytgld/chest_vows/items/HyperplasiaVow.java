package com.ytgld.chest_vows.items;

import com.ytgld.chest_item.renderer.light.Light;

public abstract class HyperplasiaVow extends BaseVows{
    public HyperplasiaVow(Properties properties) {
        super(properties);
    }

    @Override
    public int backColor() {
        return Light.ARGB.color(255,220,200,85);
    }

    public static final String hyperplasiaVow = "HyperplasiaVow";

}
