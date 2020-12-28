package dev.gegy.gengen.core;

import dev.gegy.gengen.api.GenericWorldType;
import dev.gegy.gengen.core.impl.cubic.CubeWorldTypeImpl;
import dev.gegy.gengen.core.impl.vanilla.ColumnWorldTypeImpl;
import net.minecraft.world.WorldType;

public interface GenGenProxy {
    WorldType createWorldType(GenericWorldType worldType);

    class Cubic implements GenGenProxy {
        @Override
        public WorldType createWorldType(GenericWorldType worldType) {
            return new CubeWorldTypeImpl(worldType);
        }
    }

    class Vanilla implements GenGenProxy {
        @Override
        public WorldType createWorldType(GenericWorldType worldType) {
            return new ColumnWorldTypeImpl(worldType);
        }
    }
}
