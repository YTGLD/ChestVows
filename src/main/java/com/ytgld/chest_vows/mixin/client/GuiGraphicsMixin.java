package com.ytgld.chest_vows.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ytgld.chest_vows.render.RenderVowItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow
    @Final
    private PoseStack pose;
    @Inject(at = @At(value = "HEAD"),method = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V")
    public void renderItem(LivingEntity entity, Level level, ItemStack stack, int x, int y, int seed, int guiOffset, CallbackInfo ci) {
        GuiGraphics guiGraphicsExtractor = (GuiGraphics) (Object) this;
        RenderVowItem.renderItem(guiGraphicsExtractor,pose,stack,x,y,seed);
    }
}
