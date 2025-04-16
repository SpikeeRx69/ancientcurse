package com.ancientcurse.world.dimension;

import com.ancientcurse.AncientCurse;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.biome.Biome;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

/**
 * Registers dimensions for the Ancient Curse mod.
 * Uses custom dimension types for proper Egyptian environment.
 */
public class ModDimensions {
    // Define our dimension registry keys
    public static final RegistryKey<DimensionOptions> ANCIENT_CURSE_DIMENSION = 
        RegistryKey.of(RegistryKeys.DIMENSION, new Identifier(AncientCurse.MOD_ID, "ancient_curse"));
    
    public static final RegistryKey<Biome> NILE_RIVER_BIOME = 
        RegistryKey.of(RegistryKeys.BIOME, new Identifier(AncientCurse.MOD_ID, "nile_river"));
    
    /**
     * Registers the dimension using our custom dimension type.
     */
    public static void register() {
        AncientCurse.LOGGER.info("Registering Ancient Curse dimension using custom dimension type");
        
        // Check if dimension JSON file exists
        boolean dimensionFileExists = ModDimensions.class.getClassLoader().getResource(
                "data/ancientcurse/worldgen/dimension/ancient_curse.json") != null;
        
        boolean dimensionTypeFileExists = ModDimensions.class.getClassLoader().getResource(
                "data/ancientcurse/dimension_type/ancient_curse.json") != null;
                
        if (dimensionFileExists) {
            AncientCurse.LOGGER.info("Dimension JSON file found, using ancientcurse:ancient_curse dimension type");
        } else {
            AncientCurse.LOGGER.error("Dimension JSON file NOT found! Check your resources.");
        }
        
        if (dimensionTypeFileExists) {
            AncientCurse.LOGGER.info("Dimension TYPE JSON file found, custom dimension type will be used");
        } else {
            AncientCurse.LOGGER.error("Dimension TYPE JSON file NOT found! Check your resources.");
        }
        
        // Set up server started event to check registration success
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            // Check if our dimension is registered
            boolean dimensionRegistered = server.getRegistryManager()
                .get(RegistryKeys.DIMENSION)
                .contains(ANCIENT_CURSE_DIMENSION);
            
            // Log success or failure
            if (dimensionRegistered) {
                AncientCurse.LOGGER.info("Ancient Curse dimension successfully registered!");
            } else {
                AncientCurse.LOGGER.error("Ancient Curse dimension NOT registered correctly!");
                AncientCurse.LOGGER.info("Available dimensions:");
                server.getRegistryManager().get(RegistryKeys.DIMENSION).getKeys().forEach(key -> {
                    AncientCurse.LOGGER.info(" - " + key.getValue());
                });
            }
        });
        
        AncientCurse.LOGGER.info("Ancient Curse dimensions registration complete");
    }
} 