package com.vergium.core.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class StateSorterTest {

    @Test
    void drawCallsAreSortedByShaderThenTextureThenOffset() {
        StateSorter sorter = new StateSorter();
        sorter.add(2, 3, 10, 4);
        sorter.add(1, 8, 5, 4);
        sorter.add(1, 3, 1, 4);
        sorter.add(1, 3, 0, 4);

        List<StateSorter.DrawCall> sorted = sorter.drainSorted();

        assertEquals(4, sorted.size());
        assertEquals(1, sorted.get(0).shaderId());
        assertEquals(3, sorted.get(0).textureId());
        assertEquals(0, sorted.get(0).bufferOffset());
        assertEquals(1, sorted.get(1).shaderId());
        assertEquals(3, sorted.get(1).textureId());
        assertEquals(1, sorted.get(1).bufferOffset());
        assertEquals(1, sorted.get(2).shaderId());
        assertEquals(8, sorted.get(2).textureId());
        assertEquals(2, sorted.get(3).shaderId());
    }
}
