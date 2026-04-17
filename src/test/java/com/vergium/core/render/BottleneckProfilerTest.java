package com.vergium.core.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class BottleneckProfilerTest {

    @AfterEach
    void tearDown() {
        BottleneckProfiler.resetForTests();
    }

    @Test
    void analyzerRunsEverySampleWindow() {
        AtomicLong nanos = new AtomicLong(1L);
        AtomicInteger analyzerCalls = new AtomicInteger();

        BottleneckProfiler.setTimeSource(nanos::get);
        BottleneckProfiler.setAnalyzer(fps -> analyzerCalls.incrementAndGet());

        for (int frame = 0; frame < BottleneckProfiler.SAMPLE_WINDOW; frame++) {
            BottleneckProfiler.startFrame();
            nanos.addAndGet(16_666_667L);
            BottleneckProfiler.endFrame();
        }

        assertEquals(1, analyzerCalls.get());
    }
}
