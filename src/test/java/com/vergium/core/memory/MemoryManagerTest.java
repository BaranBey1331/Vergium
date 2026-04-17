package com.vergium.core.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class MemoryManagerTest {

    @AfterEach
    void tearDown() {
        MemoryManager.freeAll();
    }

    @Test
    void allocateTracksDirectNativeOrderBuffers() {
        ByteBuffer buffer = MemoryManager.allocate(128);

        assertTrue(buffer.isDirect());
        assertEquals(128, buffer.capacity());
        assertEquals(ByteOrder.nativeOrder(), buffer.order());
        assertTrue(MemoryManager.getAllocatedBuffers().contains(buffer));
        assertEquals(1, MemoryManager.getTrackedBufferCount());
        assertEquals(128, MemoryManager.getTrackedBytes());
    }

    @Test
    void freeRemovesTrackedBuffer() {
        ByteBuffer buffer = MemoryManager.allocate(256);

        MemoryManager.free(buffer);

        assertFalse(MemoryManager.getAllocatedBuffers().contains(buffer));
        assertEquals(0, MemoryManager.getTrackedBufferCount());
        assertEquals(0, MemoryManager.getTrackedBytes());
    }

    @Test
    void freeAllClearsTrackingState() {
        MemoryManager.allocate(64);
        MemoryManager.allocate(128);

        MemoryManager.freeAll();

        assertTrue(MemoryManager.getAllocatedBuffers().isEmpty());
        assertEquals(0, MemoryManager.getTrackedBufferCount());
        assertEquals(0, MemoryManager.getTrackedBytes());
    }
}
