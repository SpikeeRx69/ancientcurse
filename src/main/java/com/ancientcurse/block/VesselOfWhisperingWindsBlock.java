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
import net.minecraft.particle.ParticleTypes;

/**
 * Vessel of Whispering Winds - a mystical pottery item that glows and emits ethereal whispers
 */
public class VesselOfWhisperingWindsBlock extends Block {
    // Shape matches the tall vessel model with its distinctive neck and top
    protected static final VoxelShape SHAPE = Block.createCuboidShape(3.875, 0.0, 4.2125, 12.125, 16.25, 11.7125);
    
    public VesselOfWhisperingWindsBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        // Add ambient particles for a mystical effect
        if (random.nextInt(10) == 0) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.7D;
            double z = pos.getZ() + 0.5D;
            
            world.addParticle(
                ParticleTypes.END_ROD,
                x + (random.nextDouble() - 0.5D) * 0.5D,
                y + (random.nextDouble() - 0.5D) * 0.2D,
                z + (random.nextDouble() - 0.5D) * 0.5D,
                0, 0.05D, 0
            );
        }
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        // Players can interact with the vessel (for future functionality)
        ItemStack heldItem = player.getStackInHand(hand);
        
        if (!heldItem.isEmpty()) {
            // For now, just consume the action without doing anything
            // In the future this could trigger special effects or rituals
            return ActionResult.success(world.isClient);
        }
        
        return ActionResult.PASS;
    }
}
