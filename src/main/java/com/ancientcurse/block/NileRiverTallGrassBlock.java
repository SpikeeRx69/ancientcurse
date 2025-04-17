package com.ancientcurse.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

/**
 * Represents tall grass that appears near the Nile River.
 * This is a double-height plant block similar to vanilla tall grass.
 */
public class NileRiverTallGrassBlock extends TallPlantBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = TallPlantBlock.HALF;

    public NileRiverTallGrassBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (state.get(HALF) != DoubleBlockHalf.UPPER) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            
            // Can be placed on Nile River blocks, grass, dirt or regular soil
            return blockState.isIn(BlockTags.DIRT) || 
                   blockState.isOf(Blocks.GRASS_BLOCK) ||
                   blockState.isOf(Blocks.DIRT) ||
                   blockState.isOf(Blocks.COARSE_DIRT) ||
                   blockState.isOf(Blocks.PODZOL) || 
                   blockState.isOf(Blocks.FARMLAND) ||
                   "ancientcurse".equals(blockState.getBlock().getClass().getName().split("\\.")[1]);
        } else {
            BlockState blockState = world.getBlockState(pos.down());
            return blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.LOWER;
        }
    }
    
    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return false;
    }
    
    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.1f;
    }
} 