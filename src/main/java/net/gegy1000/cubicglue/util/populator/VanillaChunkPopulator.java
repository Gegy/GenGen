package net.gegy1000.cubicglue.util.populator;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.ChunkPopulationWriter;
import net.gegy1000.cubicglue.api.CubicChunkPopulator;
import net.gegy1000.cubicglue.util.CubicPos;
import net.gegy1000.cubicglue.util.PseudoRandomMap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class VanillaChunkPopulator implements CubicChunkPopulator {
    protected final PseudoRandomMap randomMap;

    protected final Random random = new Random(0);
    protected final Random horizontalRandom = new Random(0);

    protected VanillaChunkPopulator(World world, long seed) {
        this.randomMap = new PseudoRandomMap(world, seed);
    }

    @Override
    public final void populate(CubicPos pos, ChunkPopulationWriter writer) {
        int globalX = pos.getMinX();
        int globalZ = pos.getMinZ();

        this.randomMap.initPosSeed(globalX, globalZ);
        this.horizontalRandom.setSeed(this.randomMap.next());

        this.randomMap.initPosSeed(globalX, pos.getMinY(), globalZ);
        this.random.setSeed(this.randomMap.next());

        Biome biome = writer.getCenterBiome();
        this.populate(pos, writer, biome);
    }

    protected abstract void populate(CubicPos pos, ChunkPopulationWriter writer, Biome biome);
}
