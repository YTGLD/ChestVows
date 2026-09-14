package com.ytgld.chest_vows;

import com.ytgld.chest_vows.entity.Entitys;
import com.ytgld.chest_vows.entity.render.TheSpiritRender;
import com.ytgld.chest_vows.other.VowsDamageTypeTagsProvider;
import com.ytgld.chest_vows.render.RenderVowItem;
import com.ytgld.chest_vows.render.particle.has_opt.CubeParticle;
import com.ytgld.chest_vows.render.particle.has_opt.MagicChestParticle;
import com.ytgld.chest_vows.render.particle.other.MagicParticles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = ChestVows.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ChestVows.MODID, value = Dist.CLIENT)
public class ChestVowsClient {
    public ChestVowsClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void tick(ClientTickEvent.Pre event) {
        RenderVowItem.clientTick(event);
    }

    @SubscribeEvent
    public static void RegisterRenderPipelinesEvent(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Entitys.TheSpirit_.get(), TheSpiritRender::new);
    }
    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MagicParticles.colorOption.get(), MagicChestParticle.Provider::new);
        event.registerSpriteSet(MagicParticles.colorCube.get(), CubeParticle.Provider::new);
    }
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        event.createProvider(VowsDamageTypeTagsProvider::new);
    }
}
