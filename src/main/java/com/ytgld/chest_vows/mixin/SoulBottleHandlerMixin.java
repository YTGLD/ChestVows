package com.ytgld.chest_vows.mixin;

import com.ytgld.chest_item.event.SoulBottleHandler;
import com.ytgld.chest_vows.event.SpiritSoulHandler;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoulBottleHandler.class)
public class SoulBottleHandlerMixin {
    @Inject(method = "spiritSoul", at = @At(value = "HEAD"),cancellable = true)
    private static void spiritSoul(LivingDeathEvent event, CallbackInfo ci) {
        SpiritSoulHandler.mixinChestSoulHandler(event, ci);
    }
}
