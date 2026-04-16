package com.vergium.core.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

/**
 * FIXED: Replaced GLES30 with standard GL31 for build environment compatibility.
 */
public class UniformBufferManager {
    private static int uboId;
    private static final ByteBuffer BUFFER = ByteBuffer.allocateDirect(1024).order(ByteOrder.nativeOrder());

    public static void init() {
        uboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uboId);
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, 1024, GL15.GL_DYNAMIC_DRAW);
        GL31.glBindBufferBase(GL31.GL_UNIFORM_BUFFER, 0, uboId);
    }

    public static void updateMatrixData(float[] projMat, float[] modelViewMat) {
        BUFFER.clear();
        for (float f : projMat) BUFFER.putFloat(f);
        for (float f : modelViewMat) BUFFER.putFloat(f);
        BUFFER.flip();
        
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uboId);
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, 0, BUFFER);
    }

    public static int getUboId() {
        return uboId;
    }
}
