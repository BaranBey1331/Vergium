package com.vergium.core.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.vergium.core.memory.MemoryManager;
import com.vergium.core.memory.NativeBuffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class VergiumBatchRendererTest {

    @AfterEach
    void tearDown() {
        VergiumBatchRenderer.clearAll();
        MemoryManager.freeAll();
    }

    @Test
    void sameLayerReturnsSameBuffer() {
        NativeBuffer first = VergiumBatchRenderer.getBufferForLayer("solid");
        NativeBuffer second = VergiumBatchRenderer.getBufferForLayer("solid");

        assertSame(first, second);
    }

    @Test
    void differentLayersReturnDifferentBuffers() {
        NativeBuffer first = VergiumBatchRenderer.getBufferForLayer("solid");
        NativeBuffer second = VergiumBatchRenderer.getBufferForLayer("cutout");

        assertNotSame(first, second);
    }

    @Test
    void flushResetsOnlyFilledBuffers() {
        NativeBuffer buffer = VergiumBatchRenderer.getBufferForLayer("solid");
        buffer.putInt(123);

        int flushed = VergiumBatchRenderer.flush();

        assertEquals(1, flushed);
        assertEquals(0, buffer.getPosition());
    }

    @Test
    void clearAllDropsCachedBuffers() {
        NativeBuffer original = VergiumBatchRenderer.getBufferForLayer("solid");

        VergiumBatchRenderer.clearAll();

        NativeBuffer recreated = VergiumBatchRenderer.getBufferForLayer("solid");
        assertNotSame(original, recreated);
    }
}
