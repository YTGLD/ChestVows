package com.ytgld.chest_vows.mixin.client;

import com.ytgld.chest_item.renderer.MRender;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.items.BaseVows;
import com.ytgld.chest_vows.render.RenderVowItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import org.joml.Matrix3x2fStack;
import org.joml.Vector2ic;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsMixin {
    @Shadow
    public abstract int guiWidth();

    @Shadow
    public abstract int guiHeight();

    @Shadow
    private ItemStack tooltipStack;

    @Shadow
    @Final
    private Matrix3x2fStack pose;

    @Inject(at = @At(value = "HEAD"),method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V")
    public void item(LivingEntity owner, Level level, ItemStack itemStack, int x, int y, int seed, CallbackInfo ci) {
        GuiGraphicsExtractor guiGraphicsExtractor = (GuiGraphicsExtractor) (Object) this;
        RenderVowItem.renderItem(guiGraphicsExtractor,pose,itemStack,x,y,seed);
    }
    @Inject(at = @At(value = "RETURN"),method = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;tooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;Lnet/minecraft/resources/Identifier;ZLnet/minecraft/world/item/ItemStack;)V")
    public void chest_vows$ClientTooltipPositioner(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, boolean extraSpaceAfterFirstLine, ItemStack tooltipStack, CallbackInfo ci) {
        GuiGraphicsExtractor guiGraphicsExtractor = (GuiGraphicsExtractor) (Object) this;

        if (tooltipStack.getItem() instanceof BaseVows) {
            RenderTooltipEvent.Pre preEvent = ClientHooks.onRenderTooltipPre(tooltipStack, guiGraphicsExtractor, xo, yo, this.guiWidth(), this.guiHeight(), lines, font, positioner);

            int i = 0;
            int j = lines.size() == 1 ? -2 : 0;

            for (ClientTooltipComponent clienttooltipcomponent : lines) {
                int k = clienttooltipcomponent.getWidth(preEvent.getFont());
                if (k > i) {
                    i = k;
                }

                j += clienttooltipcomponent.getHeight(font);
            }

            int i2 = i;
            int j2 = j;

            Vector2ic vector2ic = positioner.positionTooltip(this.guiWidth(), this.guiHeight(), preEvent.getX(), preEvent.getY(), i2, j2);

            int l = vector2ic.x();
            int i1 = vector2ic.y();
            this.pose.pushMatrix();
            if (tooltipStack.getItem() instanceof BaseVows vows) {
                chestVows26_2$renderBack((GuiGraphicsExtractor) (Object) this, l, i1, i, j,vows.backColor());

            }
            pose.popMatrix();
        }
    }
    @Unique
    public void chestVows26_2$renderBack(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height
            , Identifier farmer, Identifier back , int colorF, int colorB) {
        int i = x - 3 - 9;
        int j = y - 3 - 9;
        int k = width + 3 + 3 + 18;
        int l = height + 3 + 3 + 18;
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, farmer, i, j, k, l,colorF);
        guiGraphics.blitSprite(MRender.RenderPs.GUI_TEXTURED, back, i, j, k, l,colorB);
    }
    @Unique
    public void chestVows26_2$renderBack(GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height,int color) {
        chestVows26_2$renderBack(guiGraphics, x, y, width, height,
                Identifier.fromNamespaceAndPath(ChestVows.MODID, "tooltip/frame"),
                Identifier.fromNamespaceAndPath(ChestVows.MODID, "tooltip/background"),

                Light.ARGB.color(255, 255, 100, 240),
                color
        );
    }
}
