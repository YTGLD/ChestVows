package com.ytgld.chest_vows.other;

import com.ytgld.chest_vows.ChestVows;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class VowsDamageTypeTagsProvider extends TagsProvider<DamageType> {
    public VowsDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, ChestVows.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.WITCH_RESISTANT_TO)
                .add(VowsDamageTypes.thePlayerMagic)
        ;
    }
}
