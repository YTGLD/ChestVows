package com.ytgld.chest_vows.mixin.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ytgld.chest_item.other.IPlayer;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.items.BaseVows;
import com.ytgld.chest_vows.other.CVAttReg;
import com.ytgld.chest_vows.other.IVowsPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mixin(Player.class)
public class PlayerMixin implements IVowsPlayer {
    @Unique
    private Map<Item, Multimap<Holder<Attribute>, AttributeModifier>> ChestVows$AttributeModifier = new HashMap<>();
    @Unique
    private void ChestVows$updateAttribute() {
        Player player = (Player) (Object) this;
        Set<String> set = player.getData(CVAttReg.vows);
        for (String name : set){
            Item item = VowHandler.getVowsItemForName(name);
            if (item instanceof BaseVows baseVows) {
                Multimap<Holder<Attribute>, AttributeModifier> doAttribute = baseVows.doAttribute(player,item);
                ChestVows$AttributeModifier.getOrDefault(item, HashMultimap.create()).forEach((attributeHolder, attributeModifier)->{
                    Multimap<Holder<Attribute>, AttributeModifier> modifiers = HashMultimap.create();
                    modifiers.put(attributeHolder,attributeModifier);
                    player.getAttributes().removeAttributeModifiers(modifiers);
                });
                player.getAttributes().addTransientAttributeModifiers(doAttribute);

                ChestVows$AttributeModifier.put(item, doAttribute);
            }
        }
    }
    @Inject(method = "tick", at = @At(value = "RETURN"))
    private void tick(CallbackInfo ci) {
        ChestVows$updateAttribute();
    }

    @Override
    public void chestVows26_2$clear() {
        Player player = (Player) (Object) this;
        for(Multimap<Holder<Attribute>, AttributeModifier> attributeModifierMultimap : this.ChestVows$AttributeModifier.values()) {
            player.getAttributes().removeAttributeModifiers(attributeModifierMultimap);
        }
    }
}
