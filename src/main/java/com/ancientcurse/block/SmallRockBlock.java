package com.ancientcurse.block;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

/**
 * Small rock block that adapts to the texture of the block it's placed on.
 */
public class SmallRockBlock extends RockBlock {
    // Define shapes for each direction - small rocks are 4x2x4 pixels
    private static final VoxelShape NORTH_SHAPE = VoxelShapes.cuboid(0.375, 0.0, 0.375, 0.625, 0.125, 0.625);
    private static final VoxelShape EAST_SHAPE = VoxelShapes.cuboid(0.375, 0.0, 0.375, 0.625, 0.125, 0.625);
    private static final VoxelShape SOUTH_SHAPE = VoxelShapes.cuboid(0.375, 0.0, 0.375, 0.625, 0.125, 0.625);
    private static final VoxelShape WEST_SHAPE = VoxelShapes.cuboid(0.375, 0.0, 0.375, 0.625, 0.125, 0.625);
    
    public SmallRockBlock(Settings settings) {
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
