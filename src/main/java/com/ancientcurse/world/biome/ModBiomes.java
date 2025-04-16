package com.ancientcurse.world.biome;

import com.ancientcurse.AncientCurse;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

/**
 * Defines biome registry keys and handles biome registration.
 */
public class ModBiomes {
    
    // Define biome registry keys
    public static final RegistryKey<Biome> ANCIENT_DESERT = RegistryKey.of(
            RegistryKeys.BIOME, new Identifier(AncientCurse.MOD_ID, "ancient_desert"));
    
    public static final RegistryKey<Biome> NILE_RIVER = RegistryKey.of(
            RegistryKeys.BIOME, new Identifier(AncientCurse.MOD_ID, "nile_river"));
    
    /**
     * Register biomes
     */
    public static void registerBiomes() {
        AncientCurse.LOGGER.info("Registering biomes for " + AncientCurse.MOD_ID);
        
        // Biomes are defined through JSON files in data/ancientcurse/worldgen/biome/
        // This method is just for logging and any programmatic registration if needed
    }
} 