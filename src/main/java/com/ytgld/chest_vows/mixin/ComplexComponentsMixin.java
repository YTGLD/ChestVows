package com.ytgld.chest_vows.mixin;

import com.ytgld.chest_item.items.reinforced.meat.ComplexComponents;
import com.ytgld.chest_vows.items.vows.blood.BloodSacrifice;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ComplexComponents.class)
public class ComplexComponentsMixin {
    @Inject(method = "absorptionDamage", at = @At(value = "RETURN"), cancellable = true)
    private static void absorptionDamage(LivingEntity living, CallbackInfoReturnable<Boolean> cir) {
        BloodSacrifice.mixinHyperplasia(living,cir);
    }
}
