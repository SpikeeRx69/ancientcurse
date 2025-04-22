package com.ancientcurse.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * Pharaoh's Incense Jar - a ceremonial container used for burning sacred incense
 * Emits a soft glow when placed
 */
public class PharaohsIncenseJarBlock extends Block {
    // Shape matches the jar model
    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.5, 0.0, 4.5, 11.5, 14.0, 11.5);
    
    public PharaohsIncenseJarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
