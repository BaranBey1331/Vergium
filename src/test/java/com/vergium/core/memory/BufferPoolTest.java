package com.vergium.core.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class BufferPoolTest {

    @AfterEach
    void tearDown() {
        BufferPool.clear();
    }

    @Test
    void acquireReturnsDirectFixedSizeBuffer() {
        ByteBuffer buffer = BufferPool.acquire();

        assertTrue(buffer.isDirect());
        assertEquals(BufferPool.BUFFER_SIZE, buffer.capacity());
        assertEquals(0, buffer.position());
        assertEquals(buffer.capacity(), buffer.limit());
    }

    @Test
    void releasedBufferCanBeReused() {
        ByteBuffer first = BufferPool.acquire();
        BufferPool.release(first);

        ByteBuffer reused = BufferPool.acquire();

        assertSame(first, reused);
    }

    @Test
    void acquireResetsReusedBufferState() {
        ByteBuffer buffer = BufferPool.acquire();
        buffer.putInt(42);
        BufferPool.release(buffer);

        ByteBuffer reused = BufferPool.acquire();

        assertEquals(0, reused.position());
        assertEquals(reused.capacity(), reused.limit());
    }

    @Test
    void poolRespectsConfiguredCapacityLimit() {
        for (int index = 0; index < BufferPool.MAX_POOLED_BUFFERS + 10; index++) {
            BufferPool.release(ByteBuffer.allocateDirect(BufferPool.BUFFER_SIZE));
        }

        assertEquals(BufferPool.MAX_POOLED_BUFFERS, BufferPool.getPooledBufferCount());
    }
}
