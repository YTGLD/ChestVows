package com.ytgld.chest_vows.items.vows.blood;

import com.ytgld.chest_item.items.AttReg;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.BloodVow;
import com.ytgld.chest_vows.items.CVItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.SweepAttackEvent;

import java.util.List;

/**
 * 魔族持剑者
 * <p>
 * 攻击有%d%%的可能触发多段连斩
 * <p>
 * 攻击时有%d%%的概率进行横扫
 * <p>
 * 非剑类武器造成的伤害降低%d%%
 */
public class SwordHolder extends BloodVow {
    public SwordHolder(Properties properties) {
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
            builder.push("SwordHolder");
            number1 = builder.translation("chest_vows.config.SwordHolder")
                    .defineInRange("number", 20, 0.0F, 100);

            number2 = builder.translation("chest_vows.config.SwordHolder2")
                    .defineInRange("number2", 50, 0.0F, 100);

            number3 = builder.translation("chest_vows.config.SwordHolder3")
                    .defineInRange("number3", -0.5f, -0.99, Integer.MAX_VALUE);
            builder.pop();
        }

        @Override
        public List<CIString> theLanguageProvider() {
            return List.of(
                    new CIString(
                            "SwordHolder", "魔族持剑者", "触发多段连斩的概率"),
                    new CIString(
                            "SwordHolder2", "魔族持剑者2", "触发横扫的概率"),
                    new CIString(
                            "SwordHolder3", "魔族持剑者3", "剑类武器造成的伤害惩罚")

            );
        }
    }
    public static void event(SweepAttackEvent event){
        if (event.getEntity() instanceof Player player) {
            if (VowHandler.has(player, CVItems.SwordHolder_.get())) {
                int lv = (int)(double)ConfigItem.number2.get();
                if (player.getRandom().nextInt(100) <= lv) {
                    event.setSweeping(true);
                }
            }
        }
    }

    public static void event(LivingDamageEvent.Pre event){
        if (event.getSource().getEntity() instanceof Player player) {
            if (VowHandler.has(player, CVItems.SwordHolder_.get())) {
                //斩击
                int lv = (int)(double)ConfigItem.number1.get();
                float damageDown = (float) ConfigItem.number3.getAsDouble();
                if ((player.getMainHandItem().get(DataComponents.TOOL) != null)) {
                    event.setNewDamage(event.getNewDamage() * (1 + damageDown));
                }

                if (player.getRandom().nextInt(100) <= lv) {
                    if (event.getEntity() instanceof LivingEntity entity) {
                        if (!player.getCooldowns().isOnCooldown(CVItems.SwordHolder_.get().getDefaultInstance())) {
                            entity.setData(AttReg.swordIntent, entity.getData(AttReg.swordIntent) + 2);
                            entity.setData(AttReg.slashing, entity.getData(AttReg.slashing) + 2);

                            player.getCooldowns().addCooldown(CVItems.SwordHolder_.get().getDefaultInstance(),60);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);

        addText(tooltipComponents,Component.translatable("item.chest_vows.sword_holder.tool.string.1",
                ConfigItem.number1.get()),true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.sword_holder.tool.string.2",
                ConfigItem.number2.get()),true);

        addText(tooltipComponents,Component.translatable("item.chest_vows.sword_holder.tool.string.3",
                ConfigItem.number3.get() * 100),false);
    }

    @Override
    public String itemName() {
        return VowHandler.mixinName("sword_holder");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,255,100,170),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/sword_holder.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.sword_holder");
    }
}
