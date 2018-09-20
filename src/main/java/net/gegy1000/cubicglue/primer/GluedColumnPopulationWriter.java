package net.gegy1000.cubicglue.primer;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.ChunkPopulationWriter;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GluedColumnPopulationWriter implements ChunkPopulationWriter {
    private final World world;
    private final CubicPos pos;

    public GluedColumnPopulationWriter(World world, CubicPos pos) {
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
        Chunk chunk = this.world.getChunk(pos);

        int minY = this.pos.getCenterY();

        BlockPos surfacePos = new BlockPos(pos.getX(), minY + 16, pos.getZ());
        BlockPos nextPos;

        while (surfacePos.getY() >= minY) {
            nextPos = surfacePos.down();
            IBlockState state = chunk.getBlockState(nextPos);

            if (state.getMaterial().blocksMovement() && !state.getBlock().isLeaves(state, this.world, nextPos) && !state.getBlock().isFoliage(this.world, nextPos)) {
                return surfacePos;
            }

            surfacePos = nextPos;
        }

        return null;
    }

    @Override
    public World getGlobal() {
        return this.world;
    }
}
