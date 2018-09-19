package net.gegy1000.cubicglue.api;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.CubicGlue;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface CubicWorldType {
    String getName();

    CubicChunkGenerator createGenerator(World world);

    BiomeProvider createBiomeProvider(World world);

    default WorldType create() {
        return CubicGlue.get().createWorldType(this);
    }
}
