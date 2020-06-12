package net.gegy1000.gengen.core.impl.vanilla;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.writer.ChunkPrimeWriter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ColumnPrimeWriterImpl implements ChunkPrimeWriter {
    private final ChunkPrimer primer;
    private final int minY;

    public ColumnPrimeWriterImpl(ChunkPrimer primer, CubicPos pos) {
        this.primer = primer;
        this.minY = pos.getMinY();
    }

    @Override
    public void set(int x, int y, int z, IBlockState state) {
        this.primer.setBlockState(x & 0xF, (y & 0xF) + this.minY, z & 0xF, state);
    }

    @Override
    public IBlockState get(int x, int y, int z) {
        return this.primer.getBlockState(x & 0xF, (y & 0xF) + this.minY, z & 0xF);
    }
}
