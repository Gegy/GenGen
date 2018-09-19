package net.gegy1000.cubicglue.primer;

import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.CubicChunkPrimer;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GluedCubePrimer implements CubicChunkPrimer {
    private final CubePrimer primer;
    private final CubicPos pos;

    public GluedCubePrimer(CubePrimer primer, CubicPos pos) {
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
