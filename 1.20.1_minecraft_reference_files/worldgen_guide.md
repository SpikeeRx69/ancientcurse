# Minecraft 1.20.1 Worldgen Guide for Fabric Mods

This guide explains the key components of Minecraft's worldgen system in 1.20.1 for Fabric modders.

## Directory Structure

```
resources/data/[namespace]/worldgen/
├── biome/                 # Biome definitions
├── configured_feature/    # Feature definitions
├── placed_feature/        # Feature placement rules
├── noise_settings/        # Terrain generation settings
├── density_function/      # Custom terrain shaping functions
├── noise/                 # Custom noise configurations
├── surface_rule/          # Surface block placement rules
└── dimension_type/        # Dimension behavior settings
```

## Noise Settings (noise_settings/)

Noise settings control the base terrain shape. Key components:

```json
{
  "sea_level": 63,
  "disable_mob_generation": false,
  "aquifers_enabled": true,
  "ore_veins_enabled": true,
  "legacy_random_source": false,
  "default_block": {
    "Name": "minecraft:stone"
  },
  "default_fluid": {
    "Name": "minecraft:water",
    "Properties": {
      "level": "0"
    }
  },
  "noise": {
    "min_y": -64,
    "height": 384,
    "size_horizontal": 1,
    "size_vertical": 2
  },
  "noise_router": {
    "barrier": 0.0,
    "fluid_level_floodedness": 0.0,
    "fluid_level_spread": 0.0,
    "lava": 0.0,
    "temperature": 0.0,
    "vegetation": 0.0,
    "continents": 0.0,
    "erosion": 0.0,
    "depth": 0.0,
    "ridges": 0.0,
    "initial_density_without_jaggedness": {
      "type": "minecraft:add",
      "argument1": -0.01,
      "argument2": {
        "type": "minecraft:mul",
        "argument1": {
          "type": "minecraft:y_clamped_gradient",
          "from_y": -64,
          "to_y": 320,
          "from_value": 1.0,
          "to_value": -1.0
        },
        "argument2": {
          "type": "minecraft:add",
          "argument1": 1.0,
          "argument2": {
            "type": "minecraft:mul",
            "argument1": -0.5,
            "argument2": {
              "type": "minecraft:noise",
              "noise": "minecraft:cave_entrance",
              "xz_scale": 0.75,
              "y_scale": 1.0
            }
          }
        }
      }
    },
    "final_density": {
      "type": "minecraft:min",
      "argument1": {
        "type": "minecraft:squeeze",
        "argument": {
          "type": "minecraft:mul",
          "argument1": 0.64,
          "argument2": {
            "type": "minecraft:interpolated",
            "argument": {
              "type": "minecraft:blend_density",
              "argument": {
                "type": "minecraft:add",
                "argument1": -0.13,
                "argument2": "minecraft:overworld/base_3d_noise"
              }
            }
          }
        }
      },
      "argument2": "minecraft:overworld/caves/entrances"
    }
  },
  "surface_rule": {
    "type": "minecraft:sequence",
    "sequence": [
      // Surface rules define what blocks appear on the surface and underground
    ]
  }
}
```

For flatter terrain, reduce or set to 0 the continents, erosion, depth, and ridges values.

## Biome Configuration (biome/)

Biomes control climate, mobs, features, and visual effects:

```json
{
  "temperature": 0.8,
  "downfall": 0.4,
  "precipitation": "rain",
  "temperature_modifier": "none",
  "effects": {
    "sky_color": 7907327,
    "fog_color": 12638463,
    "water_color": 4159204,
    "water_fog_color": 329011,
    "grass_color": 9470285,
    "foliage_color": 7254795,
    "mood_sound": {
      "sound": "minecraft:ambient.cave",
      "tick_delay": 6000,
      "block_search_extent": 8,
      "offset": 2.0
    },
    "music": {
      "sound": "minecraft:music.overworld.jungle",
      "min_delay": 12000,
      "max_delay": 24000,
      "replace_current_music": false
    }
  },
  "spawners": {
    "monster": [
      // Monster spawner definitions
    ],
    "creature": [
      // Animal spawner definitions
    ],
    "ambient": [],
    "underground_water_creature": [],
    "water_creature": [],
    "water_ambient": [],
    "misc": []
  },
  "spawn_costs": {},
  "carvers": {
    "air": [
      "minecraft:cave",
      "minecraft:cave_extra_underground",
      "minecraft:canyon"
    ]
  },
  "features": [
    // Each array represents a feature generation step
    [],                          // RAW_GENERATION
    [],                          // LAKES
    [],                          // LOCAL_MODIFICATIONS
    [],                          // UNDERGROUND_STRUCTURES
    [],                          // SURFACE_STRUCTURES
    [],                          // STRONGHOLDS
    [                            // UNDERGROUND_ORES 
      "minecraft:ore_dirt",
      "minecraft:ore_gravel"
    ],
    [],                          // UNDERGROUND_DECORATION
    [],                          // FLUID_SPRINGS
    [                            // VEGETAL_DECORATION
      "minecraft:patch_grass_savanna",
      "minecraft:trees_savanna"
    ],
    []                           // TOP_LAYER_MODIFICATION
  ]
}
```

## Feature Configuration

### Configured Features (configured_feature/)

These define what a feature is:

