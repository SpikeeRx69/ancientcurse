package com.ancientcurse.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;

import java.util.stream.Stream;

public class AncientDesertBiomeSource extends BiomeSource {
    public static final Codec<AncientDesertBiomeSource> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryCodecs.entryList(RegistryKeys.BIOME).fieldOf("biomes")
                            .forGetter(source -> source.biomes)
            ).apply(instance, AncientDesertBiomeSource::new)
    );

    private final RegistryEntryList<Biome> biomes;

    public AncientDesertBiomeSource(RegistryEntryList<Biome> biomes) {
        this.biomes = biomes;
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    public RegistryEntry<Biome> getBiome(int x, int y, int z, MultiNoiseUtil.MultiNoiseSampler noiseSampler) {
        // Always return ancient desert biome
        return biomes.get(0);
    }

    @Override
    public Stream<RegistryEntry<Biome>> biomeStream() {
        return biomes.stream();
    }
} 