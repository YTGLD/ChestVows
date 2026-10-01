package com.ytgld.chest_vows.items.vows.hyperplasia;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.chest_item.items.AttReg;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.CVItems;
import com.ytgld.chest_vows.items.HyperplasiaVow;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageTypes;
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
 * 创伤的复仇
 * <p>
 * 加20%最大生命
 * <p>
 * 最多受到%d点生命值的伤害，在剩余的%d秒内扣除
 * <p>
 *  加5%攻击力
 * <p>
 * 减少30%生命恢复
 */

public class Trauma extends HyperplasiaVow {
    public Trauma(Properties properties) {
        super(properties);
    }


    @ChestVowsConfigPlugin
    public static class ConfigItem implements ChestVowsRegisterItemConfig {

        public static ModConfigSpec.DoubleValue number1;
        public static ModConfigSpec.DoubleValue number2;

        @Override
        public String theCategory() {
            return hyperplasiaVow;
        }

        @Override
        public void config(ModConfigSpec.Builder builder) {
            builder.push("Trauma");
            number1 = builder.translation("chest_vows.config.Trauma")
                    .defineInRange("number", 10, 0.0F, Integer.MAX_VALUE);

            number2 = builder.translation("chest_vows.config.Trauma2")
                    .defineInRange("number2", 5, 0.0F, Integer.MAX_VALUE);
            builder.pop();
        }

        @Override
        public List<CIString> theLanguageProvider() {
            return List.of(
                    new CIString(
                            "Trauma", "创伤的复仇", "最多受到的伤害"),
                    new CIString(
                            "Trauma2", "创伤的复仇2", "多少秒内扣除")

            );
        }
    }
    private static final String lastDamage = "traumaLastDamage";
    private static final String damageTime = "traumaDamageTime";

    public static void event(LivingDamageEvent.Pre event){
        if (event.getEntity() instanceof Player player) {
            if (VowHandler.has(player, CVItems.Trauma_.get())) {
                if (!event.getSource().is(DamageTypes.GENERIC_KILL)) {
                    CompoundTag compoundTag = player.getPersistentData();
                    float shield = ConfigItem.number1.get().floatValue();

                    int time = ConfigItem.number2.get().intValue();
                    if (time < 1) {
                        time = 1;
                    }
                    if (event.getNewDamage() > shield) {
                        float damage = event.getNewDamage() - shield;

                        compoundTag.putFloat(lastDamage,damage);
                        compoundTag.putInt(damageTime,time);

                        event.setNewDamage(shield);
                    }
                }
            }
        }
    }

    @Override
    public void tickVows(Player entity) {
        super.tickVows(entity);
        CompoundTag compoundTag = entity.getPersistentData();

        int time = ConfigItem.number2.get().intValue();
        if (time < 1) {
            time = 1;
        }
        if (entity.isDeadOrDying()) {
            compoundTag.putFloat(lastDamage, 0);
            compoundTag.putInt(damageTime, 0);
        }
        if (entity.tickCount % 20 == 1) {
            if (compoundTag.getFloatOr(lastDamage,0) > 0 && compoundTag.getIntOr(damageTime,0) > 0) {
                entity.setInvulnerableTime(0);
                entity.hurt(entity.damageSources().genericKill(),compoundTag.getFloatOr(lastDamage,0) / time);
                compoundTag.putInt(damageTime,compoundTag.getIntOr(damageTime,0) - 1);
            }else {
                compoundTag.putInt(damageTime,0);
                compoundTag.putFloat(lastDamage,0);

            }
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();

        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(id(),
                0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(id(),
                0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

        modifiers.put(AttReg.heal, new AttributeModifier(id(),
                -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));


        return modifiers;
    }
    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);

        addText(tooltipComponents,Component.translatable("item.chest_vows.trauma.tool.string.1"),
                true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.trauma.tool.string.2",
                        ConfigItem.number1.get().intValue(),ConfigItem.number2.get().intValue()),
                true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.trauma.tool.string.3"),
                true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.trauma.tool.string.4"),
                false);
    }

    @Override
    public String itemName() {
        return VowHandler.mixinName("trauma");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,255,150,80),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/trauma.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.trauma");
    }
}
