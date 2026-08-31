package com.ytgld.chest_vows.items.vows.evil;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.chest_item.items.AttReg;
import com.ytgld.chest_item.items.evil_mother.IEvil;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.CVItems;
import com.ytgld.chest_vows.items.EvilVow;
import com.ytgld.chest_vows.items.vows.blood.BloodSacrifice;
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
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

import java.util.List;
import java.util.function.Supplier;

/**
 * 互持
 * <p>
 * 治疗时同时治愈护盾
 * <p>
 * 所有护盾的最大值增加%d%%
 * <p>
 * 自身生命值和护甲值均减少%d%%
 */
public class MutualSupport extends EvilVow {
    public MutualSupport(Properties properties) {
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
            builder.push("MutualSupport");
            number1 = builder.translation("chest_vows.config.MutualSupport")
                    .defineInRange("number", 0.7, 0.0F, Integer.MAX_VALUE);

            number2 = builder.translation("chest_vows.config.MutualSupport2")
                    .defineInRange("number2", 0.5, 0.0F, Integer.MAX_VALUE);
            builder.pop();
        }

        @Override
        public List<ChestVowsRegisterItemConfig.CIString> theLanguageProvider() {
            return List.of(
                    new ChestVowsRegisterItemConfig.CIString(
                            "MutualSupport", "互持", "护盾奖励"),
                    new ChestVowsRegisterItemConfig.CIString(
                            "MutualSupport2", "互持2", "生命值和护甲值的惩罚")

            );
        }
    }

    public static void event(LivingHealEvent event){
        if (event.getEntity() instanceof Player player) {
            if (VowHandler.has(player, CVItems.MutualSupport_.get())) {
                float a  = event.getAmount();
                heal(player,AttReg.hyperplasia,AttReg.hyperplasiaATTACHMENT_TYPES,a);
                heal(player,AttReg.shadow_shield,AttReg.black_shadowAttachmentType,a);
                heal(player,AttReg.chaos_armor,AttReg.chaosWinds,a);
                heal(player,AttReg.painShield_number,AttReg.painShield,a);
            }
        }
    }

    private static void heal(Player player,Holder<Attribute> attribute , Supplier<AttachmentType<Float>> shield,float value){
        player.setData(shield,(float)Math.min(player.getAttributeValue(attribute),player.getData(shield) + value));
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();

        float add = ConfigItem.number1.get().floatValue();

        modifiers.put(AttReg.hyperplasia, new AttributeModifier(id(),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(AttReg.shadow_shield, new AttributeModifier(id(),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(AttReg.painShield_number, new AttributeModifier(id(),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(AttReg.chaos_armor, new AttributeModifier(id(),
                add, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        float down = -ConfigItem.number2.get().floatValue();

        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(id(),
                down, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(Attributes.ARMOR, new AttributeModifier(id(),
                down, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));



        return modifiers;
    }

    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        addText(tooltipComponents,Component.translatable("item.chest_vows.mutual_support.tool.string.1"),
                true);
        addText(tooltipComponents,Component.translatable("item.chest_vows.mutual_support.tool.string.2",
                (int)(ConfigItem.number1.get().floatValue() * 100)),true);
        addText(tooltipComponents,Component.translatable("item.chest_vows.mutual_support.tool.string.3",
                (int)(ConfigItem.number2.get().floatValue() * 100)),false);

    }

    @Override
    public String itemName() {
        return VowHandler.mixinName("mutual_support");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(IEvil.color,
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/mutual_support.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.mutual_support");
    }
}
