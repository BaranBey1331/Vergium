package com.vergium.core.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NativeBufferParameterizedTest {

    @AfterEach
    void tearDown() {
        MemoryManager.freeAll();
    }

    @ParameterizedTest(name = "uv pair {0}, {1}")
    @MethodSource("uvPairs")
    void packedUvHandlesManyInputs(float u, float v) {
        NativeBuffer buffer = new NativeBuffer(4);

        buffer.putPackedUV(u, v);

        ByteBuffer bytes = buffer.getByteBuffer();
        short expectedU = (short) Math.round(Math.max(0.0f, Math.min(u, 1.0f)) * 65535.0f);
        short expectedV = (short) Math.round(Math.max(0.0f, Math.min(v, 1.0f)) * 65535.0f);
        assertEquals(expectedU, bytes.getShort(0));
        assertEquals(expectedV, bytes.getShort(2));
    }

    @ParameterizedTest(name = "packed pos {0}, {1}, {2}")
    @MethodSource("positionTriples")
    void packedPosClampsAcrossManyInputs(float x, float y, float z) {
        NativeBuffer buffer = new NativeBuffer(3);

        buffer.putPackedPos(x, y, z);

        ByteBuffer bytes = buffer.getByteBuffer();
        assertEquals(toByte(x), bytes.get(0));
        assertEquals(toByte(y), bytes.get(1));
        assertEquals(toByte(z), bytes.get(2));
    }

    static Stream<Arguments> uvPairs() {
        float[] values = new float[] {-0.25f, 0.0f, 0.1f, 0.25f, 0.5f, 0.75f, 0.9f, 1.0f};
        Stream.Builder<Arguments> builder = Stream.builder();
        for (float u : values) {
            for (float v : values) {
                builder.add(Arguments.of(u, v));
            }
        }
        return builder.build().limit(32);
    }

    static Stream<Arguments> positionTriples() {
        float[] values = new float[] {-2.0f, -1.0f, -0.5f, 0.0f, 0.5f, 1.0f, 2.0f};
        Stream.Builder<Arguments> builder = Stream.builder();
        for (float x : values) {
            for (float y : values) {
                for (float z : values) {
                    builder.add(Arguments.of(x, y, z));
                }
            }
        }
        return builder.build().limit(49);
    }

    private static byte toByte(float value) {
        return (byte) Math.round(Math.max(-1.0f, Math.min(value, 1.0f)) * 127.0f);
    }
}
