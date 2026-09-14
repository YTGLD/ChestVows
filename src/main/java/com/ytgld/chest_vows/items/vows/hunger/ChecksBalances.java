package com.ytgld.chest_vows.items.vows.hunger;

import com.google.common.collect.Multimap;
import com.ytgld.vows.Vows;
import com.ytgld.vows.attributre.VowsAttributes;
import com.ytgld.vows.items.BaseVows;
import com.ytgld.vows.tool.*;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.List;
import java.util.Set;

public class ChecksBalances extends BaseVows {
    public ChecksBalances(Properties properties) {
        super(properties);
    }
    @Override
    public void tickVows(LivingEntity entity) {
        super.tickVows(entity);
        if (entity instanceof Player player) {
            player.causeFoodExhaustion(hungerDown(player));
        }
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = super.doAttribute(livingEntity, item);

        float soulShield = 0;

        if (livingEntity instanceof Player player) {
            FoodData foodData=  player.getFoodData();

            soulShield = 20 - foodData.getFoodLevel();

            soulShield *= hungerToSoulShield(player);
        }

        modifierMultimap.put(VowsAttributes.soulShieldMaxValue,
                new AttributeModifier(Vows.fromNamespaceAndPath("checks_balances"),
                        soulShield, AttributeModifier.Operation.ADD_VALUE)) ;
        return modifierMultimap;
    }
    public static void UmmDamage(LivingIncomingDamageEvent event){
        LivingEntity livingEntity = event.getEntity();
        if (Handler.has(livingEntity, Handler.mixinName("checks_balances"))) {
            if (event.getSource().is(DamageTypes.STARVE)) {
                event.setAmount(0);
                event.setCanceled(true);
            }
        }
    }
    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        addText(tooltipComponents,Component.translatable("vows.vows.checks_balances.1"),true);
        addText(tooltipComponents,Component.translatable("vows.vows.checks_balances.2"),true);
        addText(tooltipComponents,Component.translatable("vows.vows.checks_balances.3"),false);
    }
    @Override
    public String itemName() {
        return Handler.mixinName("checks_balances");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,20,255,20),
                        Vows.fromNamespaceAndPath("textures/soul/soul_2.png")),
                new ColorAndImage(Light.ARGB.color(255,0,220,50),
                        Vows.fromNamespaceAndPath("textures/soul/soul_3.png")),
                new ColorAndImage(Light.ARGB.color(255,50,200,0),
                        Vows.fromNamespaceAndPath("textures/soul/soul_5.png")),
                new ColorAndImage(Light.ARGB.color(255,100,180,100),
                        Vows.fromNamespaceAndPath("textures/soul/soul_8.png")),
                new ColorAndImage(Light.ARGB.color(255,10,180,10),
                        Vows.fromNamespaceAndPath("textures/soul/soul_10.png"))
        );
    }

    @Override
    public Component textMain() {
        return Component.translatable("vows.vows.checks_balances");
    }
    public int hungerToSoulShield(LivingEntity livingEntity){
        return (int) Handler.doValue(2,livingEntity);
    }
    public float hungerDown(LivingEntity livingEntity){
        return Handler.doValue(0.0225f,livingEntity);
    }
    @RecipePlugin
    public static class Recipe implements RegisterRecipeConfig {

        @Override
        public List<ItemStack> itemList() {
            return List.of(
                    new ItemStack(Items.APPLE,2),
                    new ItemStack(Items.ECHO_SHARD,3),
                    new ItemStack(Items.ROTTEN_FLESH,18)
            );
        }
        @Override
        public boolean canRecipe(Set<ItemStack> items) {
            return RecipeHandler.canUse(itemList(),items);
        }

        @Override
        public String output() {
            return Handler.mixinName("checks_balances");
        }
    }
}
