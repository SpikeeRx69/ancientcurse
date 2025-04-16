package com.ancientcurse.world.gen;

import com.ancientcurse.AncientCurse;
import com.ancientcurse.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

public class ModConfiguredFeatures {

    // Define RegistryKeys for features
    public static final RegistryKey<ConfiguredFeature<?, ?>> ALGAE_KEY = registerKey("algae_patch"); // Changed name for clarity
    public static final RegistryKey<ConfiguredFeature<?, ?>> PAPYRUS_KEY = registerKey("papyrus_column"); // Changed name for clarity

    // Bootstrap method is now mainly for ensuring keys exist during datagen build
    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        AncientCurse.LOGGER.info("Bootstrapping Configured Features (loaded from JSON)");
        // Actual configuration is in the JSON files
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(AncientCurse.MOD_ID, name));
    }

    // Register method no longer strictly needed if only using JSON
    // private static <FC extends FeatureConfig, F extends Feature<FC>> void register(...) { ... }
} 