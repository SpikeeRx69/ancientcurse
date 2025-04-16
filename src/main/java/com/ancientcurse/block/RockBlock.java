package com.ancientcurse.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

/**
 * Base class for rock blocks that adapt to the texture of the block they're placed on.
 * Comes in three sizes: small, medium, and large.
 */
public abstract class RockBlock extends Block implements BlockColorProvider, ItemColorProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    
    // Abstract method to be implemented by each rock size
    protected abstract VoxelShape getShapeForDirection(Direction direction);
    
    public RockBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH));
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShapeForDirection(state.get(FACING));
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
        return this.getDefaultState().with(FACING, direction);
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        // Rocks can only be placed on solid blocks
        BlockPos blockPos = pos.down();
        return hasTopSolidSurface(world, blockPos);
    }
    
    private boolean hasTopSolidSurface(WorldView world, BlockPos pos) {
        return world.getBlockState(pos).isSolidBlock(world, pos);
    }
    
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
    
    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
    
    // Implement the BlockColorProvider interface to adapt the rock's color to the block below
    @Override
    public int getColor(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex) {
        if (pos == null) {
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
    
    // Implement the ItemColorProvider interface for item rendering
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        // Default stone color for items in inventory
        return 0x8F8F8F; // Stone gray color
    }
}
