package com.ancientcurse;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;
import com.ancientcurse.world.AncientWorldGeneration;
import com.ancientcurse.world.ModWorldPresets;
import com.ancientcurse.world.biome.BiomeModifier;
import com.ancientcurse.world.biome.ModBiomes;
import com.ancientcurse.world.gen.ModSurfaceRuleRegistration;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.WorldPreset;
import com.ancientcurse.world.dimension.ModDimensions;

/**
 * Main mod class for Ancient Curse.
 */
public class AncientCurse implements ModInitializer {
    public static final String MOD_ID = "ancientcurse";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Define our world preset key
    public static final RegistryKey<WorldPreset> ANCIENT_CURSE_PRESET = RegistryKey.of(
        RegistryKeys.WORLD_PRESET, new Identifier(MOD_ID, "ancient_curse")
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Ancient Curse Mod");
        
        // Initialize GeckoLib
        GeckoLib.initialize();
        
        // Register content
        registerContent();
        
        // Register worldgen components
        registerWorldgenComponents();
        
        LOGGER.info("Ancient Curse Mod fully initialized");
    }
    
    /**
     * Registers all mod content (blocks, items, etc.)
     */
    private void registerContent() {
        // Register mod item groups
        ModItemGroup.registerItemGroups();
        
        // Register mod blocks
        ModBlocks.registerBlocks();
        
        // Register mod items
        ModItems.registerItems();
        
        // Register mod block entities
        ModBlockEntities.registerBlockEntities();
        
        // Register mod screen handlers
        ModScreenHandlers.registerScreenHandlers();
        
        // Register mod entities
        ModEntities.registerEntities();
        
        // Register mod commands
        ModCommands.registerCommands();
        
        // Register structures
        ModStructures.registerStructures();
    }
    
    /**
     * Registers all world generation components
     */
    private void registerWorldgenComponents() {
        // Register biomes first
        ModBiomes.registerBiomes();
        
        // Register dimension types and dimensions
        ModDimensions.register();
        
        // Register world presets
        ModWorldPresets.register();
        
        // Register additional worldgen components after server starts
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            // Register custom surface rules
            ModSurfaceRuleRegistration.register();
            
            // Register biome modifiers to ensure custom blocks are used
            BiomeModifier.register();
            
            // Initialize world generation with compatibility handling
            AncientWorldGeneration.init();
        });
    }
}