package net.gegy1000.gengen.api;

import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

public interface GenericEventDispatcher {
    void populate(World world, CubicPos pos, Runnable populate);

    void populateFeature(ChunkPopulationWriter writer, CubicPos pos, PopulateChunkEvent.Populate.EventType type, Runnable populate);

    default void populateEntities(ChunkPopulationWriter writer, CubicPos pos, Runnable populate) {
        this.populateFeature(writer, pos, PopulateChunkEvent.Populate.EventType.ANIMALS, populate);
    }
}
