package net.gegy1000.gengen.core.impl.cubic;

import io.github.opencubicchunks.cubicchunks.api.util.Coords;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.event.PopulateCubeEvent;
import net.gegy1000.gengen.api.ChunkPopulationWriter;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.GenericEventDispatcher;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import java.util.Random;

public class CubeEventDispatcherImpl implements GenericEventDispatcher {
    @Override
    public void populate(World world, CubicPos pos, Runnable populate) {
        populate.run();
    }

    @Override
    public void populateFeature(ChunkPopulationWriter writer, CubicPos pos, PopulateChunkEvent.Populate.EventType type, Runnable populate) {
        World world = writer.getGlobal();
        Random random = Coords.coordsSeedRandom(world.getSeed(), pos.getX(), pos.getY(), pos.getZ());

        PopulateCubeEvent.Populate event = new PopulateCubeEvent.Populate(world, random, pos.getX(), pos.getY(), pos.getZ(), false, type);
        if (!MinecraftForge.TERRAIN_GEN_BUS.post(event)) {
            populate.run();
        }
    }
}
