package com.ytgld.chest_vows.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ytgld.chest_vows.entity.TheSpirit;
import com.ytgld.chest_vows.entity.state.TheSpiritRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;


public class TheSpiritRender extends EntityRenderer<@NotNull TheSpirit, TheSpiritRenderState> {
    private final ItemModelResolver itemModelResolver;

    public TheSpiritRender(EntityRendererProvider.Context context) {
        super(context);
        itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public boolean shouldRender(TheSpirit entity, Frustum culler, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void submit(TheSpiritRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);

        TheSpirit entity = state.entity;
        double x = Mth.lerp(state.partialTick, entity.xOld, entity.getX());
        double y = Mth.lerp((double)state.partialTick, entity.yOld, entity.getY());
        double z = Mth.lerp((double)state.partialTick, entity.zOld, entity.getZ());

        poseStack.pushPose();
        poseStack.translate(entity.getX() - x, entity.getY() - y, entity.getZ() - z);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.tickCount));
        if (!state.item.isEmpty()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
            state.item.submit(poseStack, submitNodeCollector, 255, OverlayTexture.NO_OVERLAY, state.outlineColor);
        }
        poseStack.popPose();
    }

    @Override
    public void extractRenderState(TheSpirit entity, TheSpiritRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.entity = entity;
        reusedState.partialTick = partialTick;
        this.itemModelResolver.updateForNonLiving(reusedState.item,
                entity.getItem(), ItemDisplayContext.FIXED, entity);
    }
    @Override
    public TheSpiritRenderState createRenderState() {
        return new TheSpiritRenderState();
    }
}
