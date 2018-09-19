package net.gegy1000.cubicglue.api;

import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.GluedColumnGenerator;
import net.gegy1000.cubicglue.GluedCubeGenerator;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface CubicChunkGenerator {
    @Nullable
    static CubicChunkGenerator unwrap(IChunkGenerator generator) {
        if (generator instanceof GluedColumnGenerator) {
            return ((GluedColumnGenerator) generator).getInner();
        }
        return null;
    }

    @Nullable
    static CubicChunkGenerator unwrap(ICubeGenerator generator) {
        if (generator instanceof GluedCubeGenerator) {
            return ((GluedCubeGenerator) generator).getInner();
        }
        return null;
    }

    void prime(CubicPos pos, CubicChunkPrimer primer);

    void populate(CubicPos pos);

    Biome[] populateBiomes(ChunkPos pos, Biome[] buffer);
}
