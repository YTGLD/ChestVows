package com.ytgld.chest_vows.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.Handler;
import com.ytgld.chest_vows.sounds.CVSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseVows extends Item {
    public BaseVows(Properties properties) {
        super(properties.stacksTo(1));
    }
    public abstract String itemName();
    public abstract List<ColorAndImage> colorAndImage();
    public abstract Component textMain();
    public record ColorAndImage(int color , ResourceLocation image){}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (Handler.getVowsItems(player).contains(player.getItemInHand(usedHand).getItem())) {
            player.displayClientMessage(Component.translatable("chest_vows.vows.has").withStyle(Style.EMPTY.withColor(0xffff0000)),false);
            return super.use(level, player, usedHand);
        }else {
            if (Handler.getVowsItems(player).size() < Handler.getMaxVows(player)) {
                player.level().playSound(null,player.blockPosition(), CVSounds.use_vows.value(), SoundSource.PLAYERS,1,1);
                Handler.addVows(player, itemName());
                stack.shrink(1);
                return InteractionResultHolder.pass(stack);
            } else {
                player.displayClientMessage(Component.translatable("chest_vows.vows.max").withStyle(Style.EMPTY.withColor(0xffff0000)),false);
            }
        }
        return super.use(level, player, usedHand);
    }
    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Component component = super.getName(stack);
        MutableComponent co = component.copy();
        MutableComponent soul =  Component
                .translatable("chest_vows.vows")
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(Light.ARGB.color(255, 120, 90, 180))));
        co.setStyle(Style.EMPTY.withColor(Light.ARGB.color(255, 210, 75, 210)));

        return soul.append(Component.literal("<").withStyle(ChatFormatting.GRAY))
                .append(co)
                .append(Component.literal(">").withStyle(ChatFormatting.GRAY));
    }

    public void tickVows(LivingEntity entity){

    }

    public Multimap<Holder<Attribute>, AttributeModifier> doAttribute(LivingEntity livingEntity,Item item){
        return HashMultimap.create();
    }

    public void applyText(ItemStack stack,List<Component> tooltipComponents,TooltipFlag tooltipFlag){}

    public void addText(List<Component> list,MutableComponent component,boolean positive){
        int color = Light.ARGB.color(255,210,45,80);
        String at = "+";
        if (!positive) {
            color = Light.ARGB.color(255,180,135,70);
            at = "-";
        }
        list.add(Component.literal(at).withStyle(Style.EMPTY.withColor(color))
                .append(component.withStyle(Style.EMPTY.withColor(color))));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(textMain().copy().withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
    }
}
