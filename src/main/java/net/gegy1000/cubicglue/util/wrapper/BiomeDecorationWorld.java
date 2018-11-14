package net.gegy1000.cubicglue.util.wrapper;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BiomeDecorationWorld extends World {
    private final World parent;
    private final CubicPos decorationChunk;

    public BiomeDecorationWorld(World parent, CubicPos decorationChunk) {
        super(parent.getSaveHandler(), parent.getWorldInfo(), parent.provider, parent.profiler, parent.isRemote);
        this.parent = parent;
        this.decorationChunk = decorationChunk;
        this.chunkProvider = this.parent.getChunkProvider();
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
        if (!this.contains(pos)) {
            return false;
        }
        return this.parent.setBlockState(pos, newState, flags);
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        if (!this.contains(pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return this.parent.getBlockState(pos);
    }

    @Override
    public BlockPos getTopSolidOrLiquidBlock(BlockPos pos) {
        Chunk chunk = this.parent.getChunk(pos);

        int minY = this.decorationChunk.getCenterY();

        BlockPos surfacePos = new BlockPos(pos.getX(), minY + 16, pos.getZ());
        BlockPos nextPos;

        while (surfacePos.getY() >= minY) {
            nextPos = surfacePos.down();
            IBlockState state = chunk.getBlockState(nextPos);

            if (state.getMaterial().blocksMovement() && !state.getBlock().isLeaves(state, this.parent, nextPos) && !state.getBlock().isFoliage(this.parent, nextPos)) {
                return surfacePos;
            }

            surfacePos = nextPos;
        }

        return new BlockPos(pos.getX(), Short.MAX_VALUE + 1, pos.getZ());
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return this.parent.getChunkProvider();
    }

    @Override
    public IChunkProvider getChunkProvider() {
        return this.parent.getChunkProvider();
    }

    @Override
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
        return this.getChunkProvider().getLoadedChunk(x, z) != null;
    }

    private boolean contains(BlockPos pos) {
        int minX = this.decorationChunk.getCenterX();
        int minY = this.decorationChunk.getCenterY();
        int minZ = this.decorationChunk.getCenterZ();
        return pos.getX() >= minX && pos.getY() >= minY && pos.getZ() >= minZ
                && pos.getX() < minX + 16 && pos.getY() < minY + 16 && pos.getZ() < minZ + 16;
    }
}
