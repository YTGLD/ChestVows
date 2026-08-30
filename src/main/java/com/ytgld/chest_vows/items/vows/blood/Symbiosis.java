package com.ytgld.chest_vows.items.vows.blood;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.chest_item.Handler;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * 四相共生
 * <p>
 * 允许在创伤之痕恢复时继续恢复其余护盾
 * <p>
 * 创伤之痕随时间恢复
 * <p>
 * 创伤之痕不再可以吞噬护盾
 * <p>
 * 除创伤之痕外的护盾恢复速度降低50%
 */

public class Symbiosis extends BloodVow {
    public Symbiosis(Properties properties) {
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
            builder.push("Symbiosis");
            number1 = builder.translation("chest_vows.config.Symbiosis")
                    .defineInRange("number", 0.5, 0.0F, 100);

            number2 = builder.translation("chest_vows.config.Symbiosis2")
                    .defineInRange("number2", 5, 0f, 60);

            builder.pop();
        }

        @Override
        public List<ChestVowsRegisterItemConfig.CIString> theLanguageProvider() {
            return List.of(
                    new ChestVowsRegisterItemConfig.CIString(
                            "Symbiosis", "四相共生", "除创伤之痕外的护盾恢复速度降低的值"),
                    new ChestVowsRegisterItemConfig.CIString(
                            "Symbiosis2", "四相共生2", "创伤之痕每隔多少秒恢复 2 点")

            );
        }
    }
    public static void mixinTickShield(LivingEntity living, CallbackInfo ci){
        if (VowHandler.has(living, CVItems.Symbiosis_.get())) {
            ci.cancel();
        }
    }
    public static void mixinCanHeal(LivingEntity living, CallbackInfoReturnable<Boolean> cir){
        if (VowHandler.has(living, CVItems.Symbiosis_.get())) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void tickVows(Player entity) {
        super.tickVows(entity);
        int time = ConfigItem.number2.get().intValue() * 20;
        if (time < 1) {
            time = 1;
        }
        if (entity.tickCount % time == 1) {
            Handler.addHeartShield(entity, 2);
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();

        float down = ConfigItem.number1.get().intValue();

        //越大越慢
        modifiers.put(AttReg.hyperplasia_speed, new AttributeModifier(id(),
                down, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        //越大越慢
        modifiers.put(AttReg.chaos_armor_speed, new AttributeModifier(id(),
                down, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        //越小越慢
        modifiers.put(AttReg.shadow_shield_speed, new AttributeModifier(id(),
                -down, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));


        return modifiers;
    }

    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);

        addText(tooltipComponents,Component.translatable("item.chest_vows.symbiosis.tool.string.1"),
                true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.symbiosis.tool.string.2"),
                true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.symbiosis.tool.string.3"),
                false);

        addText(tooltipComponents,Component.translatable("item.chest_vows.symbiosis.tool.string.4",
                ConfigItem.number1.get().floatValue() * 100),false);
    }

    @Override
    public String itemName() {
        return VowHandler.mixinName("symbiosis");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,255,100,170),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/symbiosis.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.symbiosis");
    }

}
