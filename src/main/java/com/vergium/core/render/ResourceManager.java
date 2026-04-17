package com.vergium.core.render;

import com.vergium.core.memory.MemoryManager;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Tracks OpenGL resource ids and clears them defensively.
 */
public final class ResourceManager {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Set<Integer> BUFFERS = ConcurrentHashMap.newKeySet();
    private static final Set<Integer> SHADERS = ConcurrentHashMap.newKeySet();
    private static final Set<Integer> VAOS = ConcurrentHashMap.newKeySet();

    private ResourceManager() {
    }

    public static void trackBuffer(int id) {
        if (id > 0) {
            BUFFERS.add(id);
        }
    }

    public static void trackShader(int id) {
        if (id > 0) {
            SHADERS.add(id);
        }
    }

    public static void trackVAO(int id) {
        if (id > 0) {
            VAOS.add(id);
        }
    }

    public static int getTrackedResourceCount() {
        return BUFFERS.size() + SHADERS.size() + VAOS.size();
    }

    public static void cleanup() {
        deleteTrackedBuffers();
        deleteTrackedShaders();
        deleteTrackedVaos();
        MemoryManager.freeAll();
        ShaderManager.clear();
        UniformBufferManager.resetForTests();
        OcclusionQueryManager.clear();
    }

    private static void deleteTrackedBuffers() {
        BUFFERS.forEach(id -> safeDelete("buffer", id, () -> GL15.glDeleteBuffers(id)));
        BUFFERS.clear();
    }

    private static void deleteTrackedShaders() {
        SHADERS.forEach(id -> safeDelete("shader program", id, () -> GL20.glDeleteProgram(id)));
        SHADERS.clear();
    }

    private static void deleteTrackedVaos() {
        VAOS.forEach(id -> safeDelete("vao", id, () -> GL30.glDeleteVertexArrays(id)));
        VAOS.clear();
    }

    private static void safeDelete(String kind, int id, Runnable deletion) {
        try {
            deletion.run();
        } catch (Throwable throwable) {
            LOGGER.debug("Skipping {} cleanup for id {} because no valid GL context was available.", kind, id, throwable);
        }
    }

    static void clearTrackedStateForTests() {
        BUFFERS.clear();
        SHADERS.clear();
        VAOS.clear();
    }
}
