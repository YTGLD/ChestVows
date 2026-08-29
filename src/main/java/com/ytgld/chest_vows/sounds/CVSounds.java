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
}
