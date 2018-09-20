package net.gegy1000.cubicglue;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.CubicChunkGenerator;
import net.gegy1000.cubicglue.primer.GluedColumnPopulationWriter;
import net.gegy1000.cubicglue.primer.GluedColumnPrimeWriter;
import net.gegy1000.cubicglue.util.CubicPos;
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
import java.util.Collections;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GluedColumnGenerator implements IChunkGenerator {
    private final World world;
    private final CubicChunkGenerator generator;

    private Biome[] biomeBuffer = new Biome[256];

    public GluedColumnGenerator(World world, CubicChunkGenerator generator) {
        this.world = world;
        this.generator = generator;
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        ChunkPrimer primer = new ChunkPrimer();

        for (int y = 0; y < 16; y++) {
            CubicPos pos = new CubicPos(x, y, z);
            this.generator.prime(pos, new GluedColumnPrimeWriter(primer, pos));
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
        CubicGlue.events().populate(this.world, new CubicPos(x, 0, z), () -> {
            for (int y = 0; y < 16; y++) {
                CubicPos pos = new CubicPos(x, y, z);
                this.generator.populate(pos, new GluedColumnPopulationWriter(this.world, pos));
            }
        });
    }

    @Override
    public boolean generateStructures(Chunk chunk, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World world, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public void recreateStructures(Chunk chunk, int x, int z) {
    }

    @Override
    public boolean isInsideStructure(World world, String structureName, BlockPos pos) {
        return false;
    }

    public CubicChunkGenerator getInner() {
        return this.generator;
    }
}
