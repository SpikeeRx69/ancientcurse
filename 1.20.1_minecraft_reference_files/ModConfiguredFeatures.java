package com.ancientcurse.world.feature;

import com.ancientcurse.AncientCurse;
import com.ancientcurse.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;

import java.util.List;

public class ModConfiguredFeatures {
    // Registry Keys for our configured features
    public static final RegistryKey<ConfiguredFeature<?, ?>> PAPYRUS_REED_KEY = registerKey("papyrus_reed");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PALM_TREE_KEY = registerKey("palm_tree");
    public static final RegistryKey<ConfiguredFeature<?, ?>> LAPIS_LAZULI_ORE_KEY = registerKey("lapis_lazuli_ore");

    // Helper method to create registry keys
    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(AncientCurse.MOD_ID, name));
    }

    // Method to bootstrap all configured features
    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        // Register papyrus reed feature
        register(context, PAPYRUS_REED_KEY, Feature.SIMPLE_BLOCK,
                new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.PAPYRUS_REED)));

        // Register palm tree feature
        register(context, PALM_TREE_KEY, Feature.TREE,
                new TreeFeatureConfig.Builder(
                        BlockStateProvider.of(ModBlocks.PALM_LOG),
                        new StraightTrunkPlacer(5, 2, 1),
                        BlockStateProvider.of(ModBlocks.PALM_LEAVES),
                        new BlobFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(0), 3),
                        new TwoLayersFeatureSize(1, 0, 1)
                ).build());

        // Register lapis lazuli ore feature
        register(context, LAPIS_LAZULI_ORE_KEY, Feature.ORE,
                new OreFeatureConfig(
                        List.of(OreFeatureConfig.createTarget(
                                OreConfiguredFeatures.STONE_ORE_REPLACEABLES,
                                ModBlocks.LAPIS_LAZULI_ORE.getDefaultState())),
                        6)); // Vein size
    }

    // Helper method to register a configured feature
    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(
            Registerable<ConfiguredFeature<?, ?>> context,
            RegistryKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
} 