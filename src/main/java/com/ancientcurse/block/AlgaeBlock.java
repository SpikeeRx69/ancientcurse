package com.ancientcurse.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

/**
 * Algae block that can be placed on water
 */
public class AlgaeBlock extends Block {
    // A thin shape for the algae layer on top of water
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.5, 16.0);

    public AlgaeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // No collision for algae
        return VoxelShapes.empty();
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos belowPos = pos.down();
        FluidState fluidStateBelow = world.getFluidState(belowPos);
        // Can only place on water source blocks
        return fluidStateBelow.isOf(Fluids.WATER) && fluidStateBelow.isStill();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        // If the block below is no longer water, break the algae
        if (direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return null;
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        
        // Allow placement if clicking on water
        if (fluidState.isOf(Fluids.WATER) && fluidState.isStill()) {
            return this.getDefaultState();
        }
        
        // Otherwise, use the standard canPlaceAt logic
        if (!this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
            return null;
        }
        return super.getPlacementState(ctx);
    }
    
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        
        // If it's a boat, slow it down slightly when passing through algae
        if (entity instanceof BoatEntity) {
            Vec3d velocity = entity.getVelocity();
            entity.setVelocity(velocity.x * 0.85, velocity.y, velocity.z * 0.85);
        }
    }
} 