package com.ancientcurse.client.color;

import com.ancientcurse.block.RockBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

/**
 * Client-side color provider for rock blocks.
 * This handles the coloring of rocks based on the block beneath them.
 */
public class RockColorProvider implements BlockColorProvider, ItemColorProvider {
    
    @Override
    public int getColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex) {
        if (pos == null || !(state.getBlock() instanceof RockBlock)) {
            return 0xFFFFFF; // Default white color
        }
        
        BlockPos belowPos = pos.down();
        BlockState belowState = world.getBlockState(belowPos);
        
        // Try to get the color from the block below
        try {
            // Use the block's map color as a fallback
            return belowState.getMapColor(world, belowPos).color;
        } catch (Exception e) {
            return 0xFFFFFF; // Default white color if there's an error
        }
    }
    
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        // Default stone color for items in inventory
        return 0x8F8F8F; // Stone gray color
    }
} 