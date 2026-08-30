package com.ytgld.chest_vows.mixin;

import com.ytgld.chest_item.other.ChestMenuScreen;
import com.ytgld.chest_item.renderer.ShieldRenderHandler;
import com.ytgld.chest_vows.items.vows.blood.Symbiosis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShieldRenderHandler.class)
public class ShieldRenderHandlerMixin {

    @Inject(method = "canHeal", at = @At(value = "RETURN"), cancellable = true)
    private static void canHeal(LivingEntity living, CallbackInfoReturnable<Boolean> cir) {
        Symbiosis.mixinCanHeal(living,cir);
    }
    @Inject(method = "tickShield", at = @At(value = "HEAD"), cancellable = true)
    private static void tickShield(LivingEntity living, CallbackInfo ci) {
        Symbiosis.mixinTickShield(living,ci);
    }
}
