package com.vergium.core.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vergium.core.engine.VertexFormat;
import org.junit.jupiter.api.Test;

class VertexFormatTest {

    @Test
    void compactLayoutConstantsStayStable() {
        assertEquals(16, VertexFormat.STRIDE);
        assertEquals(0, VertexFormat.OFFSET_POS);
        assertEquals(12, VertexFormat.OFFSET_UV);
        assertTrue(VertexFormat.OFFSET_UV < VertexFormat.STRIDE);
    }
}
