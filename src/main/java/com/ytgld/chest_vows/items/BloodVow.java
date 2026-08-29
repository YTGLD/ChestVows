package com.ytgld.chest_vows.items;

import com.ytgld.chest_item.renderer.light.Light;

public abstract class BloodVow  extends BaseVows{
    public BloodVow(Properties properties) {
        super(properties);
    }

    @Override
    public int backColor() {
        return Light.ARGB.color(255,240,20,72);
    }

    public static final String bloodVow = "BloodVow";
}
