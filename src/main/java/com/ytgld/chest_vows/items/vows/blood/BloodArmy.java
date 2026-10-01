package com.ytgld.chest_vows.items.vows.blood;


import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.BloodVow;
import com.ytgld.chest_vows.items.CVItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

import java.util.List;


/**
 * 破望血军
 * <p>
 * 提高%d%%生命值恢复速度
 * <p>
 * 生命值每隔%d秒进行自然恢复
 * <p>
 * 受到伤害后的%d秒内将无法受到治疗
 */
public class BloodArmy extends BloodVow {
    public BloodArmy(Properties properties) {
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
            builder.push("BloodArmy");
            number1 = builder.translation("chest_vows.config.BloodArmy")
                    .defineInRange("number", 0.8f, -1, 100);

            number2 = builder.translation("chest_vows.config.BloodArmy2")
                    .defineInRange("number2", 2, 0.0F, Integer.MAX_VALUE);

            number3 = builder.translation("chest_vows.config.BloodArmy3")
                    .defineInRange("number3", 5, 0, 60f);
            builder.pop();
        }

        @Override
        public List<CIString> theLanguageProvider() {
            return List.of(
                    new CIString(
                            "BloodArmy", "破望血军", "生命值恢复加成"),
                    new CIString(
                            "BloodArmy2", "破望血军2", "每隔多少秒恢复2点生命值"),
                    new CIString(
                            "BloodArmy3", "破望血军3", "受伤后的治疗暂停时间")

            );
        }
    }
    public static void event(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            if (VowHandler.has(player, CVItems.BloodArmy_.get())) {
                int cooldown = ConfigItem.number3.get().intValue() * 20;
                player.getCooldowns().addCooldown(CVItems.BloodArmy_.get().getDefaultInstance(),cooldown);
            }
        }
    }
    public static void event(LivingHealEvent event){
        if (event.getEntity() instanceof Player player) {
            if (VowHandler.has(player, CVItems.BloodArmy_.get())) {
                if (canHeal(player)) {
                    float heal = ConfigItem.number1.get().floatValue();
                    event.setAmount(event.getAmount() * (1 + heal));
                }else {
                    event.setAmount(0);
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public void tickVows(Player entity) {
        super.tickVows(entity);
        int time =ConfigItem.number2.get().intValue() * 20;
        if (time < 1) {
            time = 1;
        }
        if (entity.tickCount % time == 1) {
            if (canHeal(entity)) {
                entity.heal(2);
            }
        }
    }

    @Override
    public String itemName() {
        return VowHandler.mixinName("blood_army");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,255,100,170),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/blood_army.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.blood_army");
    }

    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);

        addText(tooltipComponents,Component.translatable("item.chest_vows.blood_army.tool.string.1",
                ConfigItem.number1.get().floatValue() * 100),true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.blood_army.tool.string.2",
                ConfigItem.number2.get().intValue()),true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.blood_army.tool.string.3",
                ConfigItem.number3.get().intValue()),false);
    }

    private static boolean canHeal(Player player){
        return !player.getCooldowns().isOnCooldown(CVItems.BloodArmy_.get().getDefaultInstance());
    }
}
