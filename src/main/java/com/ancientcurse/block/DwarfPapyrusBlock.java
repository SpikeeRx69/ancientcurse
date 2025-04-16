package com.ancientcurse.block;

import com.ancientcurse.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;

/**
 * Dwarf papyrus reed - a smaller version of papyrus that can be placed
 * near water for decoration.
 */
public class DwarfPapyrusBlock extends Block {
    // Small plant shape that's 12x13x12 pixels
    protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    
    public DwarfPapyrusBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.down());
        return isSupportingBlock(blockState, world, pos.down());
    }
    
    private boolean isSupportingBlock(BlockState state, WorldView world, BlockPos pos) {
        // Dwarf papyrus can be placed on soil blocks or near water
        Block block = state.getBlock();
        boolean isProperSoil = block == Blocks.SAND || 
                              block == Blocks.RED_SAND || 
                              block == ModBlocks.FERTILE_NILE_SILT || 
                              block == ModBlocks.DRY_NILE_SILT || 
                              block == Blocks.DIRT || 
                              block == Blocks.GRASS_BLOCK || 
                              block == ModBlocks.ARID_NILE_TURF ||
                              block == ModBlocks.NILE_RIVER_SAND ||
                              block == ModBlocks.NILE_MUD ||
                              block == ModBlocks.RIVERBED;
                              
        if (isProperSoil) {
            // Check for nearby water (not required but more realistic)
            for (Direction direction : Direction.Type.HORIZONTAL) {
                BlockPos adjacentPos = pos.offset(direction);
                FluidState fluidState = world.getFluidState(adjacentPos);
                if (fluidState.isIn(FluidTags.WATER)) {
                    return true;
                }
            }
            // Can still place it without water nearby
            return true;
        }
        
        return false;
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, 
                                               WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        // If support block is removed, break this block
        if (direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return null;
        }
        
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
} 