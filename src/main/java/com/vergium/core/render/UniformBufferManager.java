package com.vergium.core.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.opengl.GLES30;
import org.lwjgl.opengl.GL31;

/**
 * Manages Uniform Buffer Objects (UBO) to minimize draw call overhead.
 * Directly maps to Vulkan Descriptor Sets in ANGLE.
 */
public class UniformBufferManager {
    private static int uboId;
    private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());

    /**
     * Initializes the UBO for storing ProjMat and ModelViewMat.
     */
    public static void init() {
        uboId = GLES30.glGenBuffers();
        GLES30.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uboId);
        GLES30.glBufferData(GL31.GL_UNIFORM_BUFFER, 1024, GLES30.GL_DYNAMIC_DRAW);
        GLES30.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, uboId);
    }

    /**
     * Updates matrix data in the UBO.
     * Maps to a single Vulkan buffer update call.
     */
    public static void updateMatrixData(float[] projMat, float[] modelViewMat) {
        BUFFER.clear();
        for (float f : projMat) BUFFER.putFloat(f);
        for (float f : modelViewMat) BUFFER.putFloat(f);
        BUFFER.flip();
        
        GLES30.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uboId);
        GLES30.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, BUFFER);
    }

    public static int getUboId() {
        return uboId;
    }
}
