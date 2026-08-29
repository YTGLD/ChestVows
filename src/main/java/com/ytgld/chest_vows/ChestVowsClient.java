package com.ytgld.chest_vows;

import com.ytgld.chest_vows.render.RenderVowItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

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
}
