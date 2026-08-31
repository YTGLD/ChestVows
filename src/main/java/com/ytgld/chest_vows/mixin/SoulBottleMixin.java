package com.ytgld.chest_vows.mixin;

import com.ytgld.chest_item.items.evil_mother.soul.SoulBottle;
import com.ytgld.chest_vows.event.SpiritSoulHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoulBottle.class)
public class SoulBottleMixin {
    @Inject(method = "addSoul", at = @At(value = "HEAD"),cancellable = true)
    private static void render(Player player, ItemStack spiritItem, CallbackInfo ci) {

        SpiritSoulHandler.mixinSoulBottle(ci);
    }
}
