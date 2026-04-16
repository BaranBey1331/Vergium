package com.vergium.core.render;

import com.vergium.core.memory.NativeBuffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

/**
 * High-performance entity instancing engine for Xclipse 940.
 * Groups identical entities to render them in a single GPU call.
 */
public class EntityInstancer {
    private static final Map<String, Integer> INSTANCE_COUNTS = new HashMap<>();
    private static final Map<String, NativeBuffer> INSTANCE_BUFFERS = new HashMap<>();
    private static final Map<String, Integer> MESH_VBOS = new HashMap<>();
    private static final Map<String, Integer> MESH_VERTEX_COUNTS = new HashMap<>();
    private static int instanceVboId = 0;

    /**
     * Registers a generic VBO and its vertex count for an entity type.
     */
    public static void registerMesh(String entityType, int vbo, int vertexCount) {
        MESH_VBOS.put(entityType, vbo);
        MESH_VERTEX_COUNTS.put(entityType, vertexCount);
    }

    /**
     * Records an entity for instanced rendering.
     */
    public static void submit(String entityType, float x, float y, float z) {
        INSTANCE_COUNTS.merge(entityType, 1, Integer::sum);
        NativeBuffer buffer = INSTANCE_BUFFERS.computeIfAbsent(entityType, k -> new NativeBuffer(1024 * 16));
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
    }

    /**
     * Renders all collected instances using glDrawArraysInstanced.
     */
    public static void flush() {
        if (instanceVboId == 0) {
            instanceVboId = GL15.glGenBuffers();
            ResourceManager.trackBuffer(instanceVboId);
        }

        INSTANCE_COUNTS.forEach((type, count) -> {
            if (count > 0) {
                Integer meshVbo = MESH_VBOS.get(type);
                Integer vertexCount = MESH_VERTEX_COUNTS.get(type);
                NativeBuffer instData = INSTANCE_BUFFERS.get(type);

                if (meshVbo != null && vertexCount != null && instData != null) {
                    // Upload instance data (x, y, z positions) to GPU
                    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceVboId);
                    ByteBuffer b = instData.getByteBuffer();
                    b.position(0);
                    b.limit(instData.getPosition());
                    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, b, GL15.GL_DYNAMIC_DRAW);

                    // Bind base mesh geometry (Generic VBO)
                    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, meshVbo);
                    GL20.glEnableVertexAttribArray(0);
                    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

                    // Setup instance positions (location 1)
                    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceVboId);
                    GL20.glEnableVertexAttribArray(1);
                    GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 12, 0);
                    GL33.glVertexAttribDivisor(1, 1); // Advance once per instance

                    // Perform instanced draw call
                    GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, vertexCount, count);

                    // Cleanup state to avoid leaking into other render calls
                    GL33.glVertexAttribDivisor(1, 0);
                    GL20.glDisableVertexAttribArray(0);
                    GL20.glDisableVertexAttribArray(1);
                }
            }
            // Reset buffer for next frame
            if (INSTANCE_BUFFERS.containsKey(type)) {
                INSTANCE_BUFFERS.get(type).reset();
            }
        });
        INSTANCE_COUNTS.clear();
    }
}
