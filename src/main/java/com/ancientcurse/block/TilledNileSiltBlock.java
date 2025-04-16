package com.ancientcurse.block;

import com.ancientcurse.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

/**
 * Implements a tilled version of Nile Silt, similar to vanilla Farmland
 * but optimized for desert crops.
 */
public class TilledNileSiltBlock extends Block {
    public static final IntProperty MOISTURE = Properties.MOISTURE;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
    
    public TilledNileSiltBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(MOISTURE, 0));
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // Check if there's water nearby during placement
        return !this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) 
            ? ModBlocks.FERTILE_NILE_SILT.getDefaultState() 
            : super.getPlacementState(ctx);
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.up());
        return !blockState.isOpaque() || blockState.getBlock() instanceof FarmlandBlock;
    }
    
    public BlockState getStateForNeighborUpdate(
        BlockState state, Direction direction, BlockState neighborState, 
        World world, BlockPos pos, BlockPos neighborPos
    ) {
        if (direction == Direction.UP && !state.canPlaceAt(world, pos)) {
            world.scheduleBlockTick(pos, this, 1);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!state.canPlaceAt(world, pos)) {
            // Convert back to fertile or dry Nile silt based on moisture
            BlockState fallbackState = state.get(MOISTURE) > 0 
                ? ModBlocks.FERTILE_NILE_SILT.getDefaultState()
                : ModBlocks.DRY_NILE_SILT.getDefaultState();
                
            world.setBlockState(pos, fallbackState);
        }
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int moisture = state.get(MOISTURE);
        
        // Check if there's water nearby
        if (!hasWater(world, pos) && !world.hasRain(pos.up())) {
            // Dry out gradually in desert climate
            if (moisture > 0) {
                world.setBlockState(pos, state.with(MOISTURE, moisture - 1), Block.NOTIFY_LISTENERS);
            }
        } else if (moisture < 7) {
            // Add moisture if water is nearby
            world.setBlockState(pos, state.with(MOISTURE, 7), Block.NOTIFY_LISTENERS);
        }
    }
    
    // Check for water nearby - Nile silt can pull moisture from further away than regular farmland
    private static boolean hasWater(WorldView world, BlockPos pos) {
        // Check a larger radius for water sources (Nile silt is more efficient at pulling water)
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-3, 0, -3), pos.add(3, 1, 3))) {
            if (world.getBlockState(blockPos).getBlock() == Blocks.WATER) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        // Convert to regular Nile Silt when trampled, like vanilla farmland
        if (!world.isClient && 
            world.random.nextFloat() < fallDistance - 0.5F && 
            entity instanceof LivingEntity && 
            (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) && 
            entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F) {
            
            // Convert to fertile or dry based on moisture
            BlockState fallbackState = state.get(MOISTURE) > 0 
                ? ModBlocks.FERTILE_NILE_SILT.getDefaultState()
                : ModBlocks.DRY_NILE_SILT.getDefaultState();
                
            world.setBlockState(pos, fallbackState);
        }
        
        super.onLandedUpon(world, state, pos, entity, fallDistance);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(MOISTURE);
    }
} 