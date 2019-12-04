package net.gegy1000.gengen.util.primer;

import com.google.common.base.MoreObjects;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.writer.ChunkPrimeWriter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GenericCavePrimer extends GenericStructurePrimer {
    private static final int SPAWN_CHANCE = 16 * 7 / (2 * 2 * 2);

    private static final IBlockState AIR = Blocks.AIR.getDefaultState();

    public GenericCavePrimer(World world) {
        super(world, 2);
    }

    @Override
    protected void generate(ChunkPrimeWriter writer, int cubeXOrigin, int cubeYOrigin, int cubeZOrigin, CubicPos generatedCubePos) {
        int nodeCount = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);
        if (this.rand.nextInt(SPAWN_CHANCE) != 0) {
            return;
        }

        for (int node = 0; node < nodeCount; node++) {
            double nodeOriginX = (cubeXOrigin << 4) + this.rand.nextInt(16);
            double nodeOriginY = (cubeYOrigin << 4) + this.rand.nextInt(16);
            double nodeOriginZ = (cubeZOrigin << 4) + this.rand.nextInt(16);

            int branchCount = 1;
            if (this.rand.nextInt(4) == 0) {
                this.addRoom(this.rand.nextLong(), generatedCubePos, writer, nodeOriginX, nodeOriginY, nodeOriginZ);
                branchCount += this.rand.nextInt(4);
            }

            for (int branch = 0; branch < branchCount; ++branch) {
                float horizontalAngle = (float) (this.rand.nextFloat() * (Math.PI * 2.0F));
                float verticalAngle = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float radius = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(10) == 0) {
                    radius *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                }

                this.addTunnel(this.rand.nextLong(), generatedCubePos, writer, nodeOriginX, nodeOriginY, nodeOriginZ, radius, horizontalAngle, verticalAngle, 0, 0, 1.0);
            }
        }
    }

    protected void addRoom(long seed, CubicPos generatedCubePos, ChunkPrimeWriter writer, double nodeOriginX, double nodeOriginY, double nodeOriginZ) {
        this.addTunnel(seed, generatedCubePos, writer, nodeOriginX, nodeOriginY, nodeOriginZ, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5);
    }

    protected void addTunnel(long seed, CubicPos generatedCubePos, ChunkPrimeWriter writer, double nodeOriginX, double nodeOriginY, double nodeOriginZ, float radius, float horizontalAngle, float verticalAngle, int currentStep, int stepCount, double flatnessFactor) {
        double centerCubeX = generatedCubePos.getCenterX();
        double centerCubeY = generatedCubePos.getCenterY();
        double centerCubeZ = generatedCubePos.getCenterZ();

        float deltaHorizontalAngle = 0.0F;
        float deltaVerticalAngle = 0.0F;
        Random random = new Random(seed);

        if (stepCount <= 0) {
            int i = (this.range - 1) << 4;
            stepCount = i - random.nextInt(i / 4);
        }

        boolean lastStep = false;
        if (currentStep == -1) {
            currentStep = stepCount / 2;
            lastStep = true;
        }

        boolean steepStep = random.nextInt(6) == 0;
        int splitStep = random.nextInt(stepCount / 2) + stepCount / 4;

        while (currentStep < stepCount) {
            double radiusHorizontal = 1.5 + MathHelper.sin((float) (currentStep * Math.PI / stepCount)) * radius;
            double radiusVertical = radiusHorizontal * flatnessFactor;

            float displacementHorizontalFactor = MathHelper.cos(verticalAngle);
            float displacementVerticalFactor = MathHelper.sin(verticalAngle);

            nodeOriginX += MathHelper.cos(horizontalAngle) * displacementHorizontalFactor;
            nodeOriginY += displacementVerticalFactor;
            nodeOriginZ += MathHelper.sin(horizontalAngle) * displacementHorizontalFactor;

            if (steepStep) {
                verticalAngle = verticalAngle * 0.92F;
            } else {
                verticalAngle = verticalAngle * 0.7F;
            }

            verticalAngle = verticalAngle + deltaVerticalAngle * 0.1F;
            horizontalAngle += deltaHorizontalAngle * 0.1F;

            deltaVerticalAngle = deltaVerticalAngle * 0.9F;
            deltaHorizontalAngle = deltaHorizontalAngle * 0.75F;
            deltaVerticalAngle = deltaVerticalAngle + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            deltaHorizontalAngle = deltaHorizontalAngle + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!lastStep && currentStep == splitStep && radius > 1.0F && stepCount > 0) {
                this.addTunnel(random.nextLong(), generatedCubePos, writer, nodeOriginX, nodeOriginY, nodeOriginZ, random.nextFloat() * 0.5F + 0.5F, horizontalAngle - (float) (Math.PI / 2.0F), verticalAngle / 3.0F, currentStep, stepCount, 1.0);
                this.addTunnel(random.nextLong(), generatedCubePos, writer, nodeOriginX, nodeOriginY, nodeOriginZ, random.nextFloat() * 0.5F + 0.5F, horizontalAngle + (float) (Math.PI / 2.0F), verticalAngle / 3.0F, currentStep, stepCount, 1.0);
                return;
            }

            if (lastStep || random.nextInt(4) != 0) {
                double deltaX = nodeOriginX - centerCubeX;
                double deltaY = nodeOriginY - centerCubeY;
                double deltaZ = nodeOriginZ - centerCubeZ;
                double stepDistReduction = stepCount - currentStep;
                double maxDistance = radius + 2.0F + 16.0F;

                double distanceSq = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ - stepDistReduction * stepDistReduction;
                if (distanceSq > maxDistance * maxDistance) {
                    return;
                }

                if (nodeOriginX >= centerCubeX - 16.0 - radiusHorizontal * 2.0
                        && nodeOriginY >= centerCubeY - 16.0 - radiusVertical * 2.0
                        && nodeOriginZ >= centerCubeZ - 16.0 - radiusHorizontal * 2.0
                        && nodeOriginX <= centerCubeX + 16.0 + radiusHorizontal * 2.0
                        && nodeOriginY <= centerCubeY + 16.0 + radiusVertical * 2.0
                        && nodeOriginZ <= centerCubeZ + 16.0 + radiusHorizontal * 2.0
                ) {
                    int minX = MathHelper.floor(nodeOriginX - radiusHorizontal) - generatedCubePos.getMinX() - 1;
                    int maxX = MathHelper.floor(nodeOriginX + radiusHorizontal) - generatedCubePos.getMinX() + 1;
                    int minY = MathHelper.floor(nodeOriginY - radiusVertical) - generatedCubePos.getMinY() - 1;
                    int maxY = MathHelper.floor(nodeOriginY + radiusVertical) + generatedCubePos.getMinY() + 1;
                    int minZ = MathHelper.floor(nodeOriginZ - radiusHorizontal) - generatedCubePos.getMinZ() - 1;
                    int maxZ = MathHelper.floor(nodeOriginZ + radiusHorizontal) - generatedCubePos.getMinZ() + 1;

                    if (minX < 0) minX = 0;
                    if (maxX > 16) maxX = 16;
                    if (minY < 0) minY = 0;
                    if (maxY > 16) maxY = 16;
                    if (minZ < 0) minZ = 0;
                    if (maxZ > 16) maxZ = 16;

                    if (!this.checkOceanic(writer, minX, maxX, minY, maxY, minZ, maxZ)) {
                        this.carveStep(generatedCubePos, writer, nodeOriginX, nodeOriginY, nodeOriginZ, radiusHorizontal, radiusVertical, minX, maxX, minY, maxY, minZ, maxZ);
                        if (lastStep) break;
                    }
                }
            }

            currentStep++;
        }
    }

    private void carveStep(CubicPos generatedCubePos, ChunkPrimeWriter writer, double nodeOriginX, double nodeOriginY, double nodeOriginZ, double radiusHorizontal, double radiusVertical, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        for (int x = minX; x < maxX; x++) {
            double deltaX = ((x + generatedCubePos.getMinX()) + 0.5 - nodeOriginX) / radiusHorizontal;

            for (int z = minZ; z < maxZ; z++) {
                double deltaZ = ((z + generatedCubePos.getMinZ()) + 0.5 - nodeOriginZ) / radiusHorizontal;
                boolean brokeSurface = false;

                if (deltaX * deltaX + deltaZ * deltaZ < 1.0) {
                    for (int y = maxY; y > minY; y--) {
                        double deltaY = ((y - 1) + 0.5 - nodeOriginY) / radiusVertical;

                        if (deltaY > -0.7 && deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ < 1.0) {
                            IBlockState state = writer.get(x, y, z);
                            IBlockState aboveState = MoreObjects.firstNonNull(writer.get(x, y + 1, z), AIR);

                            BlockPos pos = new BlockPos(x + generatedCubePos.getMinX(), generatedCubePos.getMinY() + y, z + generatedCubePos.getMinZ());
                            Biome biome = this.world.getBiome(pos);
                            if (this.isTopBlock(writer, x, y, z, biome)) {
                                brokeSurface = true;
                            }

                            this.digBlock(writer, x, y, z, biome, brokeSurface, state, aboveState);
                        }
                    }
                }
            }
        }
    }

    private boolean checkOceanic(ChunkPrimeWriter writer, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                for (int y = maxY; y >= minY; y--) {
                    if (this.isOceanBlock(writer, x, y, z)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canReplaceBlock(IBlockState state, IBlockState up) {
        Block block = state.getBlock();
        return block == Blocks.STONE
                || block == Blocks.DIRT
                || block == Blocks.GRASS
                || block == Blocks.HARDENED_CLAY
                || block == Blocks.STAINED_HARDENED_CLAY
                || block == Blocks.SANDSTONE
                || block == Blocks.RED_SANDSTONE
                || block == Blocks.MYCELIUM
                || block == Blocks.SNOW_LAYER
                || (block == Blocks.SAND || block == Blocks.GRAVEL) && up.getMaterial() != Material.WATER;
    }

    private boolean isOceanBlock(ChunkPrimeWriter writer, int x, int y, int z) {
        Block block = writer.get(x, y, z).getBlock();
        return block == Blocks.FLOWING_WATER || block == Blocks.WATER;
    }

    private boolean isTopBlock(ChunkPrimeWriter writer, int x, int y, int z, Biome biome) {
        IBlockState state = writer.get(x, y, z);
        return (this.isExceptionBiome(biome) ? state.getBlock() == Blocks.GRASS : state.getBlock() == biome.topBlock);
    }

    private boolean isExceptionBiome(Biome biome) {
        return biome == Biomes.BEACH || biome == Biomes.DESERT;
    }

    private void digBlock(ChunkPrimeWriter writer, int x, int y, int z, Biome biome, boolean brokeSurface, IBlockState state, IBlockState up) {
        IBlockState top = biome.topBlock;
        IBlockState filler = biome.fillerBlock;

        if (this.canReplaceBlock(state, up) || state.getBlock() == top.getBlock() || state.getBlock() == filler.getBlock()) {
            writer.set(x, y, z, AIR);
            if (brokeSurface && writer.get(x, y - 1, z).getBlock() == filler.getBlock()) {
                writer.set(x, y - 1, z, top.getBlock().getDefaultState());
            }
        }
    }
}
