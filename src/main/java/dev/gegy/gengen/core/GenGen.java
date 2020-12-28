package dev.gegy.gengen.core;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GenGen {
    public static final Logger LOGGER = LogManager.getLogger(GenGen.class);

    private static Field isCubicField;

    static {
        try {
            isCubicField = ReflectionHelper.findField(World.class, "isCubicWorld");
            isCubicField.setAccessible(true);
        } catch (ReflectionHelper.UnableToFindFieldException e) {
            // Cubic Chunks isn't installed
        }
    }

    private static final Map<World, GenGenWorldProxy> WORLD_PROXY = new WeakHashMap<>();

    private static GenGenProxy proxy;

    public static GenGenProxy proxy() {
        if (proxy == null) {
            proxy = Loader.isModLoaded("cubicchunks") ? new GenGenProxy.Cubic() : new GenGenProxy.Vanilla();
        }
        return proxy;
    }

    public static GenGenWorldProxy proxy(World world) {
        return WORLD_PROXY.computeIfAbsent(world, w -> isCubic(w) ? new GenGenWorldProxy.Cubic() : new GenGenWorldProxy.Vanilla());
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
}
