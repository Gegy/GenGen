package net.gegy1000.cubicglue.util;

import net.gegy1000.cubicglue.api.ChunkPopulationWriter;
import net.gegy1000.cubicglue.util.wrapper.BiomeDecorationWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class VanillaBiomeDecorator {
    private static final Random RANDOM = new Random(0);

    public static void decorate(CubicPos pos, ChunkPopulationWriter writer, Biome biome) {
        long worldSeed = writer.getGlobal().getSeed();
        RANDOM.setSeed(worldSeed);

        long seedX = RANDOM.nextLong() / 2 * 2 + 1;
        long seedZ = RANDOM.nextLong() / 2 * 2 + 1;
        RANDOM.setSeed(pos.getX() * seedX + pos.getZ() * seedZ ^ worldSeed);

        BiomeDecorationWorld wrappedWorld = new BiomeDecorationWorld(writer.getGlobal(), pos);
        biome.decorate(wrappedWorld, RANDOM, new BlockPos(pos.getX(), 0, pos.getZ()));
    }
}
