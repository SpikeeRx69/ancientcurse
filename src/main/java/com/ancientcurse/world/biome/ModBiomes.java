package com.ancientcurse.world.biome;

import com.ancientcurse.AncientCurse;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

/**
 * Template for biome registration that can be implemented later.
 * Currently using vanilla Minecraft biomes.
 */
public class ModBiomes {
    
    /**
     * Register biomes
     */
    public static void registerBiomes() {
        AncientCurse.LOGGER.info("Custom biomes disabled - using vanilla Minecraft biomes");
        
        // Biome registration is disabled.
        // You can implement this method to register custom biomes later.
    }
} 