package com.ytgld.chest_vows.items;

import com.ytgld.chest_item.Chestitem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class CVItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Chestitem.MODID);





    public static DeferredItem<Item> register(String name, Function<ResourceLocation, ? extends Item> func) {
        return ITEMS.register(name,func);
    }

}
