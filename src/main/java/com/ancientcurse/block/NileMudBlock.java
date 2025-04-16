package com.ancientcurse.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class NileMudBlock extends Block {
    
    public NileMudBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        // Apply strong slowdown effect and pull entities down
        if (!entity.isSpectator()) {
            // Slow down horizontal movement severely
            entity.slowMovement(state, new Vec3d(0.3D, 0.5D, 0.3D));
            
            // Apply sinking effect - gradually pull entities down
            if (!entity.isOnGround()) {
                double sinkSpeed = entity.getVelocity().y > -0.1D ? -0.1D : -0.05D;
                entity.setVelocity(entity.getVelocity().multiply(0.4, 0.95, 0.4).add(0, sinkSpeed, 0));
            }
            
            // Apply slowness effect to living entities
            if (entity instanceof LivingEntity livingEntity && world.getTime() % 20 == 0) {
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 1, false, false, true));
                
                // Play mud sound periodically when moving
                Vec3d velocity = entity.getVelocity();
                if ((Math.abs(velocity.x) > 0.05 || Math.abs(velocity.z) > 0.05) && world.getTime() % 16 == 0) {
                    world.playSound(null, pos, SoundEvents.BLOCK_MUD_STEP, SoundCategory.BLOCKS, 
                            0.5f, 0.8f + world.random.nextFloat() * 0.4f);
                    
                    // Add mud bubble particles when entity struggles
                    if (world.isClient) {
                        spawnMudBubbleParticles(world, entity.getX(), entity.getY(), entity.getZ(), world.random);
                    }
                }
            }
            
            // Special player handling
            if (entity instanceof PlayerEntity player && world.isClient) {
                // Show more intense bubbles when player is moving vertically (likely struggling)
                Vec3d velocity = player.getVelocity();
                if (Math.abs(velocity.y) > 0.08 && world.getTime() % 4 == 0) {
                    spawnMudBubbleParticles(world, player.getX(), player.getY(), player.getZ(), world.random);
                    world.playSound(player.getX(), player.getY(), player.getZ(), 
                                   SoundEvents.BLOCK_MUD_BREAK, 
                                   SoundCategory.BLOCKS, 0.4f, 0.5f, false);
                }
            }
        }
    }
    
    private void spawnMudBubbleParticles(World world, double x, double y, double z, Random random) {
        for (int i = 0; i < 5; i++) {
            double offsetX = random.nextDouble() * 0.6 - 0.3;
            double offsetZ = random.nextDouble() * 0.6 - 0.3;
            world.addParticle(
                ParticleTypes.FALLING_WATER, 
                x + offsetX, 
                y, 
                z + offsetZ, 
                0, 0.1, 0
            );
        }
    }
    
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // Display mud bubble particles randomly
        if (random.nextInt(10) == 0) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + random.nextDouble();
            
            // Spawn mud bubble particles
            world.addParticle(ParticleTypes.FALLING_WATER, x, y, z, 0, 0, 0);
            
            // Occasionally play a subtle bubbling sound
            if (random.nextInt(10) == 0) {
                float pitch = 0.7f + random.nextFloat() * 0.3f;
                world.playSound(x, y, z, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS, 
                        0.2f, pitch, false);
            }
        }
    }
} 