```json
{
  "type": "minecraft:random_patch",
  "config": {
    "feature": {
      "type": "minecraft:simple_block",
      "config": {
        "to_place": {
          "type": "minecraft:simple_state_provider",
          "state": {
            "Name": "minecraft:sugar_cane",
            "Properties": {
              "age": "0"
            }
          }
        }
      }
    },
    "tries": 20,
    "xz_spread": 4,
    "y_spread": 0
  }
}
```

### Placed Features (placed_feature/)

These define where a feature is placed:

```json
{
  "feature": "minecraft:patch_sugar_cane",
  "placement": [
    {
      "type": "minecraft:count",
      "count": 10
    },
    {
      "type": "minecraft:in_square"
    },
    {
      "type": "minecraft:heightmap",
      "heightmap": "WORLD_SURFACE_WG"
    },
    {
      "type": "minecraft:biome"
    }
  ]
}
```

## Common Placement Types

- `minecraft:count`: Controls how many attempts to place the feature
- `minecraft:in_square`: Distributes placement attempts in a square
- `minecraft:heightmap`: Places feature at specific heights (WORLD_SURFACE_WG, OCEAN_FLOOR_WG)
- `minecraft:height_range`: Sets vertical bounds for feature placement
- `minecraft:surface_water_depth_filter`: Controls placement in shallow water
- `minecraft:block_predicate_filter`: Tests blocks before placement
- `minecraft:biome`: Restricts to specific biomes

## Surface Rules

Surface rules determine what blocks appear on the terrain surface:

```json
{
  "type": "minecraft:sequence",
  "sequence": [
    {
      "type": "minecraft:condition",
      "if_true": {
        "type": "minecraft:biome",
        "biome_is": [
          "mymod:my_biome"
        ]
      },
      "then_run": {
        "type": "minecraft:condition",
        "if_true": {
          "type": "minecraft:above_preliminary_surface"
        },
        "then_run": {
          "type": "minecraft:sequence",
          "sequence": [
            {
              "type": "minecraft:condition",
              "if_true": {
                "type": "minecraft:stone_depth",
                "surface_type": "floor",
                "add_surface_depth": false,
                "secondary_depth_range": 0,
                "offset": 0
              },
              "then_run": {
                "type": "minecraft:block",
                "result_state": {
                  "Name": "mymod:custom_sand"
                }
              }
            }
          ]
        }
      }
    }
  ]
}
```

## Dimension Types (dimension_type/)

Control broader dimension behavior:

```json
{
  "ultrawarm": false,
  "natural": true,
  "piglin_safe": false,
  "respawn_anchor_works": false,
  "bed_works": true,
  "has_raids": true,
  "has_skylight": true,
  "has_ceiling": false,
  "coordinate_scale": 1.0,
  "ambient_light": 0.0,
  "fixed_time": false,
  "logical_height": 384,
  "effects": "minecraft:overworld",
  "infiniburn": "#minecraft:infiniburn_overworld",
  "min_y": -64,
  "height": 384,
  "monster_spawn_light_level": {
    "type": "minecraft:uniform",
    "value": {
      "min_inclusive": 0,
      "max_inclusive": 7
    }
  },
  "monster_spawn_block_light_limit": 0
}
```

## Java Registration (For Fabric)

If you're using Java code with Fabric:

```java
// Register Biomes
RegistryKey<Biome> NILE_RIVER = RegistryKey.of(Registry.BIOME_KEY, new Identifier("ancientcurse", "nile_river"));

// Register Dimension Types
RegistryKey<DimensionType> ANCIENT_CURSE_TYPE = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, new Identifier("ancientcurse", "ancient_curse"));

// Register Features
Registry.register(Registry.FEATURE, new Identifier("ancientcurse", "papyrus_reed"), new PapyrusReedFeature(DefaultFeatureConfig.CODEC));

// Configure and Place Features in Code (if preferred over JSON)
PlacedFeature PLACED_PAPYRUS_REED = new PlacedFeature(
    ConfiguredFeatures.register("ancientcurse:patch_papyrus_reed", 
        Feature.RANDOM_PATCH, 
        new RandomPatchFeatureConfig(24, 7, 3, 
            PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, 
                new SimpleBlockFeatureConfig(BlockStateProvider.of(ModBlocks.PAPYRUS_REED))))),
    List.of(
        CountPlacement.of(UniformInt.of(5, 15)),
        InSquarePlacement.spread(),
        PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
        SurfaceWaterDepthFilter.of(3),
        BiomePlacement.of()
    )
);

// Register Placed Feature
Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier("ancientcurse", "placed_papyrus_reed"), PLACED_PAPYRUS_REED);
```

## Common Issues and Solutions

1. **Missing Features**: Ensure both configured and placed features are registered and added to biome feature lists.

2. **Feature Placement Issues**: Check heightmaps and placement rules; for water plants, use `OCEAN_FLOOR_WG` heightmap.

3. **Biome Border Blending**: Use proper noise functions with appropriate scaling to create smooth transitions.

4. **Flat Terrain Generation**: Set continents, erosion, depth, and ridges values to 0.0 or close to 0.0.

5. **Registration Errors**: Ensure all dimension types, configured features, and placed features are properly registered.

6. **Invalid JSON Format**: Verify all JSON files against the correct format for 1.20.1.

7. **Performance Issues**: Limit the number of features and simplify noise functions for better performance.

## References

- [Minecraft Wiki on Biomes](https://minecraft.fandom.com/wiki/Biome)
- [Fabric Wiki](https://fabricmc.net/wiki/tutorial:biome)
- [JSON Schema for 1.20.1](https://misode.github.io/worldgen/) 