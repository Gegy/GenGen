package net.gegy1000.gengen.core.impl.cubic;

import io.github.opencubicchunks.cubicchunks.api.util.Box;
import io.github.opencubicchunks.cubicchunks.api.world.ICube;
import io.github.opencubicchunks.cubicchunks.api.worldgen.CubePrimer;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.core.world.cube.Cube;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.generator.GenericChunkGenerator;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CubeGeneratorImpl implements ICubeGenerator {
    private final World world;
    private final GenericChunkGenerator generator;

    private Biome[] biomeBuffer = new Biome[256];

    public CubeGeneratorImpl(World world, GenericChunkGenerator generator) {
        this.world = world;
        this.generator = generator;
    }

    @Override
    public CubePrimer generateCube(int x, int y, int z) {
        CubePrimer primer = new CubePrimer();

        CubicPos pos = new CubicPos(x, y, z);
        this.generator.primeChunk(pos, new CubePrimeWriterImpl(primer));

        return primer;
    }

    @Override
    public void generateColumn(Chunk column) {
        this.biomeBuffer = this.world.getBiomeProvider().getBiomes(this.biomeBuffer, column.x << 4, column.z << 4, 16, 16);
        byte[] biomeArray = column.getBiomeArray();
        for (int i = 0; i < this.biomeBuffer.length; i++) {
            biomeArray[i] = (byte) Biome.getIdForBiome(this.biomeBuffer[i]);
        }

        this.generator.generateColumn(column);
    }

    @Override
    public void populate(ICube cube) {
        if (cube instanceof Cube) {
            ((Cube) cube).setPopulated(true);
        }

        CubicPos pos = new CubicPos(cube.getX(), cube.getY(), cube.getZ());
        this.generator.populateChunk(pos, new CubePopulationWriterImpl(this.world, pos));
    }

    @Override
    public Box getFullPopulationRequirements(ICube cube) {
        return RECOMMENDED_FULL_POPULATOR_REQUIREMENT;
    }

    @Override
    public Box getPopulationPregenerationRequirements(ICube cube) {
        return RECOMMENDED_GENERATE_POPULATOR_REQUIREMENT;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
        return this.generator.getPossibleCreatures(type, pos);
    }

    @Override
    public void recreateStructures(ICube cube) {
        this.generator.prepareStructures(new CubicPos(cube.getX(), cube.getY(), cube.getZ()));
    }

    @Override
    public void recreateStructures(Chunk column) {
    }

    @Nullable
    @Override
    public BlockPos getClosestStructure(String name, BlockPos pos, boolean findUnexplored) {
        return this.generator.getClosestStructure(name, pos, findUnexplored);
    }

    public GenericChunkGenerator getInner() {
        return this.generator;
    }
}
