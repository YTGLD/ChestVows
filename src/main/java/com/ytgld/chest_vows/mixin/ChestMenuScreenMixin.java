package com.ytgld.chest_vows.mixin;

import com.ytgld.chest_item.other.ChestItemMenu;
import com.ytgld.chest_item.other.ChestMenuScreen;
import com.ytgld.chest_vows.other.CVAttReg;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public void chestVows$renderVows(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick){
        Set<String> strings = player.getData(CVAttReg.vows);

        int i = 0;
        for (String string : strings){
            i++;
            int guiLeft = (this.width - this.imageWidth) / 2 + 16 * (i - 1);
            int guiTop = (this.height - this.imageHeight) / 2 - 16;

            int appleSize = 16;
            if (mouseX >= guiLeft && mouseX < guiLeft + appleSize && mouseY >= guiTop && mouseY < guiTop + appleSize) {
                Optional<TooltipComponent> image = BuiltInRegistries.ITEM.getValue(Identifier.parse(string)).getDefaultInstance().getTooltipImage();
                List<Component> lines = Screen.getTooltipFromItem(this.minecraft, BuiltInRegistries.ITEM.getValue(Identifier.parse(string)).getDefaultInstance());
                List<ClientTooltipComponent> components = new ArrayList<>();
                image.ifPresent((img) -> components.add(ClientTooltipComponent.create(img)));

                for(Component line : lines) {
                    components.add(ClientTooltipComponent.create(line.getVisualOrderText()));
                }
                guiGraphics.tooltip(this.font, components, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, (Identifier)null, BuiltInRegistries.ITEM.getValue(Identifier.parse(string)).getDefaultInstance());
            }
            guiGraphics.item(BuiltInRegistries.ITEM.getValue(Identifier.parse(string)).getDefaultInstance(),
                    guiLeft, guiTop);
        }
    }
    @Inject(method = "extractRenderState", at = @At(value = "RETURN"))
    private void render(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        this.chestVows$renderVows(guiGraphics, mouseX, mouseY, a);
    }
}
