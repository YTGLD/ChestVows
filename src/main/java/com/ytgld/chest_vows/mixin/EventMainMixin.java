package com.ytgld.chest_vows.mixin;

import com.ytgld.chest_item.event.use.EventMain;
import com.ytgld.chest_item.other.ChestSlot;
import com.ytgld.chest_vows.items.BaseVows;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EventMain.class)
public class EventMainMixin {
    @Inject(method = "tooltip", at = @At(value = "HEAD"), cancellable = true)
    private void absorptionDamage(ItemTooltipEvent event, CallbackInfo ci) {
        if (event.getItemStack().getItem() instanceof BaseVows vowsP) {
            ci.cancel();
        }
    }
}
