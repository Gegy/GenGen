package net.gegy1000.cubicglue.util;

import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorld;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.CubePopulatorEvent;
import net.gegy1000.cubicglue.api.ChunkPopulationWriter;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

public interface GluedEventDispatcher {
    void populate(World world, CubicPos pos, Runnable populate);

    void populateFeature(CubicPos pos, ChunkPopulationWriter writer, PopulateChunkEvent.Populate.EventType type, Runnable populate);

    void spawnEntities(CubicPos pos, ChunkPopulationWriter writer, Runnable populate);

    class Vanilla implements GluedEventDispatcher {
        private final Random random = new Random(0);

        @Override
        public void populate(World world, CubicPos pos, Runnable populate) {
            this.initSeed(world, pos);

            IChunkGenerator generator = VanillaGeneratorCache.getGenerator(world);
            if (generator == null) {
                populate.run();
                return;
            }

            ForgeEventFactory.onChunkPopulate(true, generator, world, this.random, pos.getX(), pos.getZ(), false);
            populate.run();
            ForgeEventFactory.onChunkPopulate(false, generator, world, this.random, pos.getX(), pos.getZ(), false);
        }

        @Override
        public void populateFeature(CubicPos pos, ChunkPopulationWriter writer, PopulateChunkEvent.Populate.EventType type, Runnable populate) {
            World world = writer.getGlobal();
            this.initSeed(world, pos);

            IChunkGenerator generator = VanillaGeneratorCache.getGenerator(world);
            if (generator == null) {
                populate.run();
                return;
            }

            if (TerrainGen.populate(generator, world, this.random, pos.getX(), pos.getZ(), false, type)) {
                populate.run();
            }
        }

        @Override
        public void spawnEntities(CubicPos pos, ChunkPopulationWriter writer, Runnable populate) {
            this.populateFeature(pos, writer, PopulateChunkEvent.Populate.EventType.ANIMALS, populate);
        }

        private void initSeed(World world, CubicPos pos) {
            this.random.setSeed(world.getSeed());

            long seedX = this.random.nextLong() / 2 * 2 + 1;
            long seedZ = this.random.nextLong() / 2 * 2 + 1;
            this.random.setSeed(pos.getX() * seedX + pos.getZ() * seedZ ^ world.getSeed());
        }
    }

    class Cubic implements GluedEventDispatcher {
        @Override
        public void populate(World world, CubicPos pos, Runnable populate) {
            ICube cube = ((ICubicWorld) world).getCubeFromCubeCoords(pos.getX(), pos.getY(), pos.getZ());
            if (!MinecraftForge.EVENT_BUS.post(new CubePopulatorEvent(world, cube))) {
                populate.run();
            }
        }

        @Override
        public void populateFeature(CubicPos pos, ChunkPopulationWriter writer, PopulateChunkEvent.Populate.EventType type, Runnable populate) {
            populate.run();
        }

        @Override
        public void spawnEntities(CubicPos pos, ChunkPopulationWriter writer, Runnable populate) {
            populate.run();
        }
    }
}
