package com.ytgld.chest_vows.other;

import com.ytgld.chest_vows.ChestVows;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

import static net.neoforged.neoforge.common.Tags.DamageTypes.IS_MAGIC;

public class VowsDamageTypeTagsProvider extends DamageTypeTagsProvider {


    public VowsDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider,ChestVows.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(IS_MAGIC)
                .addOptional(VowsDamageTypes.thePlayerMagic)
        ;
    }
}
