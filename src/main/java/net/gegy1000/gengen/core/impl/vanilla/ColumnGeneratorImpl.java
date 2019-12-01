package net.gegy1000.gengen.core.impl.vanilla;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.GenericChunkGenerator;
import net.gegy1000.gengen.core.GenGen;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ColumnGeneratorImpl implements IChunkGenerator {
    private final World world;
    private final GenericChunkGenerator generator;

    private Biome[] biomeBuffer = new Biome[256];

    public ColumnGeneratorImpl(World world, GenericChunkGenerator generator) {
        this.world = world;
        this.generator = generator;
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        ChunkPrimer primer = new ChunkPrimer();
        for (int y = 0; y < 16; y++) {
            CubicPos pos = new CubicPos(x, y, z);
            this.generator.prime(pos, new ColumnPrimeWriterImpl(primer, pos));
        }

        Chunk chunk = new Chunk(this.world, primer, x, z);
        this.populateBiomes(chunk);

        chunk.generateSkylightMap();

        return chunk;
    }

    private void populateBiomes(Chunk chunk) {
        this.biomeBuffer = this.generator.populateBiomes(new ChunkPos(chunk.x, chunk.z), this.biomeBuffer);
        byte[] biomeArray = chunk.getBiomeArray();
        for (int i = 0; i < this.biomeBuffer.length; i++) {
            biomeArray[i] = (byte) Biome.getIdForBiome(this.biomeBuffer[i]);
        }
    }

    @Override
    public void populate(int x, int z) {
        GenGen.events(this.world).populate(this.world, new CubicPos(x, 0, z), () -> {
            for (int y = 0; y < 16; y++) {
                CubicPos pos = new CubicPos(x, y, z);
                this.generator.populate(pos, new ColumnPopulationWriterImpl(this.world, pos));
            }
        });
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
        return this.generator.getPossibleCreatures(type, pos);
    }

    @Override
    public boolean generateStructures(Chunk chunk, int x, int z) {
        return false;
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World world, String name, BlockPos pos, boolean findUnexplored) {
        return this.generator.getClosestStructure(name, pos, findUnexplored);
    }

    @Override
    public void recreateStructures(Chunk chunk, int x, int z) {
        for (int y = 0; y < 16; y++) {
            this.generator.recreateStructures(new CubicPos(x, y, z));
        }
    }

    @Override
    public boolean isInsideStructure(World world, String name, BlockPos pos) {
        return this.generator.isInsideStructure(name, pos);
    }

    public GenericChunkGenerator getInner() {
        return this.generator;
    }
}
