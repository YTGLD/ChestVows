package com.ytgld.chest_vows.mixin;

import com.ytgld.chest_item.other.ChestItemMenu;
import com.ytgld.chest_item.other.ChestMenuScreen;
import com.ytgld.chest_vows.other.CVAttReg;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ChestMenuScreen.class)
public abstract class ChestMenuScreenMixin extends AbstractContainerScreen<ChestItemMenu> {
    @Shadow
    @Final
    private Player player;

    public ChestMenuScreenMixin(ChestItemMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    @Unique
    public void chestVows$renderVows(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick){
        Set<String> strings = player.getData(CVAttReg.vows);

        int i = 0;
        for (String string : strings){
            i++;
            int guiLeft = (this.width - this.imageWidth) / 2 + 16 * (i - 1);
            int guiTop = (this.height - this.imageHeight) / 2 - 16;
            guiGraphics.renderItem(BuiltInRegistries.ITEM.get(ResourceLocation.parse(string)).getDefaultInstance(),
                    guiLeft, guiTop);
            int appleSize = 16;
            if (mouseX >= guiLeft && mouseX < guiLeft + appleSize && mouseY >= guiTop && mouseY < guiTop + appleSize) {
                guiGraphics.renderTooltip(this.font, BuiltInRegistries.ITEM.get(ResourceLocation.parse(string)).getDefaultInstance(), mouseX, mouseY);
            }
        }
    }
    @Inject(method = "render", at = @At(value = "RETURN"))
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        this.chestVows$renderVows(guiGraphics, mouseX, mouseY, partialTick);
    }
}
