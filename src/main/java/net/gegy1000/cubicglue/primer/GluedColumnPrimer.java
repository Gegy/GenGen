package net.gegy1000.cubicglue.primer;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.CubicChunkPrimer;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GluedColumnPrimer implements CubicChunkPrimer {
    private final ChunkPrimer primer;
    private final CubicPos pos;

    public GluedColumnPrimer(ChunkPrimer primer, CubicPos pos) {
        this.primer = primer;
        this.pos = pos;
    }

    @Override
    public void set(int x, int y, int z, IBlockState state) {
        if (y >= this.pos.getMinY() && y <= this.pos.getMaxY()) {
            this.primer.setBlockState(x & 0xF, y, z & 0xF, state);
        }
    }

    @Override
    public IBlockState get(int x, int y, int z) {
        if (y >= this.pos.getMinY() && y <= this.pos.getMaxY()) {
            return this.primer.getBlockState(x & 0xF, y, z & 0xF);
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }
}
