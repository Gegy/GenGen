package net.gegy1000.gengen.util.wrapper;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.generator.GenericChunkGenerator;
import net.gegy1000.gengen.core.impl.vanilla.ColumnPrimeWriterImpl;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorOverworld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class OverworldGeneratorWrapper extends ChunkGeneratorOverworld {
    private final GenericChunkGenerator generator;

    public OverworldGeneratorWrapper(World world, GenericChunkGenerator generator) {
        super(world, world.getWorldInfo().getSeed(), false, null);
        this.generator = generator;
    }

    @Nullable
    public static ChunkGeneratorOverworld from(World world) {
        GenericChunkGenerator generator = GenericChunkGenerator.unwrap(world);
        if (generator != null) {
            return new OverworldGeneratorWrapper(world, generator);
        }
        return null;
    }

    @Override
    public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
        for (int y = 0; y < 16; y++) {
            CubicPos pos = new CubicPos(x, y, z);
            this.generator.primeChunk(pos, new ColumnPrimeWriterImpl(primer, pos));
        }
    }
}
