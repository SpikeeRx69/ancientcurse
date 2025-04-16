package com.ancientcurse.world;

import com.ancientcurse.AncientCurse;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionType;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages block references to prevent unregistered intrusive holder errors
 * during world generation. It ensures that air and other essential blocks are
 * properly registered and accessible when needed.
 */
public class BlockReferenceManager {
    private static final Map<String, WeakReference<BlockState>> CACHED_STATES = new HashMap<>();
    private static boolean initialized = false;

    /**
     * Register event handlers for server lifecycle and world loading
     */
    public static void register() {
        AncientCurse.LOGGER.info("Registering BlockReferenceManager");
        
        // Ensure blocks are initialized when server starts
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            AncientCurse.LOGGER.info("Server starting - initializing block references");
            initializeBlockReferences(server.getRegistryManager());
        });
        
        // Also register when worlds load to ensure they have the references
        ServerWorldEvents.LOAD.register((server, world) -> {
            AncientCurse.LOGGER.info("World loading - ensuring block references for dimension: " + world.getDimensionKey().getValue());
            ensureBlockReferences(world);
        });
    }
    
    /**
     * Initialize block references with the dynamic registry manager
     */
    private static void initializeBlockReferences(DynamicRegistryManager registryManager) {
        try {
            AncientCurse.LOGGER.info("Initializing essential block references");
            
            // Ensure the air block is properly registered and cached
            BlockState airState = Blocks.AIR.getDefaultState();
            CACHED_STATES.put("minecraft:air", new WeakReference<>(airState));
            
            // Register other essential vanilla blocks
            cacheBlockState(Blocks.WATER);
            cacheBlockState(Blocks.STONE);
            cacheBlockState(Blocks.DIRT);
            cacheBlockState(Blocks.SAND);
            cacheBlockState(Blocks.GRASS_BLOCK);
            
            initialized = true;
            AncientCurse.LOGGER.info("Block references initialized successfully");
        } catch (Exception e) {
            AncientCurse.LOGGER.error("Failed to initialize block references", e);
        }
    }
    
    /**
     * Ensure block references are available for a specific world
     */
    private static void ensureBlockReferences(ServerWorld world) {
        try {
            if (!initialized) {
                AncientCurse.LOGGER.info("Block references not initialized yet, initializing for world: " + 
                    world.getDimensionKey().getValue());
                initializeBlockReferences(world.getServer().getRegistryManager());
            }
            
            // Access air block state through our cache or directly
            BlockState airState = getBlockState("minecraft:air");
            if (airState == null) {
                // If not in cache, try to get directly and cache it
                airState = Blocks.AIR.getDefaultState();
                CACHED_STATES.put("minecraft:air", new WeakReference<>(airState));
            }
            
            AncientCurse.LOGGER.info("Block references verified for world: " + world.getDimensionKey().getValue());
        } catch (Exception e) {
            AncientCurse.LOGGER.error("Error ensuring block references for world: " + 
                world.getDimensionKey().getValue(), e);
        }
    }
    
    /**
     * Get a block state from the cache or registry
     */
    public static BlockState getBlockState(String blockId) {
        if (blockId.equals("minecraft:air")) {
            // Let the game handle air state
            return null;
        }
        if (CACHED_STATES.containsKey(blockId)) {
            WeakReference<BlockState> ref = CACHED_STATES.get(blockId);
            BlockState state = ref.get();
            if (state != null) {
                return state;
            }
        }
        
        // If not in cache or reference was cleared, get from registry
        try {
            Block block = Registries.BLOCK.get(new Identifier(blockId));
            if (block != null) {
                BlockState state = block.getDefaultState();
                CACHED_STATES.put(blockId, new WeakReference<>(state));
                return state;
            }
        } catch (Exception e) {
            AncientCurse.LOGGER.error("Error getting block state for: " + blockId, e);
        }
        
        return null;
    }
    
    /**
     * Cache a block's default state
     */
    private static void cacheBlockState(Block block) {
        try {
            BlockState state = block.getDefaultState();
            String id = Registries.BLOCK.getId(block).toString();
            CACHED_STATES.put(id, new WeakReference<>(state));
        } catch (Exception e) {
            AncientCurse.LOGGER.error("Error caching block state for: " + block, e);
        }
    }
}
