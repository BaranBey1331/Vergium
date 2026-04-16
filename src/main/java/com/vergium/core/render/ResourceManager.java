package com.vergium.core.render;

import java.util.HashSet;
import java.util.Set;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Tracks and manages OpenGL resources to prevent memory and cache leaks.
 */
public class ResourceManager {
    private static final Set<Integer> BUFFERS = new HashSet<>();
    private static final Set<Integer> SHADERS = new HashSet<>();
    private static final Set<Integer> VAOS = new HashSet<>();

    public static void trackBuffer(int id) { BUFFERS.add(id); }
    public static void trackShader(int id) { SHADERS.add(id); }
    public static void trackVAO(int id) { VAOS.add(id); }

    /**
     * Cleans up all tracked resources. Called on world unload or shutdown.
     */
    public static void cleanup() {
        BUFFERS.forEach(GL15::glDeleteBuffers);
        SHADERS.forEach(GL20::glDeleteProgram);
        VAOS.forEach(GL30::glDeleteVertexArrays);
        
        BUFFERS.clear();
        SHADERS.clear();
        VAOS.clear();
        
        System.gc(); // Hint to clean up DirectByteBuffers
    }
}
