package com.ytgld.chest_vows.mixin.client;

import com.ytgld.chest_item.renderer.book.ChaosBookScreen;
import com.ytgld.chest_vows.items.BaseVows;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChaosBookScreen.class)
public class ChaosBookScreenMixin {
    @Inject(at = @At(value = "HEAD"),method = "itemImage", cancellable = true)
    private static void item(Item item, CallbackInfoReturnable<Identifier> cir) {
        if (item instanceof BaseVows vows) {
            Identifier image = vows.colorAndImage().getFirst().image();
            cir.setReturnValue(image);
        }
    }
}
