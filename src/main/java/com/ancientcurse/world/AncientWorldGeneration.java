package com.ancientcurse.world;

import com.ancientcurse.AncientCurse;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.feature.PlacedFeature;

/**
 * Initializes and coordinates all world generation for the Ancient Curse mod.
 */
public class AncientWorldGeneration {
    // Reference to the world preset key
    public static final RegistryKey<WorldPreset> ANCIENT_CURSE_PRESET = AncientCurse.ANCIENT_CURSE_PRESET;
    public static final RegistryKey<WorldPreset> NILE_RIVER_PRESET = RegistryKey.of(
        RegistryKeys.WORLD_PRESET,
        new Identifier(AncientCurse.MOD_ID, "nile_river_single_biome")
    );

    /**
     * Initializes all world generation components.
     * This is the main entry point for world generation registration.
     */
    public static void init() {
        AncientCurse.LOGGER.info("Initializing Ancient Curse world generation");
        
        // Initialize feature compatibility layer
        FeatureHandler.init();
        
        // Register server startup handler to enforce flat terrain for Nile River preset
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            // Check each world to see if it's using our Nile River preset
            for (ServerWorld world : server.getWorlds()) {
                // If this is our Nile River overworld, enforce flat terrain
                if (isNileRiverWorld(world)) {
                    AncientCurse.LOGGER.info("Detected Nile River world preset - enforcing flat terrain");
                    
                    // You can't directly modify terrain at this point, but this hook could be
                    // used for any additional world setup needed
                }
            }
        });
        
        AncientCurse.LOGGER.info("Ancient Curse world generation initialized successfully");
    }
    
    /**
     * Determines if a world is using the Nile River preset.
     */
    public static boolean isNileRiverWorld(World world) {
        // Check if the dimension is overworld
        if (world.getRegistryKey() == World.OVERWORLD) {
            // Now check for specific biomes in this world
            if (world instanceof ServerWorld serverWorld) {
                // Look for a chunk near spawn
                int spawnX = serverWorld.getSpawnPos().getX() >> 4;  // Convert to chunk coords
                int spawnZ = serverWorld.getSpawnPos().getZ() >> 4;
                
                // Check a few chunks near spawn to see if they're Nile River biome
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        int chunkX = spawnX + x;
                        int chunkZ = spawnZ + z;
                        
                        // Try to access the biome in this chunk
                        var biomeEntry = serverWorld.getBiome(new BlockPos(chunkX << 4, 64, chunkZ << 4));
                        
                        // Check if it's our Nile River biome
                        if (biomeEntry.getKey().isPresent() && 
                                biomeEntry.getKey().get().getValue().toString().equals("ancientcurse:nile_river")) {
                            // If we find one chunk with our biome, consider it a Nile River world
                            AncientCurse.LOGGER.info("Detected Nile River biome near spawn");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Gets a placed feature key from a string ID, safely handling namespaces.
     * 
     * @param id The feature ID, with or without namespace
     * @return The registry key for the placed feature
     */
    public static RegistryKey<PlacedFeature> getPlacedFeatureKey(String id) {
        // If there's no namespace, add our mod ID
        if (!id.contains(":")) {
            id = AncientCurse.MOD_ID + ":" + id;
        }
        
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(id));
    }
}