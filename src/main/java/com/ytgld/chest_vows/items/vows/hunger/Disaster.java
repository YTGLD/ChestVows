package com.ytgld.chest_vows.items.vows.hunger;

import com.google.common.collect.Multimap;
import com.ytgld.vows.Vows;
import com.ytgld.vows.items.BaseVows;
import com.ytgld.vows.tool.*;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

public class Disaster extends BaseVows {
    public Disaster(Properties properties) {
        super(properties);
    }

    public static void can(Player player, CallbackInfoReturnable<Boolean> cir){
        if (Handler.has(player,Handler.mixinName("disaster"))){
            cir.setReturnValue(true);
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = super.doAttribute(livingEntity, item);

        float speedAndDamage = 0;
        float max = speedAdd(livingEntity);
        if (livingEntity instanceof Player player) {
            FoodData foodData=  player.getFoodData();
            float c = 20 - foodData.getFoodLevel();
            speedAndDamage = (max / 20f) * c;
        }

        modifierMultimap.put(Attributes.MOVEMENT_SPEED,
                new AttributeModifier(Vows.fromNamespaceAndPath("disaster"),
                        speedAndDamage, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)) ;


        return modifierMultimap;
    }
    @Override
    public void tickVows(LivingEntity entity) {
        super.tickVows(entity);
        if (entity instanceof Player player) {
            if (Handler.has(player,Handler.mixinName("disaster"))) {
                player.causeFoodExhaustion(hunger(player));
            }
        }
    }
    @Override
    public String itemName() {
        return Handler.mixinName("disaster");
    }
    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        addText(tooltipComponents,Component.translatable("vows.vows.disaster.1"),true);
        addText(tooltipComponents,Component.translatable("vows.vows.disaster.2"),true);
        addText(tooltipComponents,Component.translatable("vows.vows.disaster.3"),false);
    }
    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,20,255,20),
                        Vows.fromNamespaceAndPath("textures/soul/soul_2.png")),
                new ColorAndImage(Light.ARGB.color(255,0,220,50),
                        Vows.fromNamespaceAndPath("textures/soul/soul_6.png")),
                new ColorAndImage(Light.ARGB.color(255,50,200,0),
                        Vows.fromNamespaceAndPath("textures/soul/soul_12.png")),
                new ColorAndImage(Light.ARGB.color(255,100,180,100),
                        Vows.fromNamespaceAndPath("textures/soul/soul_13.png")),
                new ColorAndImage(Light.ARGB.color(255,10,180,10),
                        Vows.fromNamespaceAndPath("textures/soul/soul_14.png"))
        );
    }
    public static float speedAdd(LivingEntity livingEntity){
        return Handler.doValue(0.2f,livingEntity);
    }
    public static float hunger(Player player){
        return Handler.doValue(0.0125f,player);
    }
    @Override
    public Component textMain() {
        return Component.translatable("vows.vows.disaster");
    }
    @RecipePlugin
    public static class Recipe implements RegisterRecipeConfig {

        @Override
        public List<ItemStack> itemList() {
            return List.of(
                    new ItemStack(Items.SUGAR,24),
                    new ItemStack(Items.SPIDER_EYE,8)
            );
        }
        @Override
        public boolean canRecipe(Set<ItemStack> items) {
            return RecipeHandler.canUse(itemList(),items);
        }
        @Override
        public String output() {
            return Handler.mixinName("disaster");
        }
    }
}
