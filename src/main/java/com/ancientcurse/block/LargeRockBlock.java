package com.ancientcurse.block;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

/**
 * Large rock block that adapts to the texture of the block it's placed on.
 */
public class LargeRockBlock extends RockBlock {
    // Define shapes for each direction - large rocks are 8x4x8 pixels
    private static final VoxelShape NORTH_SHAPE = VoxelShapes.cuboid(0.25, 0.0, 0.25, 0.75, 0.25, 0.75);
    private static final VoxelShape EAST_SHAPE = VoxelShapes.cuboid(0.25, 0.0, 0.25, 0.75, 0.25, 0.75);
    private static final VoxelShape SOUTH_SHAPE = VoxelShapes.cuboid(0.25, 0.0, 0.25, 0.75, 0.25, 0.75);
    private static final VoxelShape WEST_SHAPE = VoxelShapes.cuboid(0.25, 0.0, 0.25, 0.75, 0.25, 0.75);
    
    public LargeRockBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    protected VoxelShape getShapeForDirection(Direction direction) {
        switch (direction) {
            case EAST:
                return EAST_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            case NORTH:
            default:
                return NORTH_SHAPE;
        }
    }
}
