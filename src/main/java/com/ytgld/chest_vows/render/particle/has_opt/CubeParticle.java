package com.ytgld.chest_vows.render.particle.has_opt;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.ytgld.chest_item.renderer.CIStateShardsHasBlack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.jetbrains.annotations.NotNull;

public class CubeParticle extends TextureSheetParticle {
    public boolean isLight= false;
    public float size = 2;

    private CubeParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 100;
        this.scale(size);
    }
    @Override
    protected int getLightColor(float partialTick) {
        return 255;
    }
    public void tick() {
        super.tick();

        this.roll+=0.05f;
        this.oRoll+= (float) (0.05) ;
        if (alpha>0) {
            this.alpha -= 0.05f;
        }
        if (alpha <= 0.0) {
            this.remove();
        }
    }
    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return (tesselator, manager) -> {
            RenderSystem.depthMask(false);

            RenderSystem.setShader(CIStateShardsHasBlack::getHasBlock);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);

            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );

            return tesselator.begin(
                    VertexFormat.Mode.QUADS,
                    DefaultVertexFormat.PARTICLE
            );
        };
    }

    public record Provider(SpriteSet sprite) implements ParticleProvider<CubeOption> {
        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }
        public SpriteSet sprite() {
            return this.sprite;
        }

        @Override
        public @NotNull Particle createParticle(CubeOption simpleParticleType, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5) {
            CubeParticle particle = new CubeParticle(clientLevel, v,v1,v2);
            particle.setSpriteFromAge(this.sprite);

            int color = simpleParticleType.getColor();
            int as = (color >> 24) & 0xFF;
            int rs = (color >> 16) & 0xFF;
            int gs = (color >> 8) & 0xFF;
            int bs = color & 0xFF;

            particle.setColor(rs / 255f,gs / 255f,bs / 255f);
            particle.isLight = simpleParticleType.isLight();
            particle.scale(simpleParticleType.getSize());

            particle.setParticleSpeed(simpleParticleType.getVec3().x,simpleParticleType.getVec3().y,simpleParticleType.getVec3().z);
            return particle;
        }
    }
}
