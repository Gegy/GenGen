package net.gegy1000.cubicglue.api;

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
    BlockPos getSurface(BlockPos pos);

    Biome getCenterBiome();

    World getGlobal();
}
