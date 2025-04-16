package com.ancientcurse.block;

import com.ancientcurse.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Riverbed Clay block that drops a random amount of Raw Riverbed Clay when mined.
 */
public class RiverbedClayBlock extends Block {
    
    public RiverbedClayBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && !player.isCreative()) {
            // Generate a random number between 1 and 4
            int dropCount = world.getRandom().nextBetween(1, 4);
            
            // Drop the Raw Riverbed Clay items
            ItemStack itemStack = new ItemStack(ModItems.RAW_RIVERBED_CLAY, dropCount);
            Block.dropStack(world, pos, itemStack);
        }
        super.onBreak(world, pos, state, player);
    }
}
