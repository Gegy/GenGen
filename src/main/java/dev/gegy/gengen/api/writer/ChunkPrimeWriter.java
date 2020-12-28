package dev.gegy.gengen.api.writer;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ChunkPrimeWriter {
    void set(int x, int y, int z, IBlockState state);

    IBlockState get(int x, int y, int z);
}
