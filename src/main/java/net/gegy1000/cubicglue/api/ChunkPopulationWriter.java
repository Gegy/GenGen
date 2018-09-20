package net.gegy1000.cubicglue.api;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ChunkPopulationWriter {
    void set(BlockPos pos, IBlockState state);

    IBlockState get(BlockPos pos);

    @Nullable
    BlockPos getSurface(BlockPos pos);

    World getGlobal();
}
