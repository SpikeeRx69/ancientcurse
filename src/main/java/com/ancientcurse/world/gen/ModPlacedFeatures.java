package com.ancientcurse.world.gen;

import com.ancientcurse.AncientCurse;
import com.ancientcurse.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

public class ModPlacedFeatures {

    public static final RegistryKey<PlacedFeature> ALGAE_PLACED_KEY = registerKey("algae_placed");
    public static final RegistryKey<PlacedFeature> PAPYRUS_PLACED_KEY = registerKey("papyrus_placed");

    // Bootstrap method is now mainly for ensuring keys exist during datagen build
    public static void bootstrap(Registerable<PlacedFeature> context) {
        AncientCurse.LOGGER.info("Bootstrapping Placed Features (loaded from JSON)");
        // Actual placement is in the JSON files
    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(AncientCurse.MOD_ID, name));
    }

    // Register methods no longer strictly needed if only using JSON
    // private static void register(...) { ... }
    // private static void register(...) { ... }
} 