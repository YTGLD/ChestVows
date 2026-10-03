package com.ytgld.chest_vows.items.vows.hyperplasia;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.chest_item.items.memory.IntAndStringSyncHandler;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.config.ChestVowsConfigPlugin;
import com.ytgld.chest_vows.config.ChestVowsRegisterItemConfig;
import com.ytgld.chest_vows.items.HyperplasiaVow;
import com.ytgld.chest_vows.other.CVAttReg;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

import java.util.HashMap;
import java.util.List;

public class InnerDemon extends HyperplasiaVow {
    public InnerDemon(Properties properties) {
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
            builder.push("InnerDemon");
            number1 = builder.translation("chest_vows.config.InnerDemon")
                    .defineInRange("number", 2, 0.0F, Integer.MAX_VALUE);

            number2 = builder.translation("chest_vows.config.InnerDemon2")
                    .defineInRange("number2", 1, 0.0F, Integer.MAX_VALUE);
            builder.pop();
        }

        @Override
        public List<CIString> theLanguageProvider() {
            return List.of(
                    new CIString(
                            "InnerDemon", "心魔", "杀死生物生命值增加的基值，但是会随增加而衰减"),
                    new CIString(
                            "InnerDemon2", "心魔2", "生命值每次最多恢复的值")

            );
        }
    }
    public static final String kill = "InnerDemonKillNumber";
    public static int getKillNumber(Player player){
        IntAndStringSyncHandler.ISClass isClass = player.getData(CVAttReg.playerTag.get());
        HashMap<String, Integer> map = isClass.map();
        if (map == null){
            return 0;
        }
        if (!map.isEmpty()) {
            return map.get(kill);
        }
        return 0;
    }
    public static void addKillNumber(Player player,int value){
        IntAndStringSyncHandler.ISClass isClass = player.getData(CVAttReg.playerTag.get());
        HashMap<String, Integer> map = isClass.map();
        if (map == null) {
            player.setData(CVAttReg.playerTag.get(),new IntAndStringSyncHandler.ISClass(new HashMap<>()));
        }

        if (map != null){
            map.putIfAbsent(kill, 0);
            map.put(kill, map.get(kill) + value);
            player.setData(CVAttReg.playerTag.get(), isClass);
        }
    }

    public static void event(LivingDeathEvent event){
        Entity entity = event.getSource().getEntity();
        if (entity instanceof Player player && !player.level().isClientSide()) {
            if (VowHandler.has(player, VowHandler.mixinName("inner_demon"))) {
                int add = ConfigItem.number1.get().intValue();
                addKillNumber(player,add);
            }
        }
    }
    public static void event(LivingHealEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (VowHandler.has(player, VowHandler.mixinName("inner_demon"))){
                float value = event.getAmount();
                float max = ConfigItem.number2.get().floatValue();
                if (value > max) {
                    value = max;
                }
                event.setAmount(value);
            }
        }
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();
        float add = 0;
        if (livingEntity instanceof Player player) {
            int baseValue = getKillNumber(player);
            add = (float) Math.sqrt(baseValue);
            if (add > 100) {
                add = 100;
            }
        }
        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(id(),
                add, AttributeModifier.Operation.ADD_VALUE));
        return modifiers;
    }

    @Override
    public String itemName() {
        return VowHandler.mixinName("inner_demon");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,255,150,80),
                        ChestVows.fromNamespaceAndPath(ChestVows.MODID,"textures/vows/inner_demon.png"))
        );
    }
    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        float addHealth = ConfigItem.number1.get().floatValue();
        float healLock = ConfigItem.number2.get().floatValue();

        addText(tooltipComponents,Component.translatable("item.chest_vows.inner_demon.1" ),true);
        addText(tooltipComponents,Component.translatable("item.chest_vows.inner_demon.2",healLock ),false);
    }
    @Override
    public Component textMain() {
        return Component.translatable("chest_vows.vow.inner_demon");
    }
}
