package com.ytgld.chest_vows.items;

import com.ytgld.chest_item.items.evil_mother.IEvil;
import com.ytgld.chest_item.renderer.light.Light;

public abstract class EvilVow extends BaseVows{
    public EvilVow(Properties properties) {
        super(properties);
    }

    @Override
    public int backColor() {
        return IEvil.color;
    }

    public static final String evilVow = "EvilVow";
}
