package net.gegy1000.gengen.api;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.generator.GenericChunkGenerator;
import net.gegy1000.gengen.core.GenGen;
import net.gegy1000.gengen.core.impl.vanilla.ColumnWorldTypeImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface GenericWorldType {
    @Nullable
    static GenericWorldType unwrap(WorldType worldType) {
        if (worldType instanceof ColumnWorldTypeImpl) {
            return ((ColumnWorldTypeImpl) worldType).getInner();
        }
        return null;
    }

    @Nullable
    static <T extends GenericWorldType> T unwrapAs(WorldType worldType, Class<T> type) {
        GenericWorldType genericType = unwrap(worldType);
        if (type.isInstance(genericType)) {
            return type.cast(genericType);
        }
        return null;
    }

    default WorldType create() {
        return GenGen.proxy().createWorldType(this);
    }

    String getName();

    GenericChunkGenerator createGenerator(World world);

    BiomeProvider createBiomeProvider(World world);

    default int calculateMaxGenerationHeight(WorldServer world) {
        return Short.MAX_VALUE;
    }

    default double getHorizon(World world) {
        return 63.0;
    }

    default boolean shouldReduceSlimes(World world, Random random) {
        return false;
    }

    default int getSpawnFuzz(WorldServer world, MinecraftServer server) {
        return Math.max(0, server.getSpawnRadius(world));
    }

    @SideOnly(Side.CLIENT)
    default void onCustomize(Minecraft client, WorldType worldType, GuiCreateWorld parent) {
    }

    default boolean isCustomizable() {
        return false;
    }

    default float getCloudHeight() {
        return 128.0F;
    }
}
