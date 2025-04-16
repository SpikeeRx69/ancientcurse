package com.ancientcurse.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

/**
 * Custom leaf block for Date Palm trees
 */
public class DatePalmLeafBlock extends LeavesBlock {
    
    public DatePalmLeafBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(DISTANCE, 7)
                .with(PERSISTENT, false)
                .with(Properties.WATERLOGGED, false));
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Call the parent method to handle leaf decay
        super.randomTick(state, world, pos, random);
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, Properties.WATERLOGGED);
    }
}
