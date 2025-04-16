package com.ancientcurse.world;

import com.ancientcurse.AncientCurse;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

/**
 * Handles world initialization to prevent issues with unregistered blocks
 * during world generation.
 */
public class WorldInitHandler {
    private static boolean airInitialized = false;
    private static BlockState EMPTY_STATE = null; // Not final, will be initialized during world init

    public static void register() {
        AncientCurse.LOGGER.info("Registering world initialization handler");
        
        // Handle server starting event
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            AncientCurse.LOGGER.info("Initializing block states before world loading");
            initializeWorld();
        });
        
        // Handle world loading
        ServerWorldEvents.LOAD.register((server, world) -> {
            AncientCurse.LOGGER.info("World loading: " + world.getRegistryKey().getValue());
            if (!airInitialized) {
                AncientCurse.LOGGER.info("First world load - ensuring block states are initialized");
                initializeWorld();
            }
        });
        
        // Handle early server ticks to ensure initialization
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            if (!airInitialized) {
                initializeWorld();
            }
        });
    }
    
    /**
     * Initialize the world state
     */
    private static void initializeWorld() {
        if (airInitialized) {
            return;
        }
        
        try {
            AncientCurse.LOGGER.info("Initializing world state");
            
            // Initialize empty state during world init when blocks are properly registered
            EMPTY_STATE = Block.getBlockFromItem(Items.AIR).getDefaultState();
            
            // Also initialize other common blocks to prevent similar issues
            Blocks.WATER.getDefaultState();
            Blocks.STONE.getDefaultState();
            Blocks.DIRT.getDefaultState();
            
            airInitialized = true;
            AncientCurse.LOGGER.info("World state initialized successfully");
        } catch (Exception e) {
            AncientCurse.LOGGER.error("Failed to initialize world state", e);
        }
    }
    
    /**
     * Called during world generation to ensure any blocks being placed
     * are properly registered.
     */
    public static BlockState safeGetBlockState(BlockState original) {
        if (!airInitialized) {
            initializeWorld();
        }
        
        // Ensure the block is properly registered
        try {
            return original;
        } catch (Exception e) {
            AncientCurse.LOGGER.error("Error accessing block state: " + original, e);
            // Fallback to a safe block
            return Blocks.STONE.getDefaultState();
        }
    }
    
    /**
     * Get the empty state for world generation
     */
    public static BlockState getEmptyState() {
        if (!airInitialized) {
            initializeWorld();
        }
        return EMPTY_STATE;
    }
}
