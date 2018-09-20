package net.gegy1000.cubicglue.primer;

import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorld;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.ChunkPopulationWriter;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GluedCubePopulationWriter implements ChunkPopulationWriter {
    private final World world;
    private final CubicPos pos;

    public GluedCubePopulationWriter(World world, CubicPos pos) {
        this.world = world;
        this.pos = pos;
    }

    @Override
    public void set(BlockPos pos, IBlockState state) {
        this.world.setBlockState(pos, state);
    }

    @Override
    public IBlockState get(BlockPos pos) {
        return this.world.getBlockState(pos);
    }

    @Nullable
    @Override
    public BlockPos getSurface(BlockPos pos) {
        int minFreeY = this.pos.getCenterY();
        int maxFreeY = minFreeY + 16;

        BlockPos start = new BlockPos(pos.getX(), this.pos.getMaxY() + 16, pos.getZ());
        return ((ICubicWorld) this.world).findTopBlock(start, minFreeY, maxFreeY, ICubicWorld.SurfaceType.SOLID);
    }

    @Override
    public World getGlobal() {
        return this.world;
    }
}
