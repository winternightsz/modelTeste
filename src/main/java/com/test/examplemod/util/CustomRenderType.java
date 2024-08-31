package com.test.examplemod.util;

import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class CustomRenderType extends RenderType{

    public CustomRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn)
    {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }
    //private static final BiFunction<ResourceLocation, Boolean, RenderType> POLYGON = Util.memoize((texture, transparent) ->
    //{
    //    RenderType.CompositeState state = RenderType.CompositeState.builder()
    //            .setLightmapState(LIGHTMAP)
    //            .setShaderState(RENDERTYPE_SOLID_SHADER)
    //            .setTextureState(BLOCK_SHEET_MIPPED)
    //            .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
    //            .createCompositeState(true);
    //    return create(("polygon"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLE_STRIP, 256, true, false, state);
    //});

    private static final BiFunction<ResourceLocation, Boolean, RenderType> POLYGON   = Util.memoize((texture, transparent) ->
    {
        RenderType.CompositeState state = RenderType.CompositeState.builder()

                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY).createCompositeState(transparent);


        //.setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)
        //.setTextureState(NO_TEXTURE)
        //.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
        //.setCullState(NO_CULL)
        //.setLightmapState(LIGHTMAP)
        //.createCompositeState(false);
        return create(("polygon"), DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256, true, false, state);
    });

    public static RenderType polygon(ResourceLocation texture, boolean transparent)
    {
        return POLYGON.apply(texture, transparent);
    }


}