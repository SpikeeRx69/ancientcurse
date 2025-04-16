package com.ancientcurse.world.gen;

import com.ancientcurse.AncientCurse;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ModChunkGeneratorSettings {
    public static final RegistryKey<ChunkGeneratorSettings> ANCIENT_CURSE =
            RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, new Identifier(AncientCurse.MOD_ID, "ancient_curse"));

    // We don't bootstrap ChunkGeneratorSettings programmatically usually,
    // they are defined entirely in JSON.
} 