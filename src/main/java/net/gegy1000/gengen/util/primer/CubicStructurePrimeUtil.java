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
import net.gegy1000.gengen.api.writer.ChunkPrimeWriter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

/**
 * Adapted from https://github.com/OpenCubicChunks/CubicWorldGen
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CubicStructurePrimeUtil {

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
}
