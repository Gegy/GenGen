package dev.gegy.gengen.api.generator;

import dev.gegy.gengen.api.writer.ChunkPrimeWriter;
import mcp.MethodsReturnNonnullByDefault;
import dev.gegy.gengen.api.CubicPos;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface GenericChunkPrimer {
    void primeChunk(CubicPos pos, ChunkPrimeWriter writer);
}
