package com.ytgld.chest_vows.render;


import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.ytgld.chest_vows.ChestVows;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.resources.Identifier;

import static com.mojang.blaze3d.platform.BlendFactor.*;
import static net.minecraft.client.renderer.RenderPipelines.GLOBALS_SNIPPET;

public class VowsRenders {
    private static final RenderPipeline.Snippet  GUI_TEXTURED_SNIPPET = RenderPipeline.builder(GLOBALS_SNIPPET).
            withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION).
            withVertexShader(Identifier.fromNamespaceAndPath(ChestVows.MODID,"core/vows")).withFragmentShader(Identifier.fromNamespaceAndPath(ChestVows.MODID,"core/vows"))
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0).withColorTargetState(new ColorTargetState(new BlendFunction(
                    SRC_ALPHA,
                    ONE,
                    ONE,
                    ZERO)))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR).withPrimitiveTopology(PrimitiveTopology.QUADS).buildSnippet();

    public static final RenderPipeline Vows =
            (RenderPipeline.builder(GUI_TEXTURED_SNIPPET).
                    withLocation(Identifier.fromNamespaceAndPath(ChestVows.MODID,"pipeline/vows")).build());

}
