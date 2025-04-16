package com.ancientcurse.world;

import com.ancientcurse.AncientCurse;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;

public class AncientDesertBiome {
    public static final RegistryKey<Biome> ANCIENT_DESERT_KEY = RegistryKey.of(
            RegistryKeys.BIOME,
            new Identifier(AncientCurse.MOD_ID, "ancient_desert")
    );

    /**
     * Creates the Ancient Desert biome programmatically.
     * Note: In Minecraft 1.20.1, biomes are primarily defined via JSON, but this method
     * is kept for reference and for potential programmatic biome creation.
     */
    public static Biome createAncientDesertBiome() {
        AncientCurse.LOGGER.info("Creating Ancient Desert biome programmatically");
        
        // Create spawn settings
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        
        // Add mob spawns
        spawnSettings.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.CAMEL, 5, 2, 6));
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.HUSK, 80, 4, 4));
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SKELETON, 20, 4, 4));
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SPIDER, 100, 4, 4));
        spawnSettings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ZOMBIE, 19, 4, 4));
        
        // Add spawn costs
        spawnSettings.spawnCost(EntityType.HUSK, 0.15D, 0.15D);
        spawnSettings.spawnCost(EntityType.SKELETON, 0.1D, 0.15D);
        spawnSettings.spawnCost(EntityType.SPIDER, 0.1D, 0.15D);
        spawnSettings.spawnCost(EntityType.ZOMBIE, 0.1D, 0.15D);
        spawnSettings.spawnCost(EntityType.CAMEL, 0.1D, 0.02D);
        
        // Create generation settings
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        
        AncientCurse.LOGGER.info("Creating Ancient Desert biome with sand surface");
        
        // Build and return the biome
        return new Biome.Builder()
                .precipitation(false)
                .temperature(2.0F)
                .downfall(0.0F)
                .effects(new BiomeEffects.Builder()
                        .waterColor(4445678)
                        .waterFogColor(270131)
                        .fogColor(12638463)
                        .skyColor(7254527)
                        .moodSound(BiomeMoodSound.CAVE)
                        .build())
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }
    
    /**
     * Registers biome modifications for the Ancient Desert biome.
     * This is called during mod initialization.
     */
    public static void registerBiomeModifications() {
        AncientCurse.LOGGER.info("Registering Ancient Desert biome modifications");
        // The actual modifications are now handled in ModWorldGen.registerBiomeModifications()
    }
}