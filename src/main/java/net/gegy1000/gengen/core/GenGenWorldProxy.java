package net.gegy1000.gengen.core;

import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.WorldGenEntitySpawner;
import io.github.opencubicchunks.cubicchunks.core.server.CubeProviderServer;
import net.gegy1000.gengen.api.ChunkPopulationWriter;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.GenericChunkGenerator;
import net.gegy1000.gengen.api.GenericEventDispatcher;
import net.gegy1000.gengen.core.impl.cubic.CubeEventDispatcherImpl;
import net.gegy1000.gengen.core.impl.vanilla.ColumnEventDispatcherImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.Random;

public interface GenGenWorldProxy {
    GenericEventDispatcher getEventDispatcher();

    @Nullable
    GenericChunkGenerator unwrapChunkGenerator(World world);

    void populateEntities(CubicPos pos, ChunkPopulationWriter writer, Random random);

    class Cubic implements GenGenWorldProxy {
        private final GenericEventDispatcher dispatcher = new CubeEventDispatcherImpl();

        @Override
        public GenericEventDispatcher getEventDispatcher() {
            return this.dispatcher;
        }

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
            this.getEventDispatcher().populateEntities(writer, pos, () -> {
                World world = writer.getGlobal();
                if (world instanceof WorldServer) {
                    BlockPos origin = pos.getCenter();
                    Biome biome = writer.getCenterBiome();
                    WorldGenEntitySpawner.initialWorldGenSpawn((WorldServer) world, biome, origin.getX(), origin.getY(), origin.getZ(), 16, 16, 16, random);
                }
            });
        }
    }

    class Vanilla implements GenGenWorldProxy {
        private final GenericEventDispatcher dispatcher = new ColumnEventDispatcherImpl();

        @Override
        public GenericEventDispatcher getEventDispatcher() {
            return this.dispatcher;
        }

        @Nullable
        @Override
        public GenericChunkGenerator unwrapChunkGenerator(World world) {
            IChunkProvider provider = world.getChunkProvider();
            if (provider instanceof ChunkProviderServer) {
                IChunkGenerator generator = ((ChunkProviderServer) provider).chunkGenerator;
                return GenericChunkGenerator.unwrap(generator);
            }
            return null;
        }

        @Override
        public void populateEntities(CubicPos pos, ChunkPopulationWriter writer, Random random) {
            World world = writer.getGlobal();
            Biome biome = writer.getCenterBiome();
            this.getEventDispatcher().populateEntities(writer, pos, () -> {
                int centerX = pos.getCenterX();
                int centerZ = pos.getCenterZ();
                WorldEntitySpawner.performWorldGenSpawning(world, biome, centerX, centerZ, 16, 16, random);
            });
        }
    }
}
