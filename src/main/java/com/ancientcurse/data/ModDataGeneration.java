package com.ancientcurse.data;

import com.ancientcurse.AncientCurse;
import com.ancientcurse.world.biome.ModBiomes;
import com.ancientcurse.world.gen.ModConfiguredFeatures;
import com.ancientcurse.world.gen.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.concurrent.CompletableFuture;

/**
 * Handles data generation for the Ancient Curse mod.
 */
public class ModDataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        AncientCurse.LOGGER.info("Initializing data generation for Ancient Curse mod");
        
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModWorldGenProvider::new);
    }

    // Build dynamic registries
    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        AncientCurse.LOGGER.info("Building dynamic registries for " + AncientCurse.MOD_ID);
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
        // Add Biome registry builder
        registryBuilder.addRegistry(RegistryKeys.BIOME, ModWorldGenProvider::bootstrapBiomes); 
    }
}

/**
 * Combined provider for dynamic worldgen registries (Biomes, Features, etc.).
 */
class ModWorldGenProvider extends FabricDynamicRegistryProvider {
    public ModWorldGenProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        AncientCurse.LOGGER.info("Configuring dynamic registries entries for " + AncientCurse.MOD_ID);
        // Add entries for registries built via buildRegistry to ensure they are generated
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.BIOME));
    }

    // Static bootstrap method called by buildRegistry
    public static void bootstrapBiomes(Registerable<Biome> context) {
        AncientCurse.LOGGER.info("Bootstrapping Biomes for " + AncientCurse.MOD_ID + " (loaded from JSON)");
        // Biomes are defined in JSON files in data/ancientcurse/worldgen/biome/
        // This bootstrap method is just needed for the registry builder
    }

    @Override
    public String getName() {
        return "Ancient Curse World Generation";
    }
} 