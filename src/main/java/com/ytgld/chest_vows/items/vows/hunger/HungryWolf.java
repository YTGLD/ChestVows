package com.ytgld.chest_vows.items.vows.hunger;

import com.ytgld.vows.Vows;
import com.ytgld.vows.items.BaseVows;
import com.ytgld.vows.tool.*;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.List;
import java.util.Set;

public class HungryWolf extends BaseVows {
    public HungryWolf(Properties properties) {
        super(properties);
    }
    @Override
    public Component textMain() {
        return Component.translatable("vows.vows.hungry_wolf");
    }

    @Override
    public String itemName() {
        return Handler.mixinName("hungry_wolf");
    }
    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,20,255,20),
                        Vows.fromNamespaceAndPath("textures/soul/soul_2.png")),
                new ColorAndImage(Light.ARGB.color(255,0,220,50),
                        Vows.fromNamespaceAndPath("textures/soul/soul_4.png")),
                new ColorAndImage(Light.ARGB.color(255,50,200,0),
                        Vows.fromNamespaceAndPath("textures/soul/soul_6.png")),
                new ColorAndImage(Light.ARGB.color(255,100,180,100),
                        Vows.fromNamespaceAndPath("textures/soul/soul_8.png")),
                new ColorAndImage(Light.ARGB.color(255,10,180,10),
                        Vows.fromNamespaceAndPath("textures/soul/soul_10.png"))
        );
    }
    public static void speed(LivingEntityUseItemEvent.Start event){
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            if (Handler.has(player,Handler.mixinName("hungry_wolf"))) {
                ItemStack stack = event.getItem();
                if (stack.getUseAnimation() == ItemUseAnimation.EAT) {
                    float base = eatSpeed(player);
                    event.setDuration((int) (event.getDuration() *  base));
                }
            }
        }
    }
    public static void endUse(ItemStack stack,LivingEntity livingEntity){
        if (livingEntity instanceof Player player) {
            if (Handler.has(player,Handler.mixinName("hungry_wolf"))) {
                if (stack.getUseAnimation() == ItemUseAnimation.EAT) {
                    FoodProperties properties = stack.get(DataComponents.FOOD);
                    if (properties != null) {
                        float base = -theValue(player);
                        int foodLevelModifier = (int) (properties.nutrition() * base);
                        float saturationLevelModifier = properties.saturation() * base;
                        addEffect(player, MobEffects.REGENERATION,1,200);
                        addEffect(player, MobEffects.STRENGTH,0,300);
                        FoodData data = player.getFoodData();
                        data.eat(foodLevelModifier,saturationLevelModifier);
                    }
                }
            }
        }
    }

    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        addText(tooltipComponents,Component.translatable("vows.vows.hungry_wolf.1"),true);
        addText(tooltipComponents,Component.translatable("vows.vows.hungry_wolf.2"),true);
        addText(tooltipComponents,Component.translatable("vows.vows.hungry_wolf.3"),false);
    }

    public static float eatSpeed(Player player){
        return 1 - Handler.doValue(0.4f,player);
    }
    public static float theValue(Player player){
        return Handler.doValue(0.5f,player);
    }
    public static void addEffect(Player player, Holder<MobEffect> holder, int lvl , int time){
        player.addEffect(new MobEffectInstance(holder,time,lvl,true,true));
    }
    @RecipePlugin
    public static class Recipe implements RegisterRecipeConfig {

        @Override
        public List<ItemStack> itemList() {
            return List.of(
                    new ItemStack(Items.GOLDEN_APPLE,2),
                    new ItemStack(Items.ROTTEN_FLESH,16),
                    new ItemStack(Items.DIAMOND,4),
                    new ItemStack(Items.COOKED_BEEF,4)
            );
        }
        @Override
        public boolean canRecipe(Set<ItemStack> items) {
            return RecipeHandler.canUse(itemList(),items);
        }

        @Override
        public String output() {
            return Handler.mixinName("hungry_wolf");
        }
    }

}
