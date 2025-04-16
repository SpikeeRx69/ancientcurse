package com.ancientcurse;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static final ItemGroup ANCIENT_CURSE = Registry.register(
        Registries.ITEM_GROUP,
        new Identifier(AncientCurse.MOD_ID, "main"),
        FabricItemGroup.builder()
            .displayName(Text.translatable("itemgroup.ancientcurse"))
            .icon(() -> new ItemStack(ModItems.STAFF_OF_RA))
            .entries((displayContext, entries) -> {
                // Tools and Items
                entries.add(new ItemStack(ModItems.STAFF_OF_RA));
                
                // Food items
                entries.add(new ItemStack(ModItems.SYCAMORE_FIG));
                entries.add(new ItemStack(ModItems.GOLDEN_SYCAMORE_FIG));
                entries.add(new ItemStack(ModItems.SEKHEM_DATE));
                entries.add(new ItemStack(ModItems.SPINACH));
                
                // Seeds are automatically added by AliasedBlockItem
                // Do not add them again here to avoid duplicates
                
                // Harvested Crops / Fibers
                entries.add(new ItemStack(ModItems.BARLEY));
                entries.add(new ItemStack(ModItems.FLAX_FIBER));
                entries.add(new ItemStack(ModItems.PAPYRUS_PAPER));
                entries.add(new ItemStack(ModItems.RAW_RIVERBED_CLAY));
                entries.add(new ItemStack(ModItems.LOTUS_FLOWER));
                entries.add(new ItemStack(ModItems.ROPE));
                
                // Tree blocks
                entries.add(new ItemStack(ModBlocks.SYCAMORE_FIG_LOG));
                entries.add(new ItemStack(ModBlocks.SYCAMORE_LEAVES));
                entries.add(new ItemStack(ModBlocks.DATE_PALM_LOG));
                entries.add(new ItemStack(ModBlocks.DATE_PALM_LEAVES));
                
                // Terrain blocks
                entries.add(new ItemStack(ModBlocks.SMOOTH_SAND));
                entries.add(new ItemStack(ModBlocks.NILE_RIVER_SAND));
                entries.add(new ItemStack(ModBlocks.FERTILE_NILE_SILT));
                entries.add(new ItemStack(ModBlocks.DRY_NILE_SILT));
                entries.add(new ItemStack(ModBlocks.TILLED_NILE_SILT));
                entries.add(new ItemStack(ModBlocks.ARID_NILE_TURF));
                
                // Nile environment blocks
                entries.add(new ItemStack(ModBlocks.RIVERBED));
                entries.add(new ItemStack(ModBlocks.NILE_MUD));
                entries.add(new ItemStack(ModBlocks.GOLD_FLAKED_RIVER_BED));
                entries.add(new ItemStack(ModBlocks.MUD_FLAT));
                entries.add(new ItemStack(ModBlocks.SALT_BED));
                
                // Building materials
                entries.add(new ItemStack(ModBlocks.DRIED_REED_THATCH));
                entries.add(new ItemStack(ModBlocks.RIVERBED_CLAY));
                entries.add(new ItemStack(ModBlocks.OBELISK_STONE));
                entries.add(new ItemStack(ModBlocks.MUD_BRICK));
                entries.add(new ItemStack(ModBlocks.SUNBAKED_CLAY));
                entries.add(new ItemStack(ModBlocks.OFFERING_POT));
                
                // Plants and vegetation
                entries.add(new ItemStack(ModBlocks.LIGHT_NILE_MARSH));
                entries.add(new ItemStack(ModBlocks.HEAVY_MARSH));
                entries.add(new ItemStack(ModBlocks.RIVERBED_MOSS));
                entries.add(new ItemStack(ModBlocks.ALGAE));
                entries.add(new ItemStack(ModBlocks.REED_MAT));
                entries.add(new ItemStack(ModBlocks.SPOTTED_MARSH));
                entries.add(new ItemStack(ModBlocks.DEAD_PAPYRUS_REED));
                entries.add(new ItemStack(ModBlocks.PAPYRUS_REED));
                entries.add(new ItemStack(ModBlocks.DWARF_PAPYRUS));
                entries.add(new ItemStack(ModBlocks.EGYPTIAN_SPINACH));
                entries.add(new ItemStack(ModBlocks.EUPHORBIA_HELIOSCOPIA));
                entries.add(new ItemStack(ModBlocks.LIGHT_DEAD_FERN));
                entries.add(new ItemStack(ModBlocks.MINI_CACTUS));
                entries.add(new ItemStack(ModBlocks.PISTIA_STRATIOTES));
                entries.add(new ItemStack(ModBlocks.LOTUS_FLOWER_PAD));
                
                // Crops (Blocks themselves, for placing if needed)
                entries.add(new ItemStack(ModBlocks.FLAX));
                entries.add(new ItemStack(ModBlocks.BARLEY));
                
                // Nile River Grass blocks
                entries.add(new ItemStack(ModBlocks.NILE_RIVER_GRASS));
                entries.add(new ItemStack(ModBlocks.NILE_RIVER_THIN_GRASS));
                
                // Deshret blocks (Egyptian red desert materials)
                entries.add(new ItemStack(ModBlocks.DESHRET_SAND));
                entries.add(new ItemStack(ModBlocks.DESHRET_WAVY_SAND));
                entries.add(new ItemStack(ModBlocks.DESHRET_SANDSTONE));
                entries.add(new ItemStack(ModBlocks.DESHRET_BRICK));
                entries.add(new ItemStack(ModBlocks.DESHRET_COBBLESTONE));
                entries.add(new ItemStack(ModBlocks.HARDENED_DESHRET_STONE));
                entries.add(new ItemStack(ModBlocks.POLISHED_DESHRET_STONE));
                entries.add(new ItemStack(ModBlocks.SPOTTED_DESHRET));
                
                // Rock blocks
                entries.add(new ItemStack(ModBlocks.SMALL_ROCK));
                entries.add(new ItemStack(ModBlocks.MEDIUM_ROCK));
                entries.add(new ItemStack(ModBlocks.LARGE_ROCK));
                
                // Skip BLACK_* blocks as they don't have textures yet
                // These are mentioned in the errors but not properly registered
            })
            .build()
    );

    public static void registerItemGroups() {
        AncientCurse.LOGGER.info("Registering item groups for " + AncientCurse.MOD_ID);
    }
}