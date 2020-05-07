package net.gegy1000.gengen.core.support;

import net.gegy1000.gengen.core.GenGen;
import net.minecraft.world.gen.IChunkGenerator;

import java.lang.reflect.Field;

public final class BukkitSupport {
    private static final String GENERATOR_FIELD_NAME = "generator";

    public static IChunkGenerator unwrapChunkGenerator(IChunkGenerator generator) {
        Class<? extends IChunkGenerator> generatorClass = generator.getClass();
        if (generatorClass.getName().endsWith("NormalChunkGenerator")) {
            try {
                Field field = generatorClass.getDeclaredField(GENERATOR_FIELD_NAME);
                field.setAccessible(true);
                IChunkGenerator unwrappedGenerator = (IChunkGenerator) field.get(generator);
                if (unwrappedGenerator != null) {
                    return unwrappedGenerator;
                }
            } catch (ReflectiveOperationException e) {
                GenGen.LOGGER.error("Failed to get {} field on {}", GENERATOR_FIELD_NAME, generatorClass.getSimpleName(), e);
            }
        }

        return generator;
    }
}
