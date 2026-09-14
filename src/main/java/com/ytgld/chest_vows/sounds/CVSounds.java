package com.ytgld.chest_vows.sounds;

import com.ytgld.chest_vows.ChestVows;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CVSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY =
            DeferredRegister.create(Registries.SOUND_EVENT, ChestVows.MODID);

    public static final Holder<SoundEvent> use_vows = REGISTRY.register(
            "use_vows",
            SoundEvent::createVariableRangeEvent
    );
    public static final Holder<SoundEvent> soul_pickup =
            REGISTRY.register("soul_pickup",SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> soul_create =
            REGISTRY.register("soul_create",SoundEvent::createVariableRangeEvent);

    public static final Holder<SoundEvent> soul_fly =
            REGISTRY.register("soul_fly",SoundEvent::createVariableRangeEvent);

}
