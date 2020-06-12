package net.gegy1000.gengen.core.impl.cubic;

import io.github.opencubicchunks.cubicchunks.api.util.IntRange;
import io.github.opencubicchunks.cubicchunks.api.world.ICubicWorldType;
import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.GenericWorldType;
import net.gegy1000.gengen.api.generator.GenericChunkGenerator;
import net.gegy1000.gengen.core.impl.vanilla.ColumnWorldTypeImpl;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CubeWorldTypeImpl extends ColumnWorldTypeImpl implements ICubicWorldType {
    public CubeWorldTypeImpl(GenericWorldType worldType) {
        super(worldType);
    }

    @Nullable
    @Override
    public ICubeGenerator createCubeGenerator(World world) {
        GenericChunkGenerator generator = this.worldType.createGenerator(world);
        return new CubeGeneratorImpl(world, generator);
    }

    @Override
    public IntRange calculateGenerationHeightRange(WorldServer world) {
        return new IntRange(this.worldType.getMinGenerationHeight(world), this.worldType.getMaxGenerationHeight(world));
    }

    @Override
    public boolean hasCubicGeneratorForWorld(World world) {
        return true;
    }
}
