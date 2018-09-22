package net.gegy1000.cubicglue;

import io.github.opencubicchunks.cubicchunks.api.worldgen.ICubeGenerator;
import io.github.opencubicchunks.cubicchunks.api.worldgen.populator.WorldGenEntitySpawner;
import io.github.opencubicchunks.cubicchunks.core.server.CubeProviderServer;
import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.api.ChunkPopulationWriter;
import net.gegy1000.cubicglue.api.ColumnGenerator;
import net.gegy1000.cubicglue.api.CubicChunkGenerator;
import net.gegy1000.cubicglue.api.CubicWorldType;
import net.gegy1000.cubicglue.util.CubicPos;
import net.gegy1000.cubicglue.util.GluedEventDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CubicGlue {
    public static final Logger LOGGER = LogManager.getLogger(CubicGlue.class);

    private static Field isCubicField;

    static {
        try {
            isCubicField = ReflectionHelper.findField(World.class, "isCubicWorld");
            isCubicField.setAccessible(true);
        } catch (ReflectionHelper.UnableToFindFieldException e) {
            // Cubic Chunks isn't installed
        }
    }

    private static final Map<World, WorldProxy> WORLD_PROXY = new WeakHashMap<>();

    private static Proxy proxy;

    public static Proxy proxy() {
        if (proxy == null) {
            proxy = Loader.isModLoaded("cubicchunks") ? new Present() : new Absent();
        }
        return proxy;
    }

    public static WorldProxy proxy(World world) {
        return WORLD_PROXY.computeIfAbsent(world, w -> isCubic(w) ? new WorldPresent() : new WorldAbsent());
    }

    public static GluedEventDispatcher events(World world) {
        return proxy(world).getEventDispatcher();
    }

    public static boolean isCubic(World world) {
        if (isCubicField == null) {
            return false;
        }
        try {
            return isCubicField.getBoolean(world);
        } catch (IllegalAccessException e) {
            LOGGER.warn("Failed to check isCubicWorld field", e);
            return false;
        }
    }

    public interface Proxy {
        WorldType createWorldType(CubicWorldType worldType);
    }

    public static class Present implements Proxy {
        @Override
        public WorldType createWorldType(CubicWorldType worldType) {
            return new GluedCubicWorldType(worldType);
        }
    }

    public static class Absent implements Proxy {
        @Override
        public WorldType createWorldType(CubicWorldType worldType) {
            return new GluedColumnWorldType(worldType);
        }
    }

    public interface WorldProxy {
        GluedEventDispatcher getEventDispatcher();

        @Nullable
        CubicChunkGenerator unwrapChunkGenerator(World world);

        @Nullable
        ColumnGenerator unwrapColumnGenerator(World world);

        void spawnEntities(CubicPos pos, ChunkPopulationWriter writer, Random random);
    }

    public static class WorldPresent implements WorldProxy {
        private final GluedEventDispatcher dispatcher = new GluedEventDispatcher.Cubic();

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

        @Override
        public void spawnEntities(CubicPos pos, ChunkPopulationWriter writer, Random random) {
            this.getEventDispatcher().spawnEntities(pos, writer, () -> {
                World world = writer.getGlobal();
                if (world instanceof WorldServer) {
                    BlockPos origin = pos.getCenter();
                    Biome biome = writer.getCenterBiome();
                    WorldGenEntitySpawner.initialWorldGenSpawn((WorldServer) world, biome, origin.getX(), origin.getY(), origin.getZ(), 16, 16, 16, random);
                }
            });
        }
    }

    public static class WorldAbsent implements WorldProxy {
        private final GluedEventDispatcher dispatcher = new GluedEventDispatcher.Vanilla();

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

        @Override
        public void spawnEntities(CubicPos pos, ChunkPopulationWriter writer, Random random) {
            World world = writer.getGlobal();
            Biome biome = writer.getCenterBiome();
            this.getEventDispatcher().spawnEntities(pos, writer, () -> {
                int centerX = pos.getCenterX();
                int centerZ = pos.getCenterZ();
                WorldEntitySpawner.performWorldGenSpawning(world, biome, centerX, centerZ, 16, 16, random);
            });
        }
    }
}
