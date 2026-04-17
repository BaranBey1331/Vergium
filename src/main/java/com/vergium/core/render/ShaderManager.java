package com.vergium.core.render;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL20;

/**
 * Tracks shader programs and degrades safely when no GL context exists.
 */
public final class ShaderManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, Integer> SHADERS = new ConcurrentHashMap<>();

    private ShaderManager() {
    }

    public static void loadShader(String name, String vertexSrc, String fragmentSrc) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Shader name must not be blank");
        }
        if (vertexSrc == null || vertexSrc.isBlank() || fragmentSrc == null || fragmentSrc.isBlank()) {
            throw new IllegalArgumentException("Shader sources must not be blank");
        }
        if (!isGlContextAvailable()) {
            LOGGER.debug("Skipping shader load for {} because no GL context is active.", name);
            return;
        }

        int program = GL20.glCreateProgram();
        Integer previous = SHADERS.put(name, program);
        ResourceManager.trackShader(program);

        if (previous != null && previous > 0) {
            try {
                GL20.glDeleteProgram(previous);
            } catch (Throwable throwable) {
                LOGGER.debug("Unable to delete previous shader program {}", previous, throwable);
            }
        }
    }

    public static void useShader(String name) {
        Integer program = SHADERS.get(name);
        if (program != null && isGlContextAvailable()) {
            GL20.glUseProgram(program);
        }
    }

    public static void clear() {
        SHADERS.clear();
    }

    public static int getLoadedShaderCount() {
        return SHADERS.size();
    }

    public static Integer getProgram(String name) {
        return SHADERS.get(name);
    }

    private static boolean isGlContextAvailable() {
        try {
            return GL.getCapabilities() != null;
        } catch (IllegalStateException ex) {
            return false;
        }
    }
}
