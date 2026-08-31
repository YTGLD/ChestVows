package com.ytgld.chest_vows;

import com.mojang.logging.LogUtils;
import com.ytgld.chest_vows.config.ChestVowsLanguageProvider;
import com.ytgld.chest_vows.entity.Entitys;
import com.ytgld.chest_vows.event.CVEvent;
import com.ytgld.chest_vows.items.CVItems;
import com.ytgld.chest_vows.other.CVAttReg;
import com.ytgld.chest_vows.render.particle.other.MagicParticles;
import com.ytgld.chest_vows.sounds.CVSounds;
import com.ytgld.chest_vows.sounds.ChestVowsSoundProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

@Mod(ChestVows.MODID)
public class ChestVows {
    public static final String MODID = "chest_vows";
    public static final Logger LOGGER = LogUtils.getLogger();
    public ChestVows(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onGatherData);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.fc);

        Entitys.REGISTRY.register(modEventBus);
        MagicParticles.PARTICLE_TYPES.register(modEventBus);
        CVAttReg.ATTACHMENT_TYPES.register(modEventBus);
        CVItems.ITEMS.register(modEventBus);
        CVItems.Tab.CREATIVE_MODE_TABS.register(modEventBus);
        CVSounds.REGISTRY.register(modEventBus);

        NeoForge.EVENT_BUS.register(new CVEvent());
    }

    public static ResourceLocation fromNamespaceAndPath(String modid ,String  path){
        return ResourceLocation.fromNamespaceAndPath(modid,path);
    }
    public void onGatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();


        gen.addProvider(event.includeClient(),new ChestVowsLanguageProvider(packOutput));
        gen.addProvider(
                event.includeClient(),
                new ChestVowsSoundProvider(packOutput, existingFileHelper)
        );
    }
}
