package net.gegy1000.cubicglue.api;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.cubicglue.CubicGlue;
import net.gegy1000.cubicglue.GluedColumnWorldType;
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
public interface CubicWorldType {
    @Nullable
    static CubicWorldType unwrap(WorldType worldType) {
        if (worldType instanceof GluedColumnWorldType) {
            return ((GluedColumnWorldType) worldType).getInner();
        }
        return null;
    }

    default WorldType create() {
        return CubicGlue.get().createWorldType(this);
    }

    String getName();

    CubicChunkGenerator createGenerator(World world);

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
    default void onCustomize(Minecraft client, GuiCreateWorld parent) {
    }

    default boolean isCustomizable() {
        return false;
    }

    default float getCloudHeight() {
        return 128.0F;
    }
}
