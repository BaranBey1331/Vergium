package com.vergium.core.render;

import com.vergium.core.memory.NativeBuffer;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

/**
 * Collects per-entity instance positions and submits them in grouped draws.
 */
public final class EntityInstancer {
    private static final Map<String, Integer> INSTANCE_COUNTS = new ConcurrentHashMap<>();
    private static final Map<String, NativeBuffer> INSTANCE_BUFFERS = new ConcurrentHashMap<>();
    private static final Map<String, Integer> MESH_VBOS = new ConcurrentHashMap<>();
    private static final Map<String, Integer> MESH_VERTEX_COUNTS = new ConcurrentHashMap<>();
    private static int instanceVboId;

    private EntityInstancer() {
    }

    public static void registerMesh(String entityType, int vbo, int vertexCount) {
        if (entityType == null || entityType.isBlank() || vbo <= 0 || vertexCount <= 0) {
            return;
        }
        MESH_VBOS.put(entityType, vbo);
        MESH_VERTEX_COUNTS.put(entityType, vertexCount);
    }

    public static void submit(String entityType, float x, float y, float z) {
        if (entityType == null || entityType.isBlank()) {
            return;
        }

        INSTANCE_COUNTS.merge(entityType, 1, Integer::sum);
        NativeBuffer buffer = INSTANCE_BUFFERS.computeIfAbsent(entityType, key -> new NativeBuffer(16 * 1024));
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
    }

    public static void flush() {
        if (INSTANCE_COUNTS.isEmpty()) {
            return;
        }

        if (instanceVboId == 0) {
            instanceVboId = GL15.glGenBuffers();
            ResourceManager.trackBuffer(instanceVboId);
        }

        INSTANCE_COUNTS.forEach((type, count) -> {
            if (count <= 0) {
                return;
            }

            Integer meshVbo = MESH_VBOS.get(type);
            Integer vertexCount = MESH_VERTEX_COUNTS.get(type);
            NativeBuffer instanceData = INSTANCE_BUFFERS.get(type);
            if (meshVbo == null || vertexCount == null || instanceData == null || instanceData.isEmpty()) {
                return;
            }

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceVboId);
            ByteBuffer readable = instanceData.readableSlice();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, readable, GL15.GL_DYNAMIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, meshVbo);
            GL20.glEnableVertexAttribArray(0);
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceVboId);
            GL20.glEnableVertexAttribArray(1);
            GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 12, 0);
            GL33.glVertexAttribDivisor(1, 1);

            GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, vertexCount, count);

            GL33.glVertexAttribDivisor(1, 0);
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            instanceData.reset();
        });

        INSTANCE_COUNTS.clear();
    }
}
