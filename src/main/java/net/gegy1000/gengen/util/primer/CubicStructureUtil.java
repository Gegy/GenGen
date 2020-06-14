/*
 *  This file is part of Cubic World Generation, licensed under the MIT License (MIT).
 *
 *  Copyright (c) 2015 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package net.gegy1000.gengen.util.primer;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.writer.ChunkPrimeWriter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Adapted from https://github.com/OpenCubicChunks/CubicWorldGen
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CubicStructureUtil {

    /**
     * Generates structures in given cube, with supplied parameters and handler
     * Adapted from https://github.com/OpenCubicChunks/CubicWorldGen
     *
     * @param world the world we are operating within
     * @param writer the block buffer to be filled with blocks (Cube)
     * @param cubePos position of the cube to generate structures in
     * @param handler generation handler, to generate blocks for a given structure source point, in the specified cube
     * @param range horizontal search distance for structure sources (in cubes)
     * @param spacingBitCount only structure sources on a grid of size 2^spacingBitCount will be considered for generation
     */
    public static void primeStructure(
            World world,
            ChunkPrimeWriter writer, CubicPos cubePos, Handler handler,
            int range, int spacingBitCount
    ) {

        Random rand = new Random(world.getSeed());

        long randXMul = rand.nextLong();
        long randYMul = rand.nextLong();
        long randZMul = rand.nextLong();

        int spacing = 1 << spacingBitCount;
        int spacingBits = spacing - 1;

        // as an optimization, this structure looks for structures only in every Nth coordinate on each axis
        // this ensures that all origin points are always a multiple of 2^bits
        // this way positions used as origin position are consistent across chunks
        // With "| spacingBits" also on radius, the "1" bits cancel out to zero with "basePos - radius"
        // because it's an OR, it can never decrease radius
        int radius = range | spacingBits;
        int cubeXOriginBase = cubePos.getX() | spacingBits;
        int cubeYOriginBase = cubePos.getY() | spacingBits;
        int cubeZOriginBase = cubePos.getZ() | spacingBits;

        long randSeed = world.getSeed();

        //x/y/zOrigin is location of the structure "center", and cubeX/Y/Z is the currently generated cube
        for (int xOrigin = cubeXOriginBase - radius; xOrigin <= cubeXOriginBase + radius; xOrigin += spacing) {
            long randX = xOrigin * randXMul ^ randSeed;
            for (int yOrigin = cubeYOriginBase - radius; yOrigin <= cubeYOriginBase + radius; yOrigin += spacing) {
                long randY = yOrigin * randYMul ^ randX;
                for (int zOrigin = cubeZOriginBase - radius; zOrigin <= cubeZOriginBase + radius; zOrigin += spacing) {
                    long randZ = zOrigin * randZMul ^ randY;
                    rand.setSeed(randZ);
                    handler.generate(rand, writer, xOrigin, yOrigin, zOrigin, cubePos);
                }
            }
        }
    }

    public static boolean scanWallsForBlock(ChunkPrimeWriter writer,
                                            StructureBoundingBox boundingBox,
                                            Predicate<IBlockState> predicate) {
        int minX = boundingBox.minX;
        int minY = boundingBox.minY;
        int minZ = boundingBox.minZ;
        int maxX = boundingBox.maxX;
        int maxY = boundingBox.maxY;
        int maxZ = boundingBox.maxZ;
        // xy planes
        for (int x = minX; x < maxX; ++x) {
            for (int y = minY; y < maxY; ++y) {
                if (predicate.test(writer.get(x, y, minZ)) ||
                        predicate.test(writer.get(x, y, maxZ - 1))) {
                    return true;
                }
            }
        }

        // xz planes
        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                if (predicate.test(writer.get(x, minY, z)) ||
                        predicate.test(writer.get(x, maxY - 1, z))) {
                    return true;
                }
            }
        }

        // yz planes
        for (int y = minY; y < maxY; ++y) {
            for (int z = minZ; z < maxZ; ++z) {
                if (predicate.test(writer.get(minX, y, z)) ||
                        predicate.test(writer.get(maxX - 1, y, z))) {
                    return true;
                }
            }
        }

        return false;
    }

    //Note: it can return negative value. it's not a real distance
    public static double normalizedDistance(int cubeOriginCoord, int localCoord, double structureCoord, double scale) {
        int globalCoord = (cubeOriginCoord << 4) + localCoord;
        return (globalCoord + 0.5D - structureCoord) / scale;
    }

    /**
     * Modifies boundingBox so that max coordinates are less than or equal to 16
     * and min coords are greater than or equal to 0
     */
    public static void clampBoundingBoxToLocalCube(StructureBoundingBox boundingBox) {
        if (boundingBox.minX < 0) {
            boundingBox.minX = 0;
        }
        if (boundingBox.maxX > 16) {
            boundingBox.maxX = 16;
        }
        if (boundingBox.minY < 0) {
            boundingBox.minY = 0;
        }
        if (boundingBox.maxY > 16) {
            boundingBox.maxY = 16;
        }
        if (boundingBox.minZ < 0) {
            boundingBox.minZ = 0;
        }
        if (boundingBox.maxZ > 16) {
            boundingBox.maxZ = 16;
        }
    }

    @FunctionalInterface
    public interface Handler {
        /**
         * Generates blocks in a given cube for a structure that starts at given origin position.
         *
         * @param rand random number generator with seed for the starting position
         * @param writer the block buffer to be filled with blocks (Cube)
         * @param structureX x coordinate of the starting position of currently generated structure
         * @param structureY y coordinate of the starting position of currently generated structure
         * @param structureZ z coordinate of the starting position of currently generated structure
         * @param generatedCubePos position of the cube to fill with blocks
         */
        void generate(Random rand, ChunkPrimeWriter writer,
                             int structureX, int structureY, int structureZ,
                             CubicPos generatedCubePos
        );
    }
}
