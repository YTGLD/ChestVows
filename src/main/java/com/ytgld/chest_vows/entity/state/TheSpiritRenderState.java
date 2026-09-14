package com.ytgld.chest_vows.entity.state;

import com.ytgld.chest_vows.entity.TheSpirit;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class TheSpiritRenderState extends EntityRenderState {
    public TheSpirit entity;
    public float partialTick;
    public final ItemStackRenderState item;

    public TheSpiritRenderState() {
        item = new ItemStackRenderState();
    }
}
