package com.vergium.core.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class MemoryManagerParameterizedTest {

    @AfterEach
    void tearDown() {
        MemoryManager.freeAll();
    }

    @ParameterizedTest(name = "allocate size {0}")
    @MethodSource("sizes")
    void allocationSizesAreTracked(int size) {
        ByteBuffer buffer = MemoryManager.allocate(size);

        assertEquals(size, buffer.capacity());
        assertTrue(MemoryManager.getTrackedBytes() >= size);
        assertTrue(MemoryManager.getPeakTrackedBytes() >= size);
    }

    static IntStream sizes() {
        return IntStream.of(1, 2, 4, 8, 16, 24, 32, 48, 64, 96, 128, 192, 256, 384, 512, 768, 1024, 1536, 2048, 4096);
    }
}
