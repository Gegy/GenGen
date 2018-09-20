package net.gegy1000.cubicglue.util.wrapper;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.ColumnGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class OverworldGeneratorWrapper extends ChunkGeneratorOverworld {
    private final ColumnGenerator generator;

    public OverworldGeneratorWrapper(World world, ColumnGenerator generator) {
        super(world, world.getWorldInfo().getSeed(), false, "");
        this.generator = generator;
    }

    public static ChunkGeneratorOverworld from(World world) {
        ColumnGenerator generator = ColumnGenerator.unwrap(world);
        if (generator != null) {
            return new OverworldGeneratorWrapper(world, generator);
        }
        throw new IllegalArgumentException("Given world does not support column generation");
    }

    @Override
    public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
        this.generator.primeColumnTerrain(x, z, primer);
    }
}
