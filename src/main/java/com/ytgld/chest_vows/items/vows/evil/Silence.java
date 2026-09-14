package com.ytgld.chest_vows.items.vows.evil;

import com.google.common.collect.Multimap;
import com.ytgld.chest_item.items.evil_mother.IEvil;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.items.CVItems;
import com.ytgld.chest_vows.items.EvilVow;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * 静默
 * <p>
 * 抹去理智值的负面影响
 * <p>
 * 同时抹去理智的正面加成
 */
public class Silence extends EvilVow {
    public Silence(Properties properties) {
        super(properties);
    }


    public static void mixinTheAttrib(EntityTickEvent.Post event,
                                      Multimap<Holder<Attribute>, AttributeModifier> theAttrib,
                                      CallbackInfo ci){
        if (event.getEntity() instanceof Player player) {
            if (VowHandler.has(player, CVItems.Silence_.get())) {
                ci.cancel();
                player.getAttributes().removeAttributeModifiers(theAttrib);
            }
        }
    }


    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);

        addText(tooltipComponents,Component.translatable("item.chest_vows.silence.tool.string.1"),
                true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.silence.tool.string.2"),
                false);
    }
    @Override
    public String itemName() {
        return VowHandler.mixinName("silence");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(IEvil.color,
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/silence.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.silence");
    }
}
