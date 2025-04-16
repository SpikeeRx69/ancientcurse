package com.ancientcurse.world.gen;

import com.ancientcurse.ModBlocks;
import com.ancientcurse.world.biome.ModBiomes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.noise.NoiseParametersKeys;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialCondition;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;

public class ModSurfaceRules {

    // Block definitions
    private static final MaterialRule FERTILE_SILT = block(ModBlocks.FERTILE_NILE_SILT);
    private static final MaterialRule DRY_SILT = block(ModBlocks.DRY_NILE_SILT);
    private static final MaterialRule SANDSTONE = block(Blocks.SANDSTONE);
    private static final MaterialRule NILE_MUD = block(ModBlocks.NILE_MUD);
    private static final MaterialRule RIVERBED = block(ModBlocks.RIVERBED);
    private static final MaterialRule ALGAE = block(ModBlocks.ALGAE);
    private static final MaterialRule RIVERBED_MOSS = block(ModBlocks.RIVERBED_MOSS);
    private static final MaterialRule NILE_RIVER_SAND = block(ModBlocks.NILE_RIVER_SAND);
    private static final MaterialRule NILE_RIVER_GRASS = block(ModBlocks.NILE_RIVER_GRASS);
    private static final MaterialRule NILE_RIVER_THIN_GRASS = block(ModBlocks.NILE_RIVER_THIN_GRASS);
    
    // Biome conditions
    private static final MaterialCondition ANCIENT_DESERT_CONDITION = MaterialRules.biome(ModBiomes.ANCIENT_DESERT);
    private static final MaterialCondition NILE_RIVER_CONDITION = MaterialRules.biome(ModBiomes.NILE_RIVER);
    
    // Surface rules factory method
    public static MaterialRule createSurfaceRules() {
        return MaterialRules.sequence(
                // First, handle the Ancient Desert biome
                MaterialRules.condition(
                    ANCIENT_DESERT_CONDITION,
                    MaterialRules.sequence(
                        // Top 4 blocks are based on noise
                        MaterialRules.condition(
                            MaterialRules.surface(),
                            // Choose between two materials based on noise
                            MaterialRules.sequence(
                                MaterialRules.condition(
                                    MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, 0.25),
                                    DRY_SILT // Dry silt in some areas
                                ),
                                block(Blocks.SAND) // Sand elsewhere
                            )
                        ),
                        // Under surface blocks (4-8 blocks down)
                        MaterialRules.condition(
                            MaterialRules.verticalGradient("bedrock_floor", YOffset.fixed(0), YOffset.fixed(8)),
                            SANDSTONE // Sandstone at lower levels
                        )
                    )
                ),
                
                // Then, handle the Nile River biome
                MaterialRules.condition(
                    NILE_RIVER_CONDITION,
                    MaterialRules.sequence(
                        // Water level and below (actual riverbed)
                        MaterialRules.condition(
                            MaterialRules.water(-1, 0),
                            MaterialRules.sequence(
                                // Underwater surface is always riverbed
                                MaterialRules.condition(
                                    MaterialRules.surface(),
                                    RIVERBED // All underwater areas are riverbed blocks
                                ),
                                // Deeper layers under riverbed are nile mud
                                MaterialRules.condition(
                                    MaterialRules.stoneDepth(1, true, VerticalSurfaceType.FLOOR),
                                    NILE_MUD
                                )
                            )
                        ),
                        
                        // Shoreline areas (blocks near water) - top layer
                        MaterialRules.condition(
                            MaterialRules.surface(),
                            MaterialRules.sequence(
                                // First row next to water: chance for lush Nile River Grass
                                MaterialRules.condition(
                                    MaterialRules.water(-1, 0),
                                    MaterialRules.sequence(
                                        MaterialRules.condition(
                                            MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, 0.35),
                                            NILE_RIVER_GRASS // 35% chance for lush grass
                                        ),
                                        NILE_RIVER_SAND  // 65% chance for river sand
                                    )
                                ),
                                // Second row from water: mix of river sand, thin grass, and fertile silt
                                MaterialRules.condition(
                                    MaterialRules.water(-2, 0),
                                    MaterialRules.sequence(
                                        MaterialRules.condition(
                                            MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, 0.65),
                                            NILE_RIVER_SAND // 35% chance for river sand
                                        ),
                                        MaterialRules.condition(
                                            MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, 0.4),
                                            NILE_RIVER_THIN_GRASS // 25% chance for thin grass
                                        ),
                                        FERTILE_SILT // 40% chance for fertile silt
                                    )
                                ),
                                // Third to fifth blocks from water: mostly fertile silt with some thin grass
                                MaterialRules.condition(
                                    MaterialRules.water(-5, 0),
                                    MaterialRules.sequence(
                                        MaterialRules.condition(
                                            MaterialRules.noiseThreshold(NoiseParametersKeys.SURFACE, 0.75),
                                            NILE_RIVER_THIN_GRASS // 25% chance for thin grass
                                        ),
                                        FERTILE_SILT // 75% chance for fertile silt
                                    )
                                ),
                                // Even further is dry silt
                                DRY_SILT
                            )
                        ),
                        
                        // Underground layers for all land areas in Nile biome
                        MaterialRules.condition(
                            MaterialRules.stoneDepth(1, true, VerticalSurfaceType.FLOOR),
                            NILE_MUD
                        )
                    )
                ),
                
                // Default Minecraft surface rules as fallback
                MaterialRules.sequence(
                    MaterialRules.condition(
                        MaterialRules.surface(),
                        block(Blocks.SAND)
                    )
                )
        );
    }
    
    // Helper method to create block rules
    private static MaterialRule block(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
} 