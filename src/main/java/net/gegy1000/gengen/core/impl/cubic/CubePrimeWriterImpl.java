package net.gegy1000.gengen.core.impl.cubic;

import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.ChunkPrimeWriter;
import net.minecraft.block.state.IBlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CubePrimeWriterImpl implements ChunkPrimeWriter {
    private final CubePrimer primer;

    public CubePrimeWriterImpl(CubePrimer primer) {
        this.primer = primer;
    }

    @Override
    public void set(int x, int y, int z, IBlockState state) {
        this.primer.setBlockState(x & 0xF, y & 0xF, z & 0xF, state);
    }

    @Override
    public IBlockState get(int x, int y, int z) {
        return this.primer.getBlockState(x & 0xF, y & 0xF, z & 0xF);
    }
}
