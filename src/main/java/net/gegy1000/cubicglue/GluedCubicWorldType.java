package net.gegy1000.cubicglue;

import io.github.opencubicchunks.cubicchunks.api.util.IntRange;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldType;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.CubicChunkGenerator;
import net.gegy1000.cubicglue.api.CubicWorldType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GluedCubicWorldType extends GluedColumnWorldType implements ICubicWorldType {
    public GluedCubicWorldType(CubicWorldType worldType) {
        super(worldType);
    }

    @Nullable
    @Override
    public ICubeGenerator createCubeGenerator(World world) {
        CubicChunkGenerator generator = this.worldType.createGenerator(world);
        return new GluedCubeGenerator(world, generator);
    }

    @Override
    public IntRange calculateGenerationHeightRange(WorldServer world) {
        return new IntRange(0, this.worldType.calculateMaxGenerationHeight(world));
    }

    @Override
    public boolean hasCubicGeneratorForWorld(World world) {
        return true;
    }
}
