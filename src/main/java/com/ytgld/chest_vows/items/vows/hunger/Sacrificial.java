package com.ytgld.chest_vows.items.vows.hunger;

import com.ytgld.vows.Vows;
import com.ytgld.vows.items.BaseVows;
import com.ytgld.vows.tool.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.List;
import java.util.Set;

public class Sacrificial extends BaseVows {
    public Sacrificial(Properties properties) {
        super(properties);
    }

    @Override
    public String itemName() {
        return Handler.mixinName("sacrificial");
    }
    @Override
    public Component textMain() {
        return Component.translatable("vows.vows.sacrificial");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,20,255,20),
                        Vows.fromNamespaceAndPath("textures/soul/soul_1.png")),
                new ColorAndImage(Light.ARGB.color(255,0,220,50),
                        Vows.fromNamespaceAndPath("textures/soul/soul_2.png")),
                new ColorAndImage(Light.ARGB.color(255,50,200,0),
                        Vows.fromNamespaceAndPath("textures/soul/soul_3.png")),
                new ColorAndImage(Light.ARGB.color(255,100,180,100),
                        Vows.fromNamespaceAndPath("textures/soul/soul_4.png")),
                new ColorAndImage(Light.ARGB.color(255,10,180,10),
                        Vows.fromNamespaceAndPath("textures/soul/soul_5.png"))
        );
    }

    @Override
    public void tickVows(LivingEntity entity) {
        super.tickVows(entity);
        if (entity instanceof Player player) {
            if (Handler.has(player,Handler.mixinName("sacrificial"))) {
                player.causeFoodExhaustion(hunger(player));
            }
        }
    }
    public static void eat(LivingEntityUseItemEvent.Finish event){
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof Player player) {
            if (Handler.has(player,Handler.mixinName("sacrificial"))) {
                ItemStack stack = event.getItem();
                if (stack.getUseAnimation() == ItemUseAnimation.EAT){
                    FoodProperties properties = stack.get(DataComponents.FOOD);
                    if (properties != null) {
                        float base = eatG(player);
                        player.getFoodData().eat((int) (properties.nutrition() * base), properties.saturation() * base);
                    }
                }
            }
        }
    }
    public static float eatG(Player player){
        return Handler.doValue(0.66f,player);
    }
    public static float hunger(Player player){
        return Handler.doValue(0.0125f,player);
    }
    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        addText(tooltipComponents,Component.translatable("vows.vows.sacrificial.1"),true);
        addText(tooltipComponents,Component.translatable("vows.vows.sacrificial.2"),false);
    }
    @RecipePlugin
    public static class Recipe implements RegisterRecipeConfig {

        @Override
        public List<ItemStack> itemList() {
            return List.of(
                    new ItemStack(Items.POISONOUS_POTATO,4),
                    new ItemStack(Items.ROTTEN_FLESH,32),
                    Items.DIAMOND_HOE.getDefaultInstance()
            );
        }
        @Override
        public boolean canRecipe(Set<ItemStack> items) {
            return RecipeHandler.canUse(itemList(),items);
        }
        @Override
        public String output() {
            return Handler.mixinName("sacrificial");
        }
    }
}
