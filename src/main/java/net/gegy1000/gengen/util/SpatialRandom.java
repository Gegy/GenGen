package net.gegy1000.gengen.util;

import net.minecraft.world.World;

import java.util.Random;

public class SpatialRandom extends Random {
    private static final long MASK = (1L << 48) - 1;

    private static final long PRIME_1 = 6364136223846793005L & MASK;
    private static final long PRIME_2 = 1442695040888963407L & MASK;

    private final long baseSeed;
    private long seed;

    public SpatialRandom(long worldSeed, long localSeed) {
        super(0);
        this.baseSeed = (worldSeed ^ localSeed) & MASK;
        this.seed = this.baseSeed;
    }

    public SpatialRandom(World world, long localSeed) {
        this(world.getWorldInfo().getSeed(), localSeed);
    }

    public void setSeed(int x, int z) {
        long seed = this.baseSeed;
        for (int i = 0; i < 2; i++) {
            seed *= seed * PRIME_1 + PRIME_2;
            seed += x;
            seed *= seed * PRIME_1 + PRIME_2;
            seed += z;
        }
        this.seed = seed & MASK;
    }

    public void setSeed(int x, int y, int z) {
        long seed = this.baseSeed;
        for (int i = 0; i < 2; i++) {
            seed *= seed * PRIME_1 + PRIME_2;
            seed += x;
            seed *= seed * PRIME_1 + PRIME_2;
            seed += z;
            seed *= seed * PRIME_1 + PRIME_2;
            seed += y;
        }
        this.seed = seed & MASK;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed & MASK;
    }

    @Override
    protected int next(int bits) {
        this.seed = (this.seed * PRIME_1 + PRIME_2) & MASK;
        return (int) (this.seed >>> (48 - bits));
    }
}
