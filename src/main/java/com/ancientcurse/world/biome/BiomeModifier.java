package com.ancientcurse.world.biome;

import com.ancientcurse.AncientCurse;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;

/**
 * Handles runtime biome modifications to ensure custom blocks are properly used
 * in the Nile River biome.
 */
public class BiomeModifier {

    /**
     * Register biome modifications to replace vanilla blocks with custom blocks.
     */
    public static void register() {
        AncientCurse.LOGGER.info("Registering biome modifiers for Ancient Curse mod");
        
        // Create feature entry for custom Nile River generation
        BiomeModifications.create(new Identifier(AncientCurse.MOD_ID, "nile_river_generation"))
            .add(ModificationPhase.REPLACEMENTS, // This runs after everything else
                BiomeSelectors.includeByKey(ModBiomes.NILE_RIVER),
                context -> {
                    AncientCurse.LOGGER.info("Applying biome modifications to Nile River biome");
                    
                    // Add our custom feature to replace blocks in the Nile River biome
                    context.getGenerationSettings().addFeature(
                        GenerationStep.Feature.TOP_LAYER_MODIFICATION,
                        RegistryKey.of(
                            net.minecraft.registry.RegistryKeys.PLACED_FEATURE,
                            new Identifier(AncientCurse.MOD_ID, "nile_direct_replacer")
                        )
                    );
                }
            );
    }
}
