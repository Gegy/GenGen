package net.gegy1000.gengen.core.impl.vanilla;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.GenericWorldType;
import net.gegy1000.gengen.api.generator.GenericChunkGenerator;
import net.gegy1000.gengen.core.GenGen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ColumnWorldTypeImpl extends WorldType {
    private static Field nameField;

    static {
        try {
            nameField = ReflectionHelper.findField(WorldType.class, "name", "field_77133_f");
            if (nameField != null) {
                nameField.setAccessible(true);
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);
            } else {
                GenGen.LOGGER.error("Failed to find world type name field");
            }
        } catch (ReflectiveOperationException e) {
            GenGen.LOGGER.error("Failed to get world type name field", e);
        }
    }

    protected final GenericWorldType worldType;

    public ColumnWorldTypeImpl(GenericWorldType worldType) {
        super(nameField == null ? worldType.getName() : "lbps");
        this.worldType = worldType;
        setName(this, worldType.getName());
    }

    private static void setName(WorldType worldType, String name) {
        if (nameField != null) {
            try {
                nameField.set(worldType, name);
            } catch (IllegalAccessException e) {
                GenGen.LOGGER.error("Failed to set world type name", e);
            }
        }
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world, String generationSettings) {
        GenericChunkGenerator generator = this.worldType.createGenerator(world);
        return new ColumnGeneratorImpl(world, generator);
    }

    @Override
    public BiomeProvider getBiomeProvider(World world) {
        return this.worldType.createBiomeProvider(world);
    }

    @Override
    public double getHorizon(World world) {
        return this.worldType.getHorizon(world);
    }

    @Override
    public boolean handleSlimeSpawnReduction(Random random, World world) {
        return this.worldType.shouldReduceSlimes(world, random);
    }

    @Override
    public int getSpawnFuzz(WorldServer world, MinecraftServer server) {
        return this.worldType.getSpawnFuzz(world, server);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onCustomizeButton(Minecraft client, GuiCreateWorld parent) {
        this.worldType.onCustomize(client, this, parent);
    }

    @Override
    public boolean isCustomizable() {
        return this.worldType.isCustomizable();
    }

    @Override
    public float getCloudHeight() {
        return this.worldType.getCloudHeight();
    }

    public GenericWorldType getInner() {
        return this.worldType;
    }
}
