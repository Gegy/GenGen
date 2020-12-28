package dev.gegy.gengen.api.generator;

import dev.gegy.gengen.api.CubicPos;
import dev.gegy.gengen.api.writer.ChunkPopulationWriter;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface GenericChunkPopulator {
    void populateChunk(CubicPos pos, ChunkPopulationWriter writer);
}
