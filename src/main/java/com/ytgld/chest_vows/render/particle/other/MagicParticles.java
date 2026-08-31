package com.ytgld.chest_vows.render.particle.other;


import com.mojang.serialization.MapCodec;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.render.particle.has_opt.CubeOption;
import com.ytgld.chest_vows.render.particle.has_opt.MagicColorOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MagicParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ChestVows.MODID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<MagicColorOption>> colorOption =
            PARTICLE_TYPES.register("color_option",
                    () -> new ParticleType<>(false) {
                        @Override
                        public MapCodec<MagicColorOption> codec() {
                            return MagicColorOption.CODEC;
                        }

                        @Override
                        public StreamCodec<? super RegistryFriendlyByteBuf, MagicColorOption> streamCodec() {
                            return MagicColorOption.STREAM_CODEC;
                        }
                    });

    public static final DeferredHolder<ParticleType<?>, ParticleType<CubeOption>> colorCube =
            PARTICLE_TYPES.register("color_cube",
                    () -> new ParticleType<>(false) {
                        @Override
                        public MapCodec<CubeOption> codec() {
                            return CubeOption.CODEC;
                        }

                        @Override
                        public StreamCodec<? super RegistryFriendlyByteBuf, CubeOption> streamCodec() {
                            return CubeOption.STREAM_CODEC;
                        }
                    });
}
