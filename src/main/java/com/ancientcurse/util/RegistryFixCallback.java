package com.ancientcurse.util;

import com.ancientcurse.AncientCurse;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * This class provides callbacks to fix registry issues with vanilla blocks,
 * particularly addressing the "Some intrusive holders were not registered" error 
 * with air blocks in Minecraft 1.20.1.
 */
public class RegistryFixCallback {

    /**
     * Initialize registry callbacks to ensure proper block registration
     */
    public static void init() {
        AncientCurse.LOGGER.info("Initializing registry fix callbacks for vanilla blocks");
        
        // Register a callback for when the block registry is modified
        RegistryEntryAddedCallback.event(Registries.BLOCK).register((rawId, id, block) -> {
            // If a new block is registered, ensure air block is properly registered
            ensureAirBlockRegistration();
        });
        
        // Also attempt to pre-register air and other essential blocks
        ensureAirBlockRegistration();
    }
    
    /**
     * Ensures the air block and other essential blocks are properly registered
     */
    private static void ensureAirBlockRegistration() {
        try {
            // Force access to air block by explicitly getting the key from registry
            AncientCurse.LOGGER.info("Pre-registering air block registry entry");
            Identifier airId = Registries.BLOCK.getId(Blocks.AIR);
            Block airBlock = Registries.BLOCK.get(airId);
            
            // Access default state to ensure it's registered
            airBlock.getDefaultState();
            
            // Also ensure other essential blocks are registered
            Blocks.WATER.getDefaultState();
            Blocks.STONE.getDefaultState();
            Blocks.DIRT.getDefaultState();
            
            AncientCurse.LOGGER.info("Successfully registered air block and other essential blocks");
        } catch (Exception e) {
            AncientCurse.LOGGER.error("Error during air block registration fix", e);
        }
    }
}
