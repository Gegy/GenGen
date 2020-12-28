package dev.gegy.gengen.core.impl.cubic;

import dev.gegy.gengen.api.CubicPos;
import dev.gegy.gengen.api.writer.ChunkPopulationWriter;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CubePopulationWriterImpl implements ChunkPopulationWriter {
    private final World world;
    private final CubicPos pos;

    public CubePopulationWriterImpl(World world, CubicPos pos) {
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

    @Override
    public boolean getSurfaceMut(BlockPos.MutableBlockPos pos) {
        Chunk chunk = this.world.getChunk(pos);

        int minY = this.pos.getCenterY();
        pos.setY(minY + 16);

        while (pos.getY() >= minY) {
            pos.move(EnumFacing.DOWN);
            if (chunk.getBlockState(pos).getLightOpacity(this.world, pos) != 0) {
                pos.move(EnumFacing.UP);
                return true;
            }
        }

        return false;
    }

    @Override
    public Biome getCenterBiome() {
        return this.world.getChunk(this.pos.getX(), this.pos.getZ()).getBiome(POPULATION_CENTER, this.world.getBiomeProvider());
    }

    @Override
    public World getGlobal() {
        return this.world;
    }
}
