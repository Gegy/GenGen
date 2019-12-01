package net.gegy1000.gengen.core;

import net.gegy1000.gengen.api.GenericWorldType;
import net.gegy1000.gengen.core.impl.cubic.CubeWorldTypeImpl;
import net.gegy1000.gengen.core.impl.vanilla.ColumnWorldTypeImpl;
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
