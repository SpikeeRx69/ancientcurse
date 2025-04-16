package com.ancientcurse;

import com.ancientcurse.item.CustomAnimatedItem;
import com.ancientcurse.item.SekhemDateItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Centralizes item registration for the mod
 */
public class ModItems {
    // Define items
    public static final CustomAnimatedItem STAFF_OF_RA = new CustomAnimatedItem(new FabricItemSettings(), "staff_of_ra");
    
    // Define the Sycamore Fig food item
    public static final Item SYCAMORE_FIG = new Item(
        new FabricItemSettings()
            .food(new FoodComponent.Builder()
                .hunger(4)
                .saturationModifier(0.6f)
                .build()
            )
    );
    
    // Define the Golden Sycamore Fig food item with regeneration effect
    public static final Item GOLDEN_SYCAMORE_FIG = new Item(
        new FabricItemSettings()
            .food(new FoodComponent.Builder()
                .hunger(6)
                .saturationModifier(0.8f)
                .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 0), 1.0f) // 3 seconds of Regeneration I
                .build()
            )
    );
    
    // Define the Sekhem Date food item with fire resistance effect
    public static final Item SEKHEM_DATE = new SekhemDateItem(new FabricItemSettings());
    
    // Define crop seeds
    public static final Item FLAX_SEEDS = new AliasedBlockItem(ModBlocks.FLAX, new FabricItemSettings());
    public static final Item BARLEY_SEEDS = new AliasedBlockItem(ModBlocks.BARLEY, new FabricItemSettings());

    // Define harvested crop items
    public static final Item BARLEY = new Item(new FabricItemSettings());
    public static final Item FLAX_FIBER = new Item(new FabricItemSettings());
    
    // Define Lotus Flower item
    public static final Item LOTUS_FLOWER = new Item(
        new FabricItemSettings()
            .food(new FoodComponent.Builder()
                .hunger(2)
                .saturationModifier(0.3f)
                .statusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 200, 0), 1.0f) // 10 seconds of Water Breathing
                .build()
            )
    );
    
    // Define Raw Riverbed Clay item - drops from Riverbed Clay block
    public static final Item RAW_RIVERBED_CLAY = new Item(new FabricItemSettings());
    
    // Define Papyrus Paper item - crafted from Papyrus Reed
    public static final Item PAPYRUS_PAPER = new Item(new FabricItemSettings());
    
    // Define Spinach food item - drops from Egyptian Spinach plant
    public static final Item SPINACH = new Item(
        new FabricItemSettings()
            .food(new FoodComponent.Builder()
                .hunger(3)
                .saturationModifier(0.4f)
                .statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 100, 0), 0.7f) // 5 seconds of Night Vision with 70% chance
                .build()
            )
    );
    
    // Define Rope item - crafting material
    public static final Item ROPE = new Item(new FabricItemSettings());

    /**
     * Registers all mod items
     */
    public static void registerItems() {
        AncientCurse.LOGGER.info("Registering items for " + AncientCurse.MOD_ID);
        
        // Register the Staff of Ra
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "staff_of_ra"),
            STAFF_OF_RA
        );
        
        // Register the Sycamore Fig
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "sycamore_fig"),
            SYCAMORE_FIG
        );
        
        // Register the Golden Sycamore Fig
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "golden_sycamore_fig"),
            GOLDEN_SYCAMORE_FIG
        );
        
        // Register the Sekhem Date
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "sekhem_date"),
            SEKHEM_DATE
        );
        
        // Register crop seeds
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "flax_seeds"),
            FLAX_SEEDS
        );
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "barley_seeds"),
            BARLEY_SEEDS
        );
        
        // Register harvested crops
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "barley"),
            BARLEY
        );
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "flax_fiber"),
            FLAX_FIBER
        );
        
        // Register Lotus Flower
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "lotus_flower"),
            LOTUS_FLOWER
        );
        
        // Register Raw Riverbed Clay
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "raw_riverbed_clay"),
            RAW_RIVERBED_CLAY
        );
        
        // Register Papyrus Paper
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "papyrus_paper"),
            PAPYRUS_PAPER
        );
        
        // Register Spinach
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "spinach"),
            SPINACH
        );
        
        // Register Rope
        Registry.register(
            Registries.ITEM,
            new Identifier(AncientCurse.MOD_ID, "rope"),
            ROPE
        );
    }
}