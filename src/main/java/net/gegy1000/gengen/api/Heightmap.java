package net.gegy1000.gengen.api;

import com.google.common.base.Preconditions;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class Heightmap implements HeightFunction {
    public static final int SIZE = 16;

    private final int[] buffer;

    private Heightmap(int[] buffer) {
        this.buffer = buffer;
    }

    public static Heightmap wrap(int[] buffer) {
        Preconditions.checkArgument(buffer.length == SIZE * SIZE, "invalid buffer size");
        return new Heightmap(buffer);
    }

    public static Heightmap create(Init init) {
        int[] buffer = new int[SIZE * SIZE];
        for (int z = 0; z < SIZE; z++) {
            for (int x = 0; x < SIZE; x++) {
                buffer[indexUnchecked(x, z)] = init.get(x, z);
            }
        }
        return new Heightmap(buffer);
    }

    public int get(int x, int z) {
        return this.buffer[index(x, z)];
    }

    public int getUnchecked(int x, int z) {
        return this.buffer[indexUnchecked(x, z)];
    }

    private static int index(int x, int z) {
        if (x < 0 || z < 0 || x >= SIZE || z >= SIZE) throw new IndexOutOfBoundsException();
        return x + z * SIZE;
    }

    private static int indexUnchecked(int x, int z) {
        return x + z * SIZE;
    }

    public int[] getBuffer() {
        return this.buffer;
    }

    @Override
    public int apply(int x, int z) {
        return this.get(x, z);
    }

    public interface Init {
        int get(int x, int z);
    }
}
