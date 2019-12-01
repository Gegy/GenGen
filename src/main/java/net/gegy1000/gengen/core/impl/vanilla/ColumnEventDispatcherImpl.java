package net.gegy1000.gengen.core.impl.vanilla;

import net.gegy1000.gengen.api.ChunkPopulationWriter;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.GenericEventDispatcher;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nullable;
import java.util.Random;

public class ColumnEventDispatcherImpl implements GenericEventDispatcher {
    private final Random random = new Random(0);

    @Override
    public void populate(World world, CubicPos pos, Runnable populate) {
        this.initSeed(world, pos);

        IChunkGenerator generator = getGeneratorFor(world);
        if (generator == null) {
            populate.run();
            return;
        }

        ForgeEventFactory.onChunkPopulate(true, generator, world, this.random, pos.getX(), pos.getZ(), false);
        populate.run();
        ForgeEventFactory.onChunkPopulate(false, generator, world, this.random, pos.getX(), pos.getZ(), false);
    }

    @Override
    public void populateFeature(ChunkPopulationWriter writer, CubicPos pos, PopulateChunkEvent.Populate.EventType type, Runnable populate) {
        World world = writer.getGlobal();
        this.initSeed(world, pos);

        IChunkGenerator generator = getGeneratorFor(world);
        if (generator == null) {
            populate.run();
            return;
        }

        if (TerrainGen.populate(generator, world, this.random, pos.getX(), pos.getZ(), false, type)) {
            populate.run();
        }
    }

    private void initSeed(World world, CubicPos pos) {
        this.random.setSeed(world.getSeed());

        long seedX = this.random.nextLong() / 2 * 2 + 1;
        long seedZ = this.random.nextLong() / 2 * 2 + 1;
        this.random.setSeed(pos.getX() * seedX + pos.getZ() * seedZ ^ world.getSeed());
    }

    @Nullable
    private static IChunkGenerator getGeneratorFor(World world) {
        IChunkProvider provider = world.getChunkProvider();
        if (provider instanceof ChunkProviderServer) {
            return ((ChunkProviderServer) provider).chunkGenerator;
        }
        return null;
    }
}
