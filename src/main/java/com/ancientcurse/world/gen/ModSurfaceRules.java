package com.ancientcurse.world.gen;

import com.ancientcurse.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;

/**
 * Template for surface rules that can be implemented later.
 * Currently using vanilla Minecraft surface generation.
 */
public class ModSurfaceRules {
    
    /**
     * Create surface rules for the mod.
     * This is a placeholder that can be expanded later.
     */
    public static MaterialRule createSurfaceRules() {
        // Return vanilla surface rules as fallback
        return MaterialRules.sequence(
            MaterialRules.condition(
                MaterialRules.surface(),
                block(Blocks.GRASS_BLOCK)
            )
        );
    }
    
    // Helper method to create block rules
    private static MaterialRule block(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
} 