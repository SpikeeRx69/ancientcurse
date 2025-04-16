package com.ancientcurse.world.feature;

import com.ancientcurse.AncientCurse;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class ModPlacedFeatures {
    // Registry Keys for our placed features
    public static final RegistryKey<PlacedFeature> PAPYRUS_REED_PLACED_KEY = registerKey("papyrus_reed_placed");
    public static final RegistryKey<PlacedFeature> PALM_TREE_PLACED_KEY = registerKey("palm_tree_placed");
    public static final RegistryKey<PlacedFeature> LAPIS_LAZULI_ORE_PLACED_KEY = registerKey("lapis_lazuli_ore_placed");

    // Helper method to create registry keys
    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AncientCurse.MOD_ID, name));
    }

    // Method to bootstrap all placed features
    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        // Get our configured features from registry
        var papyrusReedConfigured = configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.PAPYRUS_REED_KEY);
        var palmTreeConfigured = configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.PALM_TREE_KEY);
        var lapisLazuliOreConfigured = configuredFeatureRegistryEntryLookup.getOrThrow(ModConfiguredFeatures.LAPIS_LAZULI_ORE_KEY);

        // Register papyrus reed placed feature
        register(context, PAPYRUS_REED_PLACED_KEY, papyrusReedConfigured,
                // Place reeds near water, with appropriate count and spacing
                RarityFilterPlacementModifier.of(4), // 1/4 chance per chunk
                SquarePlacementModifier.of(), // Spread out in a square
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, // Only on solid blocks
                BiomePlacementModifier.of()); // Respect biome settings

        // Register palm tree placed feature
        register(context, PALM_TREE_PLACED_KEY, palmTreeConfigured,
                // Place trees with appropriate spacing and count
                CountPlacementModifier.of(2), // 2 per chunk
                SquarePlacementModifier.of(), // Spread out in a square
                PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP, // Only on ocean floor or ground
                BiomePlacementModifier.of()); // Respect biome settings

        // Register lapis lazuli ore placed feature
        register(context, LAPIS_LAZULI_ORE_PLACED_KEY, lapisLazuliOreConfigured,
                // Place ores with height restrictions and frequency
                CountPlacementModifier.of(6), // 6 veins per chunk
                SquarePlacementModifier.of(), // Spread out in a square
                HeightRangePlacementModifier.trapezoid(
                        net.minecraft.world.gen.YOffset.fixed(0), 
                        net.minecraft.world.gen.YOffset.fixed(60)), // Between y=0 and y=60
                BiomePlacementModifier.of()); // Respect biome settings
    }

    // Helper method to register a placed feature
    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                               RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                               PlacementModifier... placementModifiers) {
        context.register(key, new PlacedFeature(configuration, List.of(placementModifiers)));
    }
} 