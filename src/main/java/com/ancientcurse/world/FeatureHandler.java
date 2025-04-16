package com.ancientcurse.world;

import com.ancientcurse.AncientCurse;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.PlacedFeature;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles feature compatibility between Minecraft versions.
 * This class provides safe alternatives for features that have changed or been removed.
 */
public class FeatureHandler {
    
    // Map of problematic feature types to their safe replacement alternatives
    private static final Map<String, String> FEATURE_REPLACEMENTS = new HashMap<>();
    
    static {
        // Initialize with replacements for problematic feature types
        FEATURE_REPLACEMENTS.put("disk", "disk_sand");
        FEATURE_REPLACEMENTS.put("simple_block", "patch_dead_bush");
    }
    
    /**
     * Initializes the feature handler by logging information about replaced features.
     */
    public static void init() {
        AncientCurse.LOGGER.info("Initializing feature compatibility layer");
        AncientCurse.LOGGER.info("Prepared replacements for {} problematic feature types", FEATURE_REPLACEMENTS.size());
    }
    
    /**
     * Gets a safe feature key to use in place of potentially problematic ones.
     * 
     * @param featureId The original feature ID
     * @return A registry key to a safe equivalent feature
     */
    public static RegistryKey<PlacedFeature> getSafePlacedFeatureKey(String featureId) {
        // Extract the feature type and namespace
        String namespace = "minecraft";
        String featureName = featureId;
        
        if (featureId.contains(":")) {
            String[] parts = featureId.split(":", 2);
            namespace = parts[0];
            featureName = parts[1];
        }
        
        // Check if the feature type is problematic
        for (Map.Entry<String, String> entry : FEATURE_REPLACEMENTS.entrySet()) {
            if (featureName.contains(entry.getKey())) {
                // Replace with safe alternative
                return RegistryKey.of(RegistryKeys.PLACED_FEATURE, 
                    new Identifier("minecraft", entry.getValue()));
            }
        }
        
        // Not problematic, use as is
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(featureId));
    }
} 