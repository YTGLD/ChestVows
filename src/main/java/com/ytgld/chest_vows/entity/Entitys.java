package com.ytgld.chest_vows.entity;

import com.ytgld.chest_vows.ChestVows;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Entitys {
    public static final DeferredRegister<EntityType<?>> REGISTRY =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ChestVows.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<TheSpirit>> TheSpirit_ = REGISTRY.register("spirit", () ->
            EntityType.Builder.<TheSpirit>of(TheSpirit::new, MobCategory.MISC).sized(0.01f, 0.01f).clientTrackingRange(50).build("spirit"));

}
