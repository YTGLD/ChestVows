package com.ytgld.chest_vows.items.vows;

import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.Handler;
import com.ytgld.chest_vows.items.BaseVows;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BloodSacrifice extends BaseVows {
    public BloodSacrifice(Properties properties) {
        super(properties);
    }

    @Override
    public String itemName() {
        return Handler.mixinName("blood_sacrifice");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,240,50,70),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/blood_sacrifice.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.blood_sacrifice");
    }
}
