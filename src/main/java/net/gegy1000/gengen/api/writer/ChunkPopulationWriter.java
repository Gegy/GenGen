package net.gegy1000.gengen.api.writer;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ChunkPopulationWriter {
    BlockPos POPULATION_CENTER = new BlockPos(16, 0, 16);

    void set(BlockPos pos, IBlockState state);

    IBlockState get(BlockPos pos);

    @Nullable
    default BlockPos getSurface(BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos);
        if (!this.getSurfaceMut(mutablePos)) return null;
        return mutablePos;
    }

    boolean getSurfaceMut(BlockPos.MutableBlockPos pos);

    Biome getCenterBiome();

    World getGlobal();
}
