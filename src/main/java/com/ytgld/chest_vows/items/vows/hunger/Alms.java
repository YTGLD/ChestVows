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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Set;

public class Alms extends BaseVows {
    public Alms(Properties properties) {
        super(properties);
    }

    @Override
    public String itemName() {
        return Handler.mixinName("alms");
    }

    @Override
    public List<ColorAndImage> colorAndImage() {
        return List.of(
                new ColorAndImage(Light.ARGB.color(255,20,255,20),
                        Vows.fromNamespaceAndPath("textures/soul/soul_7.png")),
                new ColorAndImage(Light.ARGB.color(255,0,220,50),
                        Vows.fromNamespaceAndPath("textures/soul/soul_9.png")),
                new ColorAndImage(Light.ARGB.color(255,50,200,0),
                        Vows.fromNamespaceAndPath("textures/soul/soul_11.png"))
        );
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity, Item item) {
        Multimap<Holder<Attribute>, AttributeModifier> multimap = super.doAttribute(livingEntity, item);
        multimap.put(Attributes.MAX_HEALTH,new AttributeModifier(Vows.fromNamespaceAndPath("alms"),
                -healthDown(livingEntity), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return multimap;
    }
    public static int healTime(Player player,int oTime){
        if (Handler.has(player, Handler.mixinName("alms"))) {
            int c = (int) (1 / hungerHeal(player));
            if (c < 1) {
                c = 1;
            }
            if (player.tickCount % c == 0) {
                return oTime + 1;
            }
        }
        return oTime;
    }
    @Override
    public void applyText(ItemStack stack, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.applyText(stack, tooltipComponents, tooltipFlag);
        addText(tooltipComponents,Component.translatable("vows.vows.alms.1"),true);
        addText(tooltipComponents,Component.translatable("vows.vows.alms.2"),false);
    }

    @Override
    public Component textMain() {
        return Component.translatable("vows.vows.alms");

    }
    public static float healthDown(LivingEntity livingEntity){
        return Handler.doValue(0.1f,livingEntity);
    }

    public static float hungerHeal(LivingEntity livingEntity){
        return Handler.doValue(0.5f,livingEntity);
    }
    @RecipePlugin
    public static class Recipe implements RegisterRecipeConfig {

        @Override
        public List<ItemStack> itemList() {
            return List.of(
                    new ItemStack(Items.ROTTEN_FLESH,32),
                    new ItemStack(Items.COOKED_COD,8),
                    new ItemStack(Items.COD,2)
            );
        }
        @Override
        public boolean canRecipe(Set<ItemStack> items) {
            return RecipeHandler.canUse(itemList(),items);
        }

        @Override
        public String output() {
            return Handler.mixinName("alms");
        }
    }
}
