package com.ytgld.chest_vows.sounds;

import com.ytgld.chest_vows.ChestVows;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ChestVowsSoundProvider extends SoundDefinitionsProvider {
    public ChestVowsSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, ChestVows.MODID, helper);
    }
    @Override
    public void registerSounds() {
        add(CVSounds.use_vows.value(), SoundDefinition.definition()
                .with(sound("chest_vows:use_vows",SoundDefinition.SoundType.SOUND)
                                .stream(true)
                                .preload(false)
                )
                .subtitle("sound.chest_vows.use_vows")
                .replace(true)
        );
    }
}
