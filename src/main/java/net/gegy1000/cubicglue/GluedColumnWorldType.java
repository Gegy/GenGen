package net.gegy1000.cubicglue;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.CubicChunkGenerator;
import net.gegy1000.cubicglue.api.CubicWorldType;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GluedColumnWorldType extends WorldType {
    protected final CubicWorldType worldType;

    public GluedColumnWorldType(CubicWorldType worldType) {
        super(worldType.getName());
        this.worldType = worldType;
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world, String generationSettings) {
        CubicChunkGenerator generator = this.worldType.createGenerator(world);
        return new GluedColumnGenerator(world, generator);
    }

    @Override
    public BiomeProvider getBiomeProvider(World world) {
        return this.worldType.createBiomeProvider(world);
    }
}
