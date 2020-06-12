package net.gegy1000.gengen.api.generator;

import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.core.GenGen;
import net.gegy1000.gengen.core.impl.cubic.CubeGeneratorImpl;
import net.gegy1000.gengen.core.impl.vanilla.ColumnGeneratorImpl;
import net.gegy1000.gengen.core.support.BukkitSupport;
import net.gegy1000.gengen.core.support.SpongeSupport;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface GenericChunkGenerator extends GenericChunkPrimer, GenericChunkPopulator, GenericStructureGenerator {
    @Nullable
    static GenericChunkGenerator unwrap(World world) {
        return GenGen.proxy(world).unwrapChunkGenerator(world);
    }

    @Nullable
    static GenericChunkGenerator unwrap(IChunkGenerator generator) {
        generator = SpongeSupport.unwrapChunkGenerator(generator);
        generator = BukkitSupport.unwrapChunkGenerator(generator);
        if (generator instanceof ColumnGeneratorImpl) {
            return ((ColumnGeneratorImpl) generator).getInner();
        }
        return null;
    }

    @Nullable
    static GenericChunkGenerator unwrap(ICubeGenerator generator) {
        if (generator instanceof CubeGeneratorImpl) {
            return ((CubeGeneratorImpl) generator).getInner();
        }
        return null;
    }

    default void generateColumn(Chunk column) {
    }

    default List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
        return Collections.emptyList();
    }
}
