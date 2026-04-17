package com.vergium.core.render;

import com.vergium.core.memory.NativeBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

/**
 * Collects per-entity instance positions and submits them in grouped draws.
 */
public final class EntityInstancer {
    private static final InstanceBatchTracker TRACKER = new InstanceBatchTracker();
    private static int instanceVboId;

    private EntityInstancer() {
    }

    public static void registerMesh(String entityType, int vbo, int vertexCount) {
        TRACKER.registerMesh(entityType, vbo, vertexCount);
    }

    public static void submit(String entityType, float x, float y, float z) {
        TRACKER.submit(entityType, x, y, z);
    }

    public static void flush() {
        if (TRACKER.getPendingInstanceCount() == 0) {
            return;
        }

        if (!isGlContextAvailable()) {
            TRACKER.resetCounts();
            return;
        }

        if (instanceVboId == 0) {
            instanceVboId = GL15.glGenBuffers();
            ResourceManager.trackBuffer(instanceVboId);
        }

        for (InstanceBatchTracker.InstancePlan plan : TRACKER.snapshotPlans()) {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceVboId);
            ByteBuffer readable = plan.instanceData().readableSlice();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, readable, GL15.GL_DYNAMIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, plan.meshVbo());
            GL20.glEnableVertexAttribArray(0);
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceVboId);
            GL20.glEnableVertexAttribArray(1);
            GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 12, 0);
            GL33.glVertexAttribDivisor(1, 1);

            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, plan.vertexCount(), plan.instanceCount());

            GL33.glVertexAttribDivisor(1, 0);
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            plan.instanceData().reset();
        }

        TRACKER.resetCounts();
    }

    static InstanceBatchTracker getTracker() {
        return TRACKER;
    }

    private static boolean isGlContextAvailable() {
        try {
            return GL.getCapabilities() != null;
        } catch (IllegalStateException ex) {
            return false;
        }
    }
}
