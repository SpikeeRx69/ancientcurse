package com.ancientcurse.item;

import com.ancientcurse.ModBlocks;
import com.ancientcurse.block.DateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SekhemDateItem extends Item {
    
    public SekhemDateItem(Settings settings) {
        super(settings
            .food(new FoodComponent.Builder()
                .hunger(5)
                .saturationModifier(1.0f)
                .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 0), 1.0f)
                .build())
        );
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        PlayerEntity player = context.getPlayer();
        
        // Check if the block is a Date Palm Log
        if (blockState.isOf(ModBlocks.DATE_PALM_LOG)) {
            Direction side = context.getSide();
            
            // Only allow placing on horizontal sides
            if (side.getAxis().isHorizontal()) {
                BlockPos placePos = blockPos.offset(side);
                
                // Check if the position is valid for placement
                if (world.getBlockState(placePos).isAir()) {
                    // Create the date block state with the correct facing direction
                    // For dates, the FACING points TOWARD the log, not away from it
                    BlockState dateBlockState = ModBlocks.DATE_BLOCK.getDefaultState()
                            .with(DateBlock.FACING, side.getOpposite())
                            .with(DateBlock.AGE, 0); // Start with stage 0 (young date)
                    
                    // Place the block
                    world.setBlockState(placePos, dateBlockState);
                    
                    // Play placement sound
                    world.playSound(
                        player, 
                        placePos, 
                        SoundEvents.BLOCK_WOOD_PLACE, 
                        SoundCategory.BLOCKS, 
                        1.0f, 
                        0.8f
                    );
                    
                    // Consume one date if not in creative mode
                    if (player != null && !player.getAbilities().creativeMode) {
                        context.getStack().decrement(1);
                    }
                    
                    return ActionResult.success(world.isClient);
                }
            }
        }
        
        // If we can't place the date, fall back to normal item usage
        return super.useOnBlock(context);
    }
}
