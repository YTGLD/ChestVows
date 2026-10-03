package com.ytgld.chest_vows.items.vows.wind;

import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.CVItems;
import com.ytgld.chest_vows.items.WindVow;
import com.ytgld.chest_vows.items.vows.hyperplasia.CtrlMagic;
import com.ytgld.chest_vows.other.IAttributeInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

import java.util.List;

public class WeightyWind extends WindVow {
    public WeightyWind(Properties properties) {
        super(properties);
    }
    @ChestVowsConfigPlugin
    public static class ConfigItem implements ChestVowsRegisterItemConfig {
        public static ModConfigSpec.DoubleValue number1;
        @Override
        public String theCategory() {
            return windVow;
        }

        @Override
        public void config(ModConfigSpec.Builder builder) {
            builder.push("WeightyWind");
            number1 = builder.translation("chest_vows.config.WeightyWind")
                    .defineInRange("number", 1.5, 0.0F, Integer.MAX_VALUE);
            builder.pop();
        }

        @Override
        public List<CIString> theLanguageProvider() {
            return List.of(
                    new CIString(
                            "WeightyWind", "沉重之风", "速度上限")
            );
        }
    }
    public static void event(LivingKnockBackEvent event){
        if (event.getEntity() instanceof Player player) {
            if (VowHandler.has(player, VowHandler.mixinName("weighty_wind"))){
                if (player.isSprinting()) {
                    event.setStrength(0);
                    event.setCanceled(true);
                }
            }
        }
    }
    @Override
    public String itemName() {
        return VowHandler.mixinName("weighty_wind");
    }

    @Override
    public void tickVows(Player entity) {
        super.tickVows(entity);
        double base = entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
        float configSpeed  = ConfigItem.number1.get().floatValue();
        double end = base * configSpeed;
        AttributeInstance attributeInstance =  entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
        if (attributeInstance instanceof IAttributeInstance iAttributeInstance) {
            iAttributeInstance.chestVows26_2$setValue(end);
        }
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(backColor(),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/weighty_wind.png"))
        );
    }
    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        float max = ConfigItem.number1.get().floatValue();
        addText(tooltipComponents,Component.translatable("item.chest_vows.weighty_wind.1"),true);
        addText(tooltipComponents,Component.translatable("item.chest_vows.weighty_wind.2",max * 100),false);
    }
    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.weighty_wind");
    }
}
