# Minecraft 1.20.1 Custom Biome Creation Guide

This guide provides information on creating custom biomes in Minecraft 1.20.1 using Fabric mods. It covers key concepts, important file structures, and best practices.

## Core Files Needed

1. **Biome JSON** - `data/<namespace>/worldgen/biome/<biome_name>.json`
   - Defines visual appearance, mob spawning, and features

2. **Dimension Type** - `data/<namespace>/dimension_type/<dimension_name>.json`
   - Sets basic dimension properties (sky, respawn mechanics, etc.)

3. **Dimension** - `data/<namespace>/worldgen/dimension/<dimension_name>.json`
   - Links biomes to noise settings and controls overall generation

4. **Noise Settings** - `data/<namespace>/worldgen/noise_settings/<settings_name>.json`
   - Controls terrain shape, cave generation, and surface rules

## Key Components in a Biome JSON

```json
{
  "temperature": 0.9,              // 0.0-2.0 value - affects visuals and mechanics
  "downfall": 0.5,                 // 0.0-1.0 value - affects grass color and rain
  "has_precipitation": true,       // Whether rain/snow can occur
  
  "effects": {                     // Visual effects
    "sky_color": 7254527,          // Sky color (decimal RGB value)
    "fog_color": 12638463,         // Fog color
    "water_color": 4445678,        // Water color
    "water_fog_color": 270131,     // Underwater fog color
    "grass_color": 11985483,       // Optional override for grass color
    "foliage_color": 10387789,     // Optional override for leaves color
    "ambient_sound": { ... },      // Background sounds
    "mood_sound": { ... },         // Cave sounds
    "additions_sound": { ... },    // Random additional sounds
    "music": { ... }               // Background music
  },

  "spawners": {                    // Mob spawning
    "creature": [ ... ],           // Passive mobs
    "monster": [ ... ],            // Hostile mobs
    "water_creature": [ ... ],     // Water mobs
    // Other categories...
  },

  "carvers": {                    // Cave generation
    "air": [ ... ]                // Cave types
  },

  "features": [                   // Features in generation phases
    [],                           // Phase 0 - Raw generation
    [],                           // Phase 1 - Lakes
    [],                           // Phase 2 - Local modifications
    [],                           // Phase 3 - Underground structures
    [],                           // Phase 4 - Surface structures
    [],                           // Phase 5 - Underground ores
    [],                           // Phase 6 - Underground ores
    [],                           // Phase 7 - Underground ores
    [],                           // Phase 8 - Underground decorations
    [],                           // Phase 9 - Fluids
    [],                           // Phase 10 - Surface decorations
    []                            // Phase 11 - Top layer modification
  ]
}
```

## Common Issues and Solutions

1. **Missing Features**: Make sure both the configured_feature and placed_feature files exist for any custom features you reference
   - Configured feature defines WHAT generates
   - Placed feature defines WHERE it generates

2. **Invalid JSON Structure**: Different versions of Minecraft use different formats
   - 1.20.1 disk features require `target` and `state` fields
   - Older formats used `targets` (array) and `state_provider`

3. **Non-existent Feature References**: Only reference vanilla or custom features that actually exist
   - Some vanilla features like `minecraft:tall_grass` don't exist as placed features
   - Use `minecraft:patch_grass` or other valid alternatives

## Debugging Tips

1. **Check logs for specific errors**
   - "No key target" usually means wrong JSON structure
   - "Unbound values in registry" means a referenced feature doesn't exist

2. **Use vanilla references**
   - Extract vanilla datapack files and compare your files to them
   - Misode.github.io has great visualizations of vanilla worldgen

3. **Test incrementally**
   - Start with minimal configuration (just terrain)
   - Add features one by one, testing after each addition

## Registering a Custom Biome in Code

```java
// Register the biome
private static RegistryKey<Biome> NILE_RIVER = RegistryKey.of(
    RegistryKeys.BIOME, new Identifier("ancientcurse", "nile_river"));

// Add the biome to the dimension
// This is done in your mod initializer
public static void registerBiomes() {
    BiomeModifications.addFeature(
        BiomeSelectors.includeByKey(NILE_RIVER),
        GenerationStep.Feature.VEGETAL_DECORATION,
        RegistryKey.of(RegistryKeys.PLACED_FEATURE, 
            new Identifier("ancientcurse", "patch_papyrus_reed")));
}
```

## Feature Generation Order

1. **Phase 0-2**: Basic terrain and caves
2. **Phase 3-4**: Structures
3. **Phase 5-7**: Ores and mineral deposits
4. **Phase 8**: Underground decorations
5. **Phase 9**: Fluid springs
6. **Phase 10**: Surface vegetation, trees
7. **Phase 11**: Final touches (like snow)

## Best Practices

1. **Use consistent IDs**: Keep namespaces consistent between all your files
2. **Match vanilla patterns**: Study vanilla implementations for patterns
3. **Test regularly**: Test changes incrementally rather than all at once
4. **Create feature pairs**: Always create both configured_feature and placed_feature files
5. **Keep structured folders**: Organize your resources neatly

## Resources

- Minecraft Wiki Custom Worldgen: https://minecraft.fandom.com/wiki/Custom_world_generation
- Fabric Wiki: https://fabricmc.net/wiki/tutorial:biomes
- Misode's Generators: https://misode.github.io/

---

This guide was created for the Ancient Curse mod. If you find any issues or have suggestions, please let me know! 