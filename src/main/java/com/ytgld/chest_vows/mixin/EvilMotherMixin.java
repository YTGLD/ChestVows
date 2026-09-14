package com.ytgld.chest_vows.mixin;

import com.google.common.collect.Multimap;
import com.ytgld.chest_item.items.evil_mother.EvilMother;
import com.ytgld.chest_vows.items.vows.evil.Silence;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EvilMother.class)
public class EvilMotherMixin {
    @Shadow
    private static Multimap<Holder<Attribute>, AttributeModifier> theAttrib(Player player) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Inject(method = "attrib", at = @At(value = "HEAD"), cancellable = true)
    private static void attrib(EntityTickEvent.Post event, CallbackInfo ci) {
        if (event.getEntity() instanceof Player player) {
            Silence.mixinTheAttrib(event, theAttrib(player), ci);
        }
    }
}
