package com.vergium.core.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

/**
 * Centralized UBO lifecycle with context guards.
 */
public final class UniformBufferManager {
    private static final int BUFFER_SIZE = 1024;
    private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(BUFFER_SIZE).order(ByteOrder.nativeOrder());

    private static int uboId;

    private UniformBufferManager() {
    }

    public static void init() {
        if (uboId != 0 || !isGlContextAvailable()) {
            return;
        }

        uboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uboId);
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, BUFFER_SIZE, GL15.GL_DYNAMIC_DRAW);
        GL31.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, uboId);
        ResourceManager.trackBuffer(uboId);
    }

    public static void updateMatrixData(float[] projMat, float[] modelViewMat) {
        if (uboId == 0 || projMat == null || modelViewMat == null || projMat.length != 16 || modelViewMat.length != 16) {
            return;
        }

        BUFFER.clear();
        for (float value : projMat) {
            BUFFER.putFloat(value);
        }
        for (float value : modelViewMat) {
            BUFFER.putFloat(value);
        }
        BUFFER.flip();

        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uboId);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, BUFFER);
    }

    public static int getUboId() {
        return uboId;
    }

    private static boolean isGlContextAvailable() {
        try {
            return GL.getCapabilities() != null;
        } catch (IllegalStateException ex) {
            return false;
        }
    }
}
