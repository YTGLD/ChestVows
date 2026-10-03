package com.ytgld.chest_vows.other;

import com.ytgld.chest_item.items.memory.IntAndStringSyncHandler;
import com.ytgld.chest_item.items.memory.TheMemoryDataHandler;
import com.ytgld.chest_vows.ChestVows;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class CVAttReg {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ChestVows.MODID);

    public static final Supplier<AttachmentType<Set<String>>> vows = ATTACHMENT_TYPES.register(
            "vows",
            () -> AttachmentType.<Set<String>>builder(() -> new HashSet<>())
                    .sync(new TheMemoryDataHandler.StringSetSync())
                    .serialize(TheMemoryDataHandler.StringSetCodec.CODEC.fieldOf("vows"))
                    .build()
    );

    public static final Supplier<AttachmentType<IntAndStringSyncHandler.ISClass>> playerTag = ATTACHMENT_TYPES.register(
            "player_tag",
            () -> AttachmentType.<IntAndStringSyncHandler.ISClass>builder(() -> new IntAndStringSyncHandler.ISClass(new HashMap<>()))
                    .sync(new IntAndStringSyncHandler())
                    .serialize(IntAndStringSyncHandler.CODEC.fieldOf("player_tag"))
                    .build()
    );
}
