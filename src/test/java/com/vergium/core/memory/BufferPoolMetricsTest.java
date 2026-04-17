package com.vergium.core.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BufferPoolMetricsTest {

    @AfterEach
    void tearDown() {
        BufferPool.clear();
    }

    @ParameterizedTest(name = "release rejection case #{index}")
    @MethodSource("invalidBuffers")
    void invalidBuffersAreRejected(ByteBuffer invalidBuffer) {
        BufferPool.release(invalidBuffer);

        assertEquals(1, BufferPool.getTotalRejections());
        assertEquals(0, BufferPool.getPooledBufferCount());
    }

    static Stream<Arguments> invalidBuffers() {
        return Stream.of(
                Arguments.of((ByteBuffer) null),
                Arguments.of(ByteBuffer.allocate(8)),
                Arguments.of(ByteBuffer.allocateDirect(8)),
                Arguments.of(ByteBuffer.allocateDirect(BufferPool.BUFFER_SIZE / 2)),
                Arguments.of(ByteBuffer.allocateDirect(BufferPool.BUFFER_SIZE + 16)),
                Arguments.of(ByteBuffer.allocate(0))
        );
    }
}
