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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * 血祭
 * <p>
 * 疤痕组织最大值增加50%且吸收全部伤害
 * <p>
 * 疤痕组织在被根除时产生的冷却增加100%
 *
 */
public class BloodSacrifice extends BloodVow {
    public BloodSacrifice(Properties properties) {
        super(properties);
    }

    @ChestVowsConfigPlugin
    public static class ConfigItem implements ChestVowsRegisterItemConfig {

        public static ModConfigSpec.DoubleValue number1;
        public static ModConfigSpec.DoubleValue number2;

        @Override
        public String theCategory() {
            return bloodVow;
        }

        @Override
        public void config(ModConfigSpec.Builder builder) {
            builder.push("BloodSacrifice");
            number1 = builder.translation("chest_vows.config.BloodSacrifice")
                    .defineInRange("number", 0.5, 0.0F, Integer.MAX_VALUE);

            number2 = builder.translation("chest_vows.config.BloodSacrifice2")
                    .defineInRange("number2", 1, 0.0F, Integer.MAX_VALUE);
            builder.pop();
        }

        @Override
        public List<ChestVowsRegisterItemConfig.CIString> theLanguageProvider() {
            return List.of(
                    new ChestVowsRegisterItemConfig.CIString(
                            "BloodSacrifice", "血祭", "疤痕组织最大值奖励"),
                    new ChestVowsRegisterItemConfig.CIString(
                            "BloodSacrifice2", "血祭2", "疤痕组织在被根除时产生的冷却惩罚")

            );
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();


        modifiers.put(AttReg.hyperplasia, new AttributeModifier(id(),
                ConfigItem.number1.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(AttReg.hyperplasiaCooldown, new AttributeModifier(id(),
                ConfigItem.number2.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        return modifiers;
    }
    public static void mixinHyperplasia(LivingEntity living, CallbackInfoReturnable<Boolean> cir){
        if (VowHandler.has(living, CVItems.BloodSacrifice_.get())) {
            cir.setReturnValue(true);
        }
    }
    @Override
    public String itemName() {
        return VowHandler.mixinName("blood_sacrifice");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,255,100,170),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/blood_sacrifice.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.blood_sacrifice");
    }

    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);

        addText(tooltipComponents,Component.translatable("item.chest_vows.blood_sacrifice.tool.string.1",
                ConfigItem.number1.get() * 100),true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.blood_sacrifice.tool.string.2",
                ConfigItem.number2.get()* 100),false);
    }
}
