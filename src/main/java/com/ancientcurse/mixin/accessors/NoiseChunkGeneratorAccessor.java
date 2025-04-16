package com.ancientcurse.mixin.accessors;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor mixin for NoiseChunkGenerator to retrieve its settings field.
 */
@Mixin(NoiseChunkGenerator.class)
public interface NoiseChunkGeneratorAccessor {

    @Accessor("settings")
    RegistryEntry<ChunkGeneratorSettings> getSettings();

} 