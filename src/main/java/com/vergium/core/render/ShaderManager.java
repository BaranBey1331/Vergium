package com.vergium.core.render;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL20;

/**
 * Manages and injects optimized GLSL shaders for Xclipse 940.
 * Updated to use standard OpenGL for build compatibility.
 */
public class ShaderManager {
    private static final Map<String, Integer> SHADERS = new HashMap<>();

    /**
     * Loads a shader program.
     */
    public static void loadShader(String name, String vertexSrc, String fragmentSrc) {
        int program = GL20.glCreateProgram();
        // compilation and linking logic...
        SHADERS.put(name, program);
    }

    /**
     * Activates an optimized shader program.
     */
    public static void useShader(String name) {
        Integer program = SHADERS.get(name);
        if (program != null) {
            GL20.glUseProgram(program);
        }
    }
}
