package net.gegy1000.gengen.api.generator;

import mcp.MethodsReturnNonnullByDefault;
import net.gegy1000.gengen.api.CubicPos;
import net.gegy1000.gengen.api.writer.ChunkPrimeWriter;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface GenericChunkPrimer {
    void primeChunk(CubicPos pos, ChunkPrimeWriter writer);
}
