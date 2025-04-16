package com.ancientcurse.block;

import com.ancientcurse.ModBlocks;
import com.ancientcurse.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.List;

/**
 * Barley crop block that grows taller than wheat and produces barley grain.
 * Reaches up to 2 blocks tall when fully grown.
 */
public class BarleyCropBlock extends CropBlock {
    public static final IntProperty AGE = Properties.AGE_4; // 5 growth stages (0-4)
    public static final BooleanProperty TOP = BooleanProperty.of("top");
    
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[] {
        Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),  // Age 0
        Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),  // Age 1
        Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),  // Age 2
        Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), // Age 3
        Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)  // Age 4 (bottom part)
    };
    
    private static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    
    public BarleyCropBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(this.getAgeProperty(), 0)
                .with(TOP, false));
    }
    
    @Override
    public IntProperty getAgeProperty() {
        return AGE;
    }
    
    @Override
    public int getMaxAge() {
        return 4;
    }
    
    @Override
    protected ItemConvertible getSeedsItem() {
        return ModItems.BARLEY_SEEDS;
    }
    
    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.BARLEY_SEEDS);
    }

    // Override getDroppedStacks to control drops directly
    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> drops = super.getDroppedStacks(state, builder);
        int age = state.get(AGE);

        // Only drop barley if fully grown (bottom part)
        if (age == getMaxAge() && !state.get(TOP)) { 
            // Add barley (e.g., 1-2)
            drops.add(new ItemStack(ModItems.BARLEY, builder.getWorld().random.nextInt(2) + 1)); 
            // Remove default seed drop
            drops.removeIf(stack -> stack.isOf(ModItems.BARLEY_SEEDS));
            // Add back 1 seed guaranteed, plus chance for more
            drops.add(new ItemStack(ModItems.BARLEY_SEEDS, 1));
            if (builder.getWorld().random.nextInt(4) == 0) { // 25% chance for extra seed
                 drops.add(new ItemStack(ModItems.BARLEY_SEEDS, 1));
            }
        }
        // Don't drop anything from the top part when broken directly
        else if (state.get(TOP)) {
            drops.clear();
        } 
        
        return drops;
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(TOP)) {
            return TOP_SHAPE;
        }
        return AGE_TO_SHAPE[state.get(this.getAgeProperty())];
    }
    
    // Calculate growth speed based on the surrounding environment
    protected float getGrowthSpeed(World world, BlockPos pos) {
        float speed = 1.0F;
        BlockPos soilPos = pos.down();
        BlockState soilState = world.getBlockState(soilPos);
        
        // Check surrounding blocks for other crops (3x3x1 area)
        for (BlockPos blockPos : BlockPos.iterate(pos.add(-1, 0, -1), pos.add(1, 0, 1))) {
            float blockInfluence = 1.0F;
            
            // If checking the current position, skip it
            if (blockPos.equals(pos)) {
                continue;
            }
            
            BlockState blockState = world.getBlockState(blockPos);
            
            // Calculate influence based on adjacent blocks
            if (blockState.getBlock() instanceof CropBlock) {
                // Adjacent crops slightly reduce growth
                blockInfluence = 0.8F;
            } else if (blockState.isAir()) {
                // Air is good for growth
                blockInfluence = 1.2F;
            }
            
            // If diagonal, reduce influence
            if (blockPos.getX() != pos.getX() && blockPos.getZ() != pos.getZ()) {
                blockInfluence *= 0.5F;
            }
            
            speed *= blockInfluence;
        }
        
        // Special bonus for Nile silt variants
        if (soilState.isOf(ModBlocks.FERTILE_NILE_SILT)) {
            speed *= 1.75F; // Very good for growth
        } else if (soilState.isOf(ModBlocks.TILLED_NILE_SILT)) {
            speed *= 2.0F; // Excellent for growth
        } else if (soilState.getBlock() instanceof FarmlandBlock) {
            // Standard farmland still provides a bonus
            int moisture = soilState.contains(FarmlandBlock.MOISTURE) ? soilState.get(FarmlandBlock.MOISTURE) : 0;
            speed *= 1.0F + (moisture / 7.0F);
        }
        
        return speed;
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Skip top block - only the bottom one grows
        if (state.get(TOP)) {
            return;
        }
        
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int currentAge = this.getAge(state);
            
            if (currentAge < this.getMaxAge()) {
                float growthSpeed = getGrowthSpeed(world, pos);
                
                // Faster growth on Nile silts
                BlockState soilState = world.getBlockState(pos.down());
                if (soilState.isOf(ModBlocks.FERTILE_NILE_SILT) || 
                    soilState.isOf(ModBlocks.TILLED_NILE_SILT)) {
                    growthSpeed *= 1.5f;
                }
                
                if (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0) {
                    int newAge = currentAge + 1;
                    
                    // If growing to stage 4, also place the top block if there's space
                    if (newAge == 4 && world.isAir(pos.up())) {
                        world.setBlockState(pos, this.withAge(newAge), Block.NOTIFY_LISTENERS);
                        world.setBlockState(pos.up(), getDefaultState().with(TOP, true), Block.NOTIFY_LISTENERS);
                    } else if (newAge < 4) {
                        world.setBlockState(pos, this.withAge(newAge), Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        // Top part can only be placed above a mature barley bottom block
        if (state.get(TOP)) {
            BlockState belowState = world.getBlockState(pos.down());
            return belowState.isOf(this) && belowState.get(AGE) == 4 && !belowState.get(TOP);
        }
        
        // Bottom part follows normal crop rules
        BlockPos soilPos = pos.down();
        BlockState soilState = world.getBlockState(soilPos);
        
        // Can grow on vanilla farmland or any type of Nile silt
        return (super.canPlaceAt(state, world, pos) || 
                soilState.isOf(ModBlocks.FERTILE_NILE_SILT) || 
                soilState.isOf(ModBlocks.TILLED_NILE_SILT));
    }
    
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, 
                                               WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        // If this is the top block and the block below is gone or not a mature barley, break it
        if (state.get(TOP)) {
            if (direction == Direction.DOWN) {
                BlockState belowState = world.getBlockState(pos.down());
                if (!belowState.isOf(this) || belowState.get(AGE) != 4 || belowState.get(TOP)) {
                    return null;
                }
            }
        } 
        // If this is a mature bottom block and there's no top block, it's no longer fully grown
        // This should likely break the top part if it exists, handled by onBreak?
        else if (state.get(AGE) == 4) {
            if (direction == Direction.UP) {
                BlockState aboveState = world.getBlockState(pos.up());
                if (!aboveState.isOf(this) || !aboveState.get(TOP)) {
                    // We shouldn't revert age here, just let onBreak handle the top part if it exists
                    // return state.with(AGE, 3); // Reverting age could cause issues
                }
            }
        }
        
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // Allow harvesting by right-clicking the bottom part when fully grown
        if (!world.isClient && state.get(AGE) == 4 && !state.get(TOP)) {
            // Get drops based on loot context
            List<ItemStack> drops = getDroppedStacks(state, new LootContextParameterSet.Builder((ServerWorld)world)
                .add(LootContextParameters.BLOCK_STATE, state)
                .add(LootContextParameters.TOOL, player.getStackInHand(hand))
                .add(LootContextParameters.ORIGIN, hit.getPos()));
            
            // Spawn drops
            for (ItemStack drop : drops) {
                dropStack(world, pos, drop);
            }
            
            // Reset to age 0
            world.setBlockState(pos, this.withAge(0), Block.NOTIFY_LISTENERS);
            
            // Remove the top part
            if (world.getBlockState(pos.up()).isOf(this) && world.getBlockState(pos.up()).get(TOP)) {
                world.removeBlock(pos.up(), false);
            }
            
            return ActionResult.SUCCESS;
        }
        
        return ActionResult.PASS;
    }
    
    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // If breaking the bottom block, also break the top block
        if (!state.get(TOP) && state.get(AGE) == 4) {
            BlockPos topPos = pos.up();
            if (world.getBlockState(topPos).isOf(this) && world.getBlockState(topPos).get(TOP)) {
                 // Ensure the top block drops nothing when broken this way
                 world.removeBlock(topPos, false);
                 // world.breakBlock(topPos, false, player); // false might not prevent drops, use setBlockState
            }
        } 
        // If breaking the top block, ensure the bottom block remains (but might revert age via neighbor update)
        else if (state.get(TOP)) {
             BlockPos bottomPos = pos.down();
             BlockState bottomState = world.getBlockState(bottomPos);
             if (bottomState.isOf(this) && bottomState.get(AGE) == 4) {
                 // Optionally trigger neighbor update on bottom block if needed,
                 // although getStateForNeighborUpdate should handle this
             }
        }
        
        super.onBreak(world, pos, state, player);
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE, TOP);
    }
} 