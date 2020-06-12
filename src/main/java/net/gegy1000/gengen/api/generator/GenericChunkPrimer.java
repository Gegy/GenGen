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
package net.gegy1000.gengen.api.generator;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.writer.ChunkPrimeWriter;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface GenericChunkPrimer {
    void primeChunk(CubicPos pos, ChunkPrimeWriter writer);

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
    default void primeStructure(
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

    @FunctionalInterface
    interface Handler {
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
