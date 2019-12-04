package net.gegy1000.gengen.core;

import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.WorldGenEntitySpawner;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.event.PopulateCubeEvent;
import io.github.opencubicchunks.cubicchunks.core.server.CubeProviderServer;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.generator.GenericChunkGenerator;
import net.gegy1000.gengen.api.writer.ChunkPopulationWriter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nullable;
import java.util.Random;

public interface GenGenWorldProxy {
    @Nullable
    GenericChunkGenerator unwrapChunkGenerator(World world);

    void populateEntities(CubicPos pos, ChunkPopulationWriter writer, Random random);

    class Cubic implements GenGenWorldProxy {
        @Nullable
        @Override
        public GenericChunkGenerator unwrapChunkGenerator(World world) {
            IChunkProvider provider = world.getChunkProvider();
            if (provider instanceof CubeProviderServer) {
                ICubeGenerator generator = ((CubeProviderServer) provider).getCubeGenerator();
                return GenericChunkGenerator.unwrap(generator);
            } else if (provider instanceof ChunkProviderServer) {
                IChunkGenerator generator = ((ChunkProviderServer) provider).chunkGenerator;
                return GenericChunkGenerator.unwrap(generator);
            }
            return null;
        }

        @Override
        public void populateEntities(CubicPos pos, ChunkPopulationWriter writer, Random random) {
            World world = writer.getGlobal();
            if (world instanceof WorldServer) {
                PopulateCubeEvent.Populate event = new PopulateCubeEvent.Populate(world, random, pos.getX(), pos.getY(), pos.getZ(), false, PopulateChunkEvent.Populate.EventType.ANIMALS);
                if (MinecraftForge.TERRAIN_GEN_BUS.post(event)) return;

                BlockPos origin = pos.getCenter();
                Biome biome = writer.getCenterBiome();
                WorldGenEntitySpawner.initialWorldGenSpawn((WorldServer) world, biome, origin.getX(), origin.getY(), origin.getZ(), 16, 16, 16, random);
            }
        }
    }

    class Vanilla implements GenGenWorldProxy {
        @Nullable
        @Override
        public GenericChunkGenerator unwrapChunkGenerator(World world) {
            IChunkGenerator generator = this.getChunkGenerator(world);
            if (generator == null) return null;

            return GenericChunkGenerator.unwrap(generator);
        }

        @Override
        public void populateEntities(CubicPos pos, ChunkPopulationWriter writer, Random random) {
            World world = writer.getGlobal();
            Biome biome = writer.getCenterBiome();

            IChunkGenerator generator = this.getChunkGenerator(world);
            if (generator == null) return;

            if (TerrainGen.populate(generator, world, random, pos.getX(), pos.getZ(), false, PopulateChunkEvent.Populate.EventType.ANIMALS)) {
                int centerX = pos.getCenterX();
                int centerZ = pos.getCenterZ();
                WorldEntitySpawner.performWorldGenSpawning(world, biome, centerX, centerZ, 16, 16, random);
            }
        }

        @Nullable
        private IChunkGenerator getChunkGenerator(World world) {
            IChunkProvider provider = world.getChunkProvider();
            if (provider instanceof ChunkProviderServer) {
                return ((ChunkProviderServer) provider).chunkGenerator;
            }
            return null;
        }
    }
}
