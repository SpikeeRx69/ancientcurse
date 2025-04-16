package com.ancientcurse.world;

import com.ancientcurse.AncientCurse;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides compatibility for handling feature types that have changed format
 * between Minecraft versions, particularly disk and simple_block features.
 */
public class FeatureCompatibility {
    
    // Map of problematic features to vanilla replacements
    private static final Map<String, String> FEATURE_REPLACEMENTS = new HashMap<>();
    
    static {
        // Initialize with fallback replacements for disk features
        FEATURE_REPLACEMENTS.put("minecraft:disk", "minecraft:disk_sand");
        FEATURE_REPLACEMENTS.put("ancientcurse:disk_desert_sand", "minecraft:disk_sand");
        FEATURE_REPLACEMENTS.put("ancientcurse:disk_desert_sandstone", "minecraft:disk_sand");
        FEATURE_REPLACEMENTS.put("ancientcurse:disk_riverbed", "minecraft:disk_clay");
        FEATURE_REPLACEMENTS.put("ancientcurse:disk_riverbed_clay", "minecraft:disk_clay");
        FEATURE_REPLACEMENTS.put("ancientcurse:disk_nile_mud", "minecraft:disk_clay");
        
        // Replacements for simple_block features
        FEATURE_REPLACEMENTS.put("minecraft:simple_block", "minecraft:patch_dead_bush");
        FEATURE_REPLACEMENTS.put("ancientcurse:algae", "minecraft:patch_waterlily");
        FEATURE_REPLACEMENTS.put("ancientcurse:algae_patch", "minecraft:patch_waterlily");
        FEATURE_REPLACEMENTS.put("ancientcurse:desert_ruins", "minecraft:basalt_pillar");
        FEATURE_REPLACEMENTS.put("ancientcurse:desert_ruins_new", "minecraft:basalt_pillar");
        FEATURE_REPLACEMENTS.put("ancientcurse:mirage_pool", "minecraft:disk_sand");
        FEATURE_REPLACEMENTS.put("ancientcurse:volcanic_vent", "minecraft:disk_gravel");
    }
    
    /**
     * Safely gets a registry key for a placed feature, falling back to a compatible
     * replacement if the requested feature is problematic.
     *
     * @param featureId The original feature identifier
     * @return A registry key for a safe feature to use
     */
    public static RegistryKey<PlacedFeature> getSafePlacedFeatureKey(String featureId) {
        // If the feature is known to be problematic, use a replacement
        if (FEATURE_REPLACEMENTS.containsKey(featureId)) {
            String replacement = FEATURE_REPLACEMENTS.get(featureId);
            AncientCurse.LOGGER.info("Replacing problematic feature " + featureId + " with " + replacement);
            return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(replacement));
        }
        
        // Otherwise use the original
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(featureId));
    }
    
    /**
     * Checks if a feature ID is known to be problematic
     */
    public static boolean isProblematicFeature(String featureId) {
        // Check if this is a disk or simple_block feature
        return featureId.contains("disk") || 
               featureId.contains("simple_block") ||
               FEATURE_REPLACEMENTS.containsKey(featureId);
    }
} 