package com.ytgld.chest_vows.items.vows.hyperplasia;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.chest_item.items.AttReg;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.HyperplasiaVow;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class PainSoul extends HyperplasiaVow {
    public PainSoul(Properties properties) {
        super(properties);
    }

    @ChestVowsConfigPlugin
    public static class ConfigItem implements ChestVowsRegisterItemConfig {

        public static ModConfigSpec.DoubleValue number1;

        @Override
        public String theCategory() {
            return hyperplasiaVow;
        }

        @Override
        public void config(ModConfigSpec.Builder builder) {
            builder.push("PainSoul");
            number1 = builder.translation("chest_vows.config.PainSoul")
                    .defineInRange("number", 0.33, 0.0F, 1);
            builder.pop();
        }

        @Override
        public List<CIString> theLanguageProvider() {
            return List.of(
                    new CIString(
                            "PainSoul", "苦痛誓约", "生命值惩罚")
            );
        }
    }
    @Override
    public void tickVows(Player entity) {
        super.tickVows(entity);
        if (entity instanceof Player player) {
            AttributeInstance instance = player.getAttributes().getInstance(Attributes.MAX_HEALTH);
            if (instance != null) {
                for (AttributeModifier modifier : instance.getModifiers()){
                    player.getAttributes().addTransientAttributeModifiers(modifierMultimap(modifier));
                }
            }
        }
    }

    private Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap (AttributeModifier modifier){
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = HashMultimap.create();
        for (Holder<Attribute> attributeHolder : list()){
            modifierMultimap.put(attributeHolder, new AttributeModifier(
                    ChestVows.fromNamespaceAndPath(ChestVows.MODID,
                            modifier.id().getNamespace() + modifier.id().getPath() + "pain_soul"),
                    modifier.amount(),modifier.operation()));
        }

        return modifierMultimap;
    }

    private static List<Holder<Attribute>> list(){
        return List.of(AttReg.chaos_armor,AttReg.hyperplasia,AttReg.painShield_number,AttReg.shadow_shield);
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = super.doAttribute(livingEntity,item);
        float health = ConfigItem.number1.get().floatValue();

        modifierMultimap.put(Attributes.MAX_HEALTH,new AttributeModifier(
                ChestVows.fromNamespaceAndPath(ChestVows.MODID,"pain_soul"),
                -health, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return modifierMultimap;
    }
    @Override
    public String itemName() {
        return VowHandler.mixinName("pain_soul");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,255,150,80),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/pain_soul.png"))
        );
    }

    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        float health = ConfigItem.number1.get().floatValue();

        addText(tooltipComponents,Component.translatable("item.chest_vows.pain_soul.1"),true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.pain_soul.3",health * 100),false);
        addText(tooltipComponents,Component.translatable("item.chest_vows.pain_soul.4"),false);
    }
    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.pain_soul");
    }


}
