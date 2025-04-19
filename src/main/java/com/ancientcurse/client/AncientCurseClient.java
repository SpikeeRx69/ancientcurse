package com.ancientcurse.client;

import com.ancientcurse.AncientCurse;
import com.ancientcurse.ModBlocks;
import com.ancientcurse.block.RockBlock;
import com.ancientcurse.client.color.RockColorProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

/**
 * Client-side initialization for the Ancient Curse mod.
 * Handles client-specific features and block rendering.
 */
@Environment(EnvType.CLIENT)
public class AncientCurseClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AncientCurse.LOGGER.info("Initializing Ancient Curse Client");
        
        // Register render layers for transparent blocks
        registerRenderLayers();
        
        // Register color providers
        registerColorProviders();
        
        AncientCurse.LOGGER.info("Ancient Curse Client initialized");
    }
    
    /**
     * Register render layers for blocks that need special rendering
     */
    private void registerRenderLayers() {
        // Register cutout render layers for blocks with transparency
        
        // Add vegetation blocks with transparency here as needed
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NILE_RIVER_GRASS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PAPYRUS_REED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DEAD_PAPYRUS_REED, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DWARF_PAPYRUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EGYPTIAN_SPINACH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EUPHORBIA_HELIOSCOPIA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LIGHT_DEAD_FERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MINI_CACTUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PISTIA_STRATIOTES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOTUS_FLOWER_PAD, RenderLayer.getCutout());
        
        // Note: NILE_RIVER_TALL_GRASS removed to fix compilation issues
    }
    
    /**
     * Register color providers for blocks that need dynamic coloring
     */
    private void registerColorProviders() {
        // Create color provider instances
        RockColorProvider rockColorProvider = new RockColorProvider();
        
        // Register block color providers
        ColorProviderRegistry.BLOCK.register(rockColorProvider, 
            ModBlocks.SMALL_ROCK, 
            ModBlocks.MEDIUM_ROCK,
            ModBlocks.LARGE_ROCK
        );
        
        // Register item color providers
        ColorProviderRegistry.ITEM.register(rockColorProvider,
            ModBlocks.SMALL_ROCK,
            ModBlocks.MEDIUM_ROCK,
            ModBlocks.LARGE_ROCK
        );
    }
}
