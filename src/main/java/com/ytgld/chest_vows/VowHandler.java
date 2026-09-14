package com.ytgld.chest_vows;

import com.ytgld.chest_vows.other.CVAttReg;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;

public class VowHandler {
    public static boolean has(LivingEntity player,String itemName){
        Set<String> strings = player.getData(CVAttReg.vows);
        return strings.contains(itemName);
    }
    public static boolean has(LivingEntity player,Item itemName){
        Set<Item> strings = getVowsItems(player);
        return strings.contains(itemName);
    }
    public static Set<Item> getVowsItems(LivingEntity player){
        Set<Item> set = new HashSet<>();
        if (player!=null) {
            Set<String> strings = player.getData(CVAttReg.vows);
            if (!strings.isEmpty()) {
                for (String name : strings) {
                    Item item = BuiltInRegistries.ITEM.getValue(Identifier.parse(name));
                    set.add(item);
                }
            }
        }
        return set;
    }


    public static boolean addVows(Player player,String itemName){
        Set<String> strings = player.getData(CVAttReg.vows);
        return strings.add(itemName);
    }
    public static int getMaxVows(Player player){
        return 3;
    }

    public static Item getVowsItemForName(String itemName){
        return BuiltInRegistries.ITEM.getValue(Identifier.parse(itemName));
    }
    public static String mixinName(String s){
        return ChestVows.MODID + ":" + s;
    }
}
