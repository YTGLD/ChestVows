package com.ytgld.chest_vows.mixin.common;

import com.ytgld.chest_vows.other.IAttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AttributeInstance.class)
public class AttributeInstanceMixin implements IAttributeInstance {
    @Shadow
    private double cachedValue;

    @Override
    public void chestVows26_2$setValue(double set) {
        this.cachedValue = set;
    }
}
