package com.ytgld.chest_vows.event;

import com.google.common.collect.HashMultimap;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.items.BaseVows;
import com.ytgld.chest_vows.items.vows.blood.BloodArmy;
import com.ytgld.chest_vows.items.vows.blood.ChiefPriest;
import com.ytgld.chest_vows.items.vows.blood.SwordHolder;
import com.ytgld.chest_vows.items.vows.evil.Abandon;
import com.ytgld.chest_vows.other.CVAttReg;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.client.event.GatherSkippedAttributeTooltipsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import net.neoforged.neoforge.common.util.AttributeUtil;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.SweepAttackEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CVEvent {

    @SubscribeEvent
    public void event(LivingDamageEvent.Pre event){
        SwordHolder.event(event);
        BloodArmy.event(event);
        ChiefPriest.event(event);
        Abandon.event(event);
    }
    @SubscribeEvent
    public void event(LivingHealEvent event){
        BloodArmy.event(event);
        ChiefPriest.event(event);
    }
    @SubscribeEvent
    public void event(SweepAttackEvent event){
        SwordHolder.event(event);
    }
    @SubscribeEvent
    public void event(EntityTickEvent.Pre event){
        if (event.getEntity() instanceof Player livingEntity) {
            Set<String> set = livingEntity.getData(CVAttReg.vows);
            for (String name : set){
                Item item = VowHandler.getVowsItemForName(name);
                if (item instanceof BaseVows baseVows) {
                    baseVows.tickVows(livingEntity);
                }
            }
        }
    }

    @SubscribeEvent
    public void event(AddAttributeTooltipsEvent evt){
        AttributeTooltipContext context = evt.getContext();
        ItemStack stack = evt.getStack();
        GatherSkippedAttributeTooltipsEvent skipped =
                NeoForge.EVENT_BUS.post(new GatherSkippedAttributeTooltipsEvent(stack, context));

        if (skipped.isSkippingAll()) {
            return;
        }
        List<Component> attributesTooltip = new ArrayList<>();
        Player player = context.player();
        if (player!=null) {
            if (stack.getItem() instanceof BaseVows baseVows) {
                evt.addTooltipLines(Component.empty());
                attributesTooltip.add(Component.translatable("chest_vows.vows.attribute").
                        withStyle(ChatFormatting.GOLD));
                AttributeUtil.applyTextFor(
                        stack,
                        attributesTooltip::add,
                        HashMultimap.create(),
                        AttributeTooltipContext.of(player, context,context.flag()));
                baseVows.applyText(baseVows.getDefaultInstance(),attributesTooltip,evt.getContext().flag());
                for (Component component : attributesTooltip) {
                    MutableComponent co = component.copy();
                    evt.addTooltipLines(co);
                }
            }
        }
    }
}
