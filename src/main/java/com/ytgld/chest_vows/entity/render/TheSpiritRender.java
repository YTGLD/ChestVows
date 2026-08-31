package com.ytgld.chest_vows.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.entity.TheSpirit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;


public class TheSpiritRender extends EntityRenderer<TheSpirit> {

    public TheSpiritRender(EntityRendererProvider.Context p_173917_) {
        super(p_173917_);
    }

    public boolean shouldRender(TheSpirit livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(TheSpirit theSpirit) {
        return ResourceLocation.parse("aaa");
    }

    @Override
    public void render(TheSpirit entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        double x = Mth.lerp(partialTick, entity.xOld, entity.getX());
        double y = Mth.lerp((double)partialTick, entity.yOld, entity.getY());
        double z = Mth.lerp((double)partialTick, entity.zOld, entity.getZ());
        poseStack.pushPose();
        poseStack.translate(entity.getX() - x, entity.getY() - y, entity.getZ() - z);
        if (entity.canSee) {
            poseStack.mulPose(Axis.YP.rotationDegrees(entity.tickCount));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack stack = entity.getItem();
            BakedModel model = itemRenderer.getModel(stack, Minecraft.getInstance().level, null, 0);
            itemRenderer.render(stack, ItemDisplayContext.NONE, false, poseStack, bufferSource, Minecraft.getInstance().getEntityRenderDispatcher().getPackedLightCoords(entity, 0.0F), OverlayTexture.NO_OVERLAY, model);

        }
        poseStack.popPose();
    }
}

