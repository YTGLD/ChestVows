package com.ytgld.chest_vows.items.vows.blood;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.chest_item.items.AttReg;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.BloodVow;
import com.ytgld.chest_vows.items.CVItems;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

import java.util.List;

/**
 * 混沌祭司长
 * <p>
 * 生命值和抗性增加%d%%
* <p>
 * 有%d%%的可能将受到的治疗无效化
 * <p>
 *  受到伤害时有%d%%的概率使伤害翻倍
 */
public class ChiefPriest extends BloodVow {

    public ChiefPriest(Properties properties) {
        super(properties);
    }
    @ChestVowsConfigPlugin
    public static class ConfigItem implements ChestVowsRegisterItemConfig {

        public static ModConfigSpec.DoubleValue number1;
        public static ModConfigSpec.DoubleValue number2;
        public static ModConfigSpec.DoubleValue number3;

        @Override
        public String theCategory() {
            return bloodVow;
        }

        @Override
        public void config(ModConfigSpec.Builder builder) {
            builder.push("ChiefPriest");
            number1 = builder.translation("chest_vows.config.ChiefPriest")
                    .defineInRange("number", 0.3, 0f, 60);

            number2 = builder.translation("chest_vows.config.ChiefPriest2")
                    .defineInRange("number2", 25, 0f, 100);

            number3 = builder.translation("chest_vows.config.ChiefPriest3")
                    .defineInRange("number3", 20, 0, 100f);
            builder.pop();
        }

        @Override
        public List<CIString> theLanguageProvider() {
            return List.of(
                    new CIString(
                            "ChiefPriest", "混沌祭司长", "抗性和生命值的奖励"),
                    new CIString(
                            "ChiefPriest2", "混沌祭司长2", "治疗失败的概率"),
                    new CIString(
                            "ChiefPriest3", "混沌祭司长3", "受到伤害翻倍的概率")

            );
        }
    }
    public static void event(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            if (VowHandler.has(player, CVItems.ChiefPriest_.get())) {
                int l = ConfigItem.number3.get().intValue();
                if (player.getRandom().nextInt(100) <= l){
                    event.setNewDamage(event.getNewDamage() * 2);
                }
            }
        }
    }

    public static void event(LivingHealEvent event){
        if (event.getEntity() instanceof Player player) {
            if (VowHandler.has(player, CVItems.ChiefPriest_.get())) {
                int l = ConfigItem.number2.get().intValue();
                if (player.getRandom().nextInt(100) <= l) {
                    event.setAmount(0);
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();

        float add = ConfigItem.number1.get().floatValue();

        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(id(),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(AttReg.resistance, new AttributeModifier(id(),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));


        return modifiers;
    }

    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        addText(tooltipComponents,Component.translatable("item.chest_vows.chief_priest.tool.string.1",
                (int)(ConfigItem.number1.get().floatValue() * 100)),
                true);
        addText(tooltipComponents,Component.translatable("item.chest_vows.chief_priest.tool.string.2",
                        ConfigItem.number2.get().intValue()),
                false);
        addText(tooltipComponents,Component.translatable("item.chest_vows.chief_priest.tool.string.3",
                        ConfigItem.number3.get().intValue()),
                false);
    }

    @Override
    public String itemName() {
        return VowHandler.mixinName("chief_priest");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,255,100,170),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/chief_priest.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.chief_priest");
    }
}
