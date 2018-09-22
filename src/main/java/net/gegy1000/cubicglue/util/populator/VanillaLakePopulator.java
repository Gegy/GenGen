package net.gegy1000.cubicglue.util.populator;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.CubicGlue;
import net.gegy1000.cubicglue.api.ChunkPopulationWriter;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VanillaLakePopulator extends VanillaChunkPopulator {
    private final WorldGenLakes generator = new WorldGenLakes(Blocks.WATER);

    public VanillaLakePopulator(World world, long seed) {
        super(world, seed);
    }

    @Override
    protected void populate(CubicPos pos, ChunkPopulationWriter writer, Biome biome) {
        if (biome != Biomes.DESERT && this.horizontalRandom.nextInt(16) == 0) {
            CubicGlue.events(writer.getGlobal()).populateFeature(pos, writer, PopulateChunkEvent.Populate.EventType.LAKE, () -> {
                int offsetX = this.random.nextInt(16);
                int offsetY = this.random.nextInt(16);
                int offsetZ = this.random.nextInt(16);

                BlockPos origin = pos.getCenter().add(offsetX, offsetY, offsetZ);
                this.generator.generate(writer.getGlobal(), this.random, origin);
            });
        }
    }
}
