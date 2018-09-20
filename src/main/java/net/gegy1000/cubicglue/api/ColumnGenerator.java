package net.gegy1000.cubicglue.api;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.CubicGlue;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface ColumnGenerator {
    @Nullable
    static ColumnGenerator unwrap(World world) {
        return CubicGlue.proxy().unwrapColumnGenerator(world);
    }

    void primeColumnTerrain(int x, int z, ChunkPrimer primer);
}
