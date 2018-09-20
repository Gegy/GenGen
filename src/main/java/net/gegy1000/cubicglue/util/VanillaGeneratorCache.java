package net.gegy1000.cubicglue.util;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.WeakHashMap;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VanillaGeneratorCache {
    private static final Map<World, IChunkGenerator> CACHE = new WeakHashMap<>();

    @Nullable
    public static IChunkGenerator getGenerator(World world) {
        return CACHE.computeIfAbsent(world, VanillaGeneratorCache::lookupGenerator);
    }

    @Nullable
    private static IChunkGenerator lookupGenerator(World world) {
        IChunkProvider provider = world.getChunkProvider();
        if (provider instanceof ChunkProviderServer) {
            return ((ChunkProviderServer) provider).chunkGenerator;
        }
        return null;
    }
}
