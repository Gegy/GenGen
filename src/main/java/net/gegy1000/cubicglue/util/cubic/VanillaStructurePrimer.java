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
package net.gegy1000.cubicglue.util.cubic;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.ChunkPrimeWriter;
import net.gegy1000.cubicglue.api.CubicChunkPrimer;
import net.gegy1000.cubicglue.util.CubicPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * Basic structure generator for Cubic Chunks.
 * <p>
 * The basic idea is to loop over all cubes within some radius (max structure size) and figure out which parts of
 * structures starting there intersect currently generated cube.
 *
 * Adapted from https://github.com/OpenCubicChunks/CubicWorldGen
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class VanillaStructurePrimer implements CubicChunkPrimer {

    /** The number of Chunks to gen-check in any given direction. */
    protected int range = 8;

    /** The RNG used by the MapGen classes. */
    @Nonnull protected Random rand = new Random();

    /** This world object. */
    protected World world;

    /** The minimum spacing of structures. */
    protected final int spacing;

    /**
     * @param world The world we will be generating in
     * @param spacing The minimum spacing. Structures that aren't generated at integer multiple coords of this value will be skipped.
     */
    protected VanillaStructurePrimer(World world, int spacing) {
        this.world = world;
        this.spacing = spacing;
    }

    @Override
    public void prime(CubicPos pos, ChunkPrimeWriter writer) {

        this.rand.setSeed(world.getSeed());
        //used to randomize contribution of each coordinate to the cube seed
        //without these swapping x/y/z coordinates would result in the same seed
        //so structures would generate symmetrically
        long randXMul = this.rand.nextLong();
        long randYMul = this.rand.nextLong();
        long randZMul = this.rand.nextLong();

        // as an optimization, this structure looks for structures only in every second coordinate on each axis
        // ensure all origin points are always odd (could also be even, that would be & ~1),
        // this way positions used as origin position are consistent across chunks
        // increase scan radius by 1 because `|1` introduces offset to even X/Y/Z coords
        int radius = this.range + 1;
        int cubeXOriginBase = pos.getX() | 1;
        int cubeYOriginBase = pos.getY() | 1;
        int cubeZOriginBase = pos.getZ() | 1;

        long randSeed = world.getSeed();

        int spacing = this.spacing;
        //x/y/zOrigin is location of the structure "center", and cubeX/Y/Z is the currently generated cube
        for (int xOrigin = cubeXOriginBase - radius; xOrigin <= cubeXOriginBase + radius; xOrigin += spacing) {
            long randX = xOrigin * randXMul ^ randSeed;
            for (int yOrigin = cubeYOriginBase - radius; yOrigin <= cubeYOriginBase + radius; yOrigin += spacing) {
                long randY = yOrigin * randYMul ^ randX;
                for (int zOrigin = cubeZOriginBase - radius; zOrigin <= cubeZOriginBase + radius; zOrigin += spacing) {
                    long randZ = zOrigin * randZMul ^ randY;
                    this.rand.setSeed(randZ);
                    this.generate(writer, xOrigin, yOrigin, zOrigin, pos);
                }
            }

        }
    }

    /**
     * Generates blocks in a given cube for a structure that starts at given origin position.
     *
     * @param writer the block buffer to be filled with blocks (Cube)
     * @param structureX x coordinate of the starting position of currently generated structure
     * @param structureY y coordinate of the starting position of currently generated structure
     * @param structureZ z coordinate of the starting position of currently generated structure
     * @param generatedCubePos position of the cube to fill with blocks
     */
    protected abstract void generate(ChunkPrimeWriter writer,
                                     int structureX, int structureY, int structureZ,
                                     CubicPos generatedCubePos);
}
