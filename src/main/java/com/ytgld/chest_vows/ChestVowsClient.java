package com.ytgld.chest_vows;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.ytgld.chest_vows.render.RenderVowItem;
import com.ytgld.chest_vows.render.VowsRenders;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.io.IOException;

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
    public static void EntityRenderersEvent(RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(),
                    ResourceLocation.fromNamespaceAndPath(ChestVows.MODID,"vows"),
                    DefaultVertexFormat.POSITION_TEX_COLOR), VowsRenders::setVows);
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

}
