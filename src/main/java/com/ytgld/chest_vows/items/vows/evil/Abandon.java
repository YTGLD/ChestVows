package com.ytgld.chest_vows.items.vows.evil;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.chest_item.effect.Effects;
import com.ytgld.chest_item.items.AttReg;
import com.ytgld.chest_item.items.evil_mother.IEvil;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.CVItems;
import com.ytgld.chest_vows.items.EvilVow;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.List;

/**
 * 遗弃者的复仇
 * <p>
 * 攻击施加邪母之拒，若无法施加，则造成%d%%的额外伤害
 * <p>
 * 自身生命值和抗性均永久损失%d%%
 */
public class Abandon extends EvilVow {
    public Abandon(Properties properties) {
        super(properties);
    }
    @ChestVowsConfigPlugin
    public static class ConfigItem implements ChestVowsRegisterItemConfig {

        public static ModConfigSpec.DoubleValue number1;
        public static ModConfigSpec.DoubleValue number2;

        @Override
        public String theCategory() {
            return evilVow;
        }

        @Override
        public void config(ModConfigSpec.Builder builder) {
            builder.push("Abandon");
            number1 = builder.translation("chest_vows.config.Abandon")
                    .defineInRange("number", 0.5, 0.0F, Integer.MAX_VALUE);

            number2 = builder.translation("chest_vows.config.Abandon2")
                    .defineInRange("number2", 0.2, 0.0F, Integer.MAX_VALUE);
            builder.pop();
        }

        @Override
        public List<CIString> theLanguageProvider() {
            return List.of(
                    new CIString(
                            "Abandon", "遗弃者的复仇", "额外伤害"),
                    new CIString(
                            "Abandon2", "遗弃者的复仇2", "自身生命值和抗性的惩罚")

            );
        }
    }
    public static void event(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity living) {
            if (VowHandler.has(player, CVItems.Abandon_.get())) {
                if (!living.addEffect(new MobEffectInstance(Effects.EvilErosion, 100, 0))) {
                    float damage = ConfigItem.number1.get().floatValue();
                    event.setNewDamage(event.getNewDamage() * (1 + damage));
                }
            }
        }
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();

        float down = ConfigItem.number2.get().floatValue();

        modifiers.put(AttReg.resistance, new AttributeModifier(id(),
                -down, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(id(),
                -down, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return modifiers;
    }
    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);

        addText(tooltipComponents,Component.translatable("item.chest_vows.abandon.tool.string.1",
                (int)(ConfigItem.number1.get().floatValue() * 100)), true);
        addText(tooltipComponents,Component.translatable("item.chest_vows.abandon.tool.string.2",
                        (int)(ConfigItem.number2.get().floatValue() * 100)), false);
    }
    @Override
    public String itemName() {
        return VowHandler.mixinName("abandon");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(IEvil.color,
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/abandon.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.abandon");
    }
}
