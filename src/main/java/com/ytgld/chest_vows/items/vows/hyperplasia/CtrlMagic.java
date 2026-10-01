package com.ytgld.chest_vows.items.vows.hyperplasia;

import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.HyperplasiaVow;
import com.ytgld.chest_vows.other.VowsDamageTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.List;


public class CtrlMagic extends HyperplasiaVow {
    public CtrlMagic(Properties properties) {
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
            builder.push("CtrlMagic");
            number1 = builder.translation("chest_vows.config.CtrlMagic")
                    .defineInRange("number", 0.25, 0.0F, Integer.MAX_VALUE);

            number2 = builder.translation("chest_vows.config.CtrlMagic2")
                    .defineInRange("number2", 0.5, 0.0F, Integer.MAX_VALUE);
            builder.pop();
        }

        @Override
        public List<CIString> theLanguageProvider() {
            return List.of(
                    new CIString(
                            "CtrlMagic", "魔法掌控", "魔法伤害加成"),
                    new CIString(
                            "CtrlMagic2", "魔法掌控2", "魔法转换")

            );
        }
    }
    public static void event(LivingDamageEvent.Pre event){
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            if (VowHandler.has(player, VowHandler.mixinName("ctrl_magic"))) {
                if (event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)
                        || event.getSource().is(VowsDamageTypes.thePlayerMagic)
                        || event.getSource().is(Tags.DamageTypes.IS_MAGIC)
                ) {
                    event.setNewDamage(event.getNewDamage() * 1.6f);
                }
            }
        }
        if (event.getSource().getEntity() instanceof Player player) {
            if (VowHandler.has(player, VowHandler.mixinName("ctrl_magic"))) {
                if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)
                        || event.getSource().is(VowsDamageTypes.thePlayerMagic)
                        || event.getSource().is(Tags.DamageTypes.IS_MAGIC) &&
                        !(event.getSource().getEntity() instanceof Player player1 && !player1.is(player))) {
                    float to = ConfigItem.number1.get().floatValue();
                    event.setNewDamage(event.getNewDamage() * (1 + to));
                }
            }
        }
    }
    public static void event(AttackEntityEvent event){
        Player player = event.getEntity();
        float magic = ConfigItem.number2.get().floatValue();

        if (event.getTarget() instanceof LivingEntity livingEntity) {
            if (VowHandler.has(player, VowHandler.mixinName("ctrl_magic"))) {
                livingEntity.hurt(VowsDamageTypes.playerMagic(player),
                        (float) (player.getAttributeValue(Attributes.ATTACK_DAMAGE) * magic));
            }
        }
    }
    @Override
    public String itemName() {
        return VowHandler.mixinName("ctrl_magic");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,255,150,80),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/ctrl_magic.png"))
        );
    }
    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        float magic = ConfigItem.number1.get().floatValue();
        float to = ConfigItem.number2.get().floatValue();

        addText(tooltipComponents,Component.translatable("item.chest_vows.ctrl_magic.1",magic * 100),true);
        addText(tooltipComponents,Component.translatable("item.chest_vows.ctrl_magic.2",to * 100),true);
        addText(tooltipComponents,Component.translatable("item.chest_vows.ctrl_magic.3"),false);
    }
    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.ctrl_magic");
    }
}
