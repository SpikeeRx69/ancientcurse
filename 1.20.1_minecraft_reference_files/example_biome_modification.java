package com.ancientcurse.world.biome;

import com.ancientcurse.AncientCurse;
import com.ancientcurse.world.feature.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;

public class ModBiomeModifications {
    public static void registerBiomeModifications() {
        // Add papyrus reeds to Nile river biome
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(ModBiomes.NILE_RIVER_KEY),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.PLACED_PAPYRUS_REED_KEY
        );
        
        // Example of adding features to vanilla desert biomes for compatibility
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(BiomeKeys.DESERT),
            GenerationStep.Feature.VEGETAL_DECORATION,
            ModPlacedFeatures.PLACED_PALM_TREE_KEY
        );
        
        // Add ore features to specific biomes
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_MOUNTAIN),
            GenerationStep.Feature.UNDERGROUND_ORES,
            ModPlacedFeatures.LAPIS_LAZULI_ORE_PLACED_KEY
        );
        
        AncientCurse.LOGGER.info("Registering ModBiomeModifications for " + AncientCurse.MOD_ID);
    }
} 