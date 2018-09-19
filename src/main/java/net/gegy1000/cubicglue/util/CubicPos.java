package net.gegy1000.cubicglue.util;

import net.minecraft.util.math.BlockPos;

public class CubicPos {
    private final int x;
    private final int y;
    private final int z;

    public CubicPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public int getMinX() {
        return this.x << 4;
    }

    public int getMinY() {
        return this.y << 4;
    }

    public int getMinZ() {
        return this.z << 4;
    }

    public int getMaxX() {
        return (this.x << 4) + 15;
    }

    public int getMaxY() {
        return (this.y << 4) + 15;
    }

    public int getMaxZ() {
        return (this.z << 4) + 15;
    }

    public int getCenterX() {
        return (this.x << 4) + 8;
    }

    public int getCenterY() {
        return (this.y << 4) + 8;
    }

    public int getCenterZ() {
        return (this.z << 4) + 8;
    }

    public BlockPos getCenter() {
        return new BlockPos(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }

    public boolean contains(int x, int y, int z) {
        int minX = this.getMinX();
        int minY = this.getMinY();
        int minZ = this.getMinZ();
        return x >= minX && y >= minY && z >= minZ && x < minX + 16 && y < minY + 16 && z < minZ + 16;
    }
}
