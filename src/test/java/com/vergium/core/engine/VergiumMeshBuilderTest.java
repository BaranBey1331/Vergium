package com.vergium.core.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.vergium.core.memory.MemoryManager;

class VergiumMeshBuilderTest {

    @AfterEach
    void tearDown() {
        MemoryManager.freeAll();
    }

    @Test
    void vertexWritesExpectedCompactLayout() {
        VergiumMeshBuilder builder = new VergiumMeshBuilder(16);

        builder.vertex(1.0f, 2.0f, 3.0f, 0.25f, 0.75f);

        ByteBuffer bytes = builder.getBuffer().getByteBuffer();
        assertEquals(VertexFormat.STRIDE, builder.getBuffer().getPosition());
        assertEquals(1, builder.getVertexCount());
        assertEquals(1.0f, bytes.getFloat(0));
        assertEquals(2.0f, bytes.getFloat(4));
        assertEquals(3.0f, bytes.getFloat(8));
        assertEquals((short) Math.round(0.25f * 65535.0f), bytes.getShort(12));
        assertEquals((short) Math.round(0.75f * 65535.0f), bytes.getShort(14));
    }

    @Test
    void resetClearsMeshBuilderState() {
        VergiumMeshBuilder builder = new VergiumMeshBuilder(16);
        builder.vertex(1.0f, 2.0f, 3.0f, 0.25f, 0.75f);

        builder.reset();

        assertEquals(0, builder.getVertexCount());
        assertEquals(0, builder.getBuffer().getPosition());
    }
}
