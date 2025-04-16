package com.ancientcurse.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * Egyptian offering pot - a decorative vase used in ancient Egyptian rituals
 */
public class OfferingPotBlock extends Block {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);

    public OfferingPotBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // Players can insert items into the offering pot (for future functionality)
        ItemStack heldItem = player.getStackInHand(hand);
        
        if (!heldItem.isEmpty()) {
            // For now, just consume the action without doing anything
            // In the future this could store items or trigger effects
            return ActionResult.success(world.isClient);
        }
        
        return ActionResult.PASS;
    }
} 