package net.gegy1000.gengen.api.generator;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.writer.ChunkPopulationWriter;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface GenericChunkPopulator {
    void populateChunk(CubicPos pos, ChunkPopulationWriter writer);
}
