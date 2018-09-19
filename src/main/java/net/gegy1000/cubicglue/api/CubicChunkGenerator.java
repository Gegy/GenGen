package net.gegy1000.cubicglue.api;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface CubicChunkGenerator {
    void prime(CubicPos pos, CubicChunkPrimer primer);

    void populate(CubicPos pos);

    void populateBiomes(ChunkPos pos, Biome[] buffer);
}
