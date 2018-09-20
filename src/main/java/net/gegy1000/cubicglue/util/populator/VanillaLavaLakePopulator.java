package net.gegy1000.cubicglue.util.populator;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.CubicGlue;
import net.gegy1000.cubicglue.api.ChunkPopulationWriter;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VanillaLavaLakePopulator extends VanillaChunkPopulator {
    private final WorldGenLakes generator = new WorldGenLakes(Blocks.LAVA);

    public VanillaLavaLakePopulator(World world, long seed) {
        super(world, seed);
    }

    @Override
    protected void populate(CubicPos pos, ChunkPopulationWriter writer, Biome biome) {
        if (this.horizontalRandom.nextInt(8) == 0) {
            int offsetY = this.random.nextInt(this.random.nextInt(writer.getGlobal().getHeight() - 8) + 8);

            if (offsetY >= pos.getMinY() && offsetY <= pos.getMaxY()) {
                CubicGlue.events().populateFeature(pos, writer, PopulateChunkEvent.Populate.EventType.LAVA, () -> {
                    int offsetX = this.random.nextInt(16);
                    int offsetZ = this.random.nextInt(16);

                    if (offsetY < writer.getGlobal().getSeaLevel() || this.random.nextInt(10) == 0) {
                        BlockPos origin = pos.getCenter();
                        this.generator.generate(writer.getGlobal(), this.random, origin.add(offsetX, offsetY, offsetZ));
                    }
                });
            }
        }
    }
}
