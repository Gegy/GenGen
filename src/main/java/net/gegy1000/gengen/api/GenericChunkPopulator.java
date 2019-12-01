package net.gegy1000.gengen.api;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface GenericChunkPopulator {
    void populate(CubicPos pos, ChunkPopulationWriter writer);
}
