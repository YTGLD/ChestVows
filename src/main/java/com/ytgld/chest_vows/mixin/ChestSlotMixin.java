package com.ytgld.chest_vows.mixin;

import com.ytgld.chest_item.other.ChestSlot;
import com.ytgld.chest_vows.items.BaseVows;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestSlot.class)
public class ChestSlotMixin {
    @Inject(method = "mayPlace", at = @At(value = "RETURN"), cancellable = true)
    private static void absorptionDamage(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof BaseVows vowsP) {
            cir.setReturnValue(false);
        }
    }
}
