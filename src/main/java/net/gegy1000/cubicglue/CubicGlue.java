package net.gegy1000.cubicglue;

import net.gegy1000.cubicglue.api.CubicWorldType;
import net.gegy1000.cubicglue.util.GluedEventDispatcher;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CubicGlue {
    public static final Logger LOGGER = LogManager.getLogger(CubicGlue.class);

    private static Proxy proxy;

    public static Proxy get() {
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
    }
}
