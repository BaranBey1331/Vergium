package com.vergium.core.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class BottleneckProfilerMetricsTest {

    @AfterEach
    void tearDown() {
        BottleneckProfiler.resetForTests();
    }

    @Test
    void averageFrameTimeAndSlowFramesAreTracked() {
        AtomicLong nanos = new AtomicLong(10L);
        BottleneckProfiler.setTimeSource(nanos::get);

        BottleneckProfiler.startFrame();
        nanos.addAndGet(16_000_000L);
        BottleneckProfiler.endFrame();

        BottleneckProfiler.startFrame();
        nanos.addAndGet(70_000_000L);
        BottleneckProfiler.endFrame();

        assertEquals(43.0, BottleneckProfiler.getAverageFrameTimeMillis());
        assertEquals(1, BottleneckProfiler.getSlowFrameCount());
    }
}
