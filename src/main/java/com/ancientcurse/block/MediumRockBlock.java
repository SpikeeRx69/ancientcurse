package com.ancientcurse.block;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

/**
 * Medium rock block that adapts to the texture of the block it's placed on.
 */
public class MediumRockBlock extends RockBlock {
    // Define shapes for each direction - medium rocks are 6x3x6 pixels
    private static final VoxelShape NORTH_SHAPE = VoxelShapes.cuboid(0.3125, 0.0, 0.3125, 0.6875, 0.1875, 0.6875);
    private static final VoxelShape EAST_SHAPE = VoxelShapes.cuboid(0.3125, 0.0, 0.3125, 0.6875, 0.1875, 0.6875);
    private static final VoxelShape SOUTH_SHAPE = VoxelShapes.cuboid(0.3125, 0.0, 0.3125, 0.6875, 0.1875, 0.6875);
    private static final VoxelShape WEST_SHAPE = VoxelShapes.cuboid(0.3125, 0.0, 0.3125, 0.6875, 0.1875, 0.6875);
    
    public MediumRockBlock(Settings settings) {
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
