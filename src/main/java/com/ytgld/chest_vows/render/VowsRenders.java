package com.ytgld.chest_vows.render;

import net.minecraft.client.renderer.ShaderInstance;

public class VowsRenders {
    public static ShaderInstance vows;

    public static void setVows(ShaderInstance vows) {
        VowsRenders.vows = vows;
    }

    public static ShaderInstance getVows() {
        return vows;
    }
}
