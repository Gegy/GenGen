package net.gegy1000.cubicglue;

import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.core.server.CubeProviderServer;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.ColumnGenerator;
import net.gegy1000.cubicglue.api.CubicChunkGenerator;
import net.gegy1000.cubicglue.api.CubicWorldType;
import net.gegy1000.cubicglue.util.GluedEventDispatcher;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CubicGlue {
    public static final Logger LOGGER = LogManager.getLogger(CubicGlue.class);

    private static Proxy proxy;

    public static Proxy proxy() {
        if (proxy == null) {
            proxy = Loader.isModLoaded("cubicchunks") ? new Present() : new Absent();
        }
        return proxy;
    }

    public static GluedEventDispatcher events() {
        return proxy.getEventDispatcher();
    }

    public interface Proxy {
        WorldType createWorldType(CubicWorldType worldType);

        GluedEventDispatcher getEventDispatcher();

        @Nullable
        CubicChunkGenerator unwrapChunkGenerator(World world);

        @Nullable
        ColumnGenerator unwrapColumnGenerator(World world);
    }

    public static class Present implements Proxy {
        private final GluedEventDispatcher dispatcher = new GluedEventDispatcher.Cubic();

        @Override
        public WorldType createWorldType(CubicWorldType worldType) {
            return new GluedCubicWorldType(worldType);
        }

        @Override
        public GluedEventDispatcher getEventDispatcher() {
            return this.dispatcher;
        }

        @Nullable
        @Override
        public CubicChunkGenerator unwrapChunkGenerator(World world) {
            IChunkProvider provider = world.getChunkProvider();
            if (provider instanceof CubeProviderServer) {
                ICubeGenerator generator = ((CubeProviderServer) provider).getCubeGenerator();
                return CubicChunkGenerator.unwrap(generator);
            } else if (provider instanceof ChunkProviderServer) {
                IChunkGenerator generator = ((ChunkProviderServer) provider).chunkGenerator;
                return CubicChunkGenerator.unwrap(generator);
            }
            return null;
        }

        @Nullable
        @Override
        public ColumnGenerator unwrapColumnGenerator(World world) {
            IChunkProvider provider = world.getChunkProvider();
            if (provider instanceof CubeProviderServer) {
                ICubeGenerator generator = ((CubeProviderServer) provider).getCubeGenerator();
                if (generator instanceof ColumnGenerator) {
                    return (ColumnGenerator) generator;
                }
            } else if (provider instanceof ChunkProviderServer) {
                IChunkGenerator generator = ((ChunkProviderServer) provider).chunkGenerator;
                if (generator instanceof ColumnGenerator) {
                    return (ColumnGenerator) generator;
                }
            }
            return null;
        }
    }

    public static class Absent implements Proxy {
        private final GluedEventDispatcher dispatcher = new GluedEventDispatcher.Vanilla();

        @Override
        public WorldType createWorldType(CubicWorldType worldType) {
            return new GluedColumnWorldType(worldType);
        }

        @Override
        public GluedEventDispatcher getEventDispatcher() {
            return this.dispatcher;
        }

        @Nullable
        @Override
        public CubicChunkGenerator unwrapChunkGenerator(World world) {
            IChunkProvider provider = world.getChunkProvider();
            if (provider instanceof ChunkProviderServer) {
                IChunkGenerator generator = ((ChunkProviderServer) provider).chunkGenerator;
                return CubicChunkGenerator.unwrap(generator);
            }
            return null;
        }

        @Nullable
        @Override
        public ColumnGenerator unwrapColumnGenerator(World world) {
            IChunkProvider provider = world.getChunkProvider();
            if (provider instanceof ChunkProviderServer) {
                IChunkGenerator generator = ((ChunkProviderServer) provider).chunkGenerator;
                if (generator instanceof ColumnGenerator) {
                    return (ColumnGenerator) generator;
                }
            }
            return null;
        }
    }
}
