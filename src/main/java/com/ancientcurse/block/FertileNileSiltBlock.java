package com.ancientcurse.block;

import com.ancientcurse.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * Implements fertile Nile silt that can be tilled with a hoe
 * to create farmland for crops.
 */
public class FertileNileSiltBlock extends Block {
    
    public FertileNileSiltBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getStackInHand(hand);
        
        // Check if the player is using a hoe
        if (heldItem.getItem() instanceof HoeItem) {
            // Don't convert if there's a solid block above
            if (!world.getBlockState(pos.up()).isAir()) {
                return ActionResult.PASS;
            }
            
            // Convert to tilled Nile silt
            world.setBlockState(pos, ModBlocks.TILLED_NILE_SILT.getDefaultState());
            
            // Play sound effect
            world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            
            // Damage the hoe
            if (!player.isCreative()) {
                heldItem.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
            }
            
            return ActionResult.success(world.isClient);
        }
        
        return ActionResult.PASS;
    }
} 