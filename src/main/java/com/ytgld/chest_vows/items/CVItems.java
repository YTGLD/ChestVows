package com.ytgld.chest_vows.items;

import com.ytgld.chest_vows.ChestVows;
import com.ytgld.chest_vows.items.vows.blood.BloodSacrifice;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class CVItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ChestVows.MODID);

    public static final DeferredHolder<Item ,Item > BloodSacrifice_ =
            register("blood_sacrifice",(resourceLocation -> new BloodSacrifice(new Item.Properties().stacksTo(1))));
    public static class Tab{
        public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChestVows.MODID);
        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> tab =
                CREATIVE_MODE_TABS.register("chest_vows", () -> CreativeModeTab.builder()
                .title(Component.translatable("chest_vows.tab"))
                .icon(CVItems.BloodSacrifice_.get()::getDefaultInstance)
                .displayItems((parameters, output) -> {
                    output.accept(CVItems.BloodSacrifice_.get());
                }).build());
    }


    public static DeferredItem<Item> register(String name, Function<ResourceLocation, ? extends Item> func) {
        return ITEMS.register(name,func);
    }

}
