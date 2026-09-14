package com.ytgld.chest_vows.render.particle.has_opt;

import com.ytgld.chest_item.renderer.MRender;
import com.ytgld.chest_item.renderer.light.Light;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;

public class MagicChestParticle extends SingleQuadParticle {
    public boolean isLight= false;
    public int color = Light.ARGB.color(255,255,255,255);
    public float size = 2;

    public MagicChestParticle(ClientLevel level, double x, double y, double z, float movementX, float movementY, float movementZ, TextureAtlasSprite textureAtlasSprite) {
        super(level,x,y,z,movementX,movementY,movementZ,textureAtlasSprite);
        this.setParticleSpeed(movementX,movementY,movementZ);
        this.lifetime = 100;
        this.scale(size);
    }

    @Override
    protected int getLightCoords(float a) {
        return 255;
    }

    public void tick() {
        super.tick();
        this.roll+=0.05f + Mth.nextFloat(this.random,0.01F,0.2F);
        this.oRoll+= (float) (0.05 + Mth.nextFloat(this.random,0.01F,0.2F));
        if (alpha>0) {
            this.alpha -= 0.05f;
        }
        if (alpha <= 0.0) {
            this.remove();
        }
    }

    @Override
    protected @NotNull Layer getLayer() {
        return new Layer(true, TextureAtlas.LOCATION_PARTICLES,
                MRender.RenderPs.TRANSLUCENT_PARTICLE);
    }
    public record Provider(SpriteSet sprite) implements ParticleProvider<MagicColorOption> {
        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }
        public SpriteSet sprite() {
            return this.sprite;
        }

        @Override
        public @NotNull Particle createParticle(MagicColorOption simpleParticleType, ClientLevel clientLevel, double v, double v1, double v2, double v3, double v4, double v5,RandomSource randomSource) {
            MagicChestParticle particle = new MagicChestParticle(clientLevel, v,v1,v2, (float) v3, (float) v4, (float) v5,sprite.get(randomSource));
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