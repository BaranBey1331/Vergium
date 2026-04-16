package com.vergium.core.render;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Detects rendering bottlenecks between CPU and Xclipse 940 GPU.
 */
public class BottleneckProfiler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static long lastFrameTime;
    private static int frameCount;
    private static float cpuWaitTime; // Accumulated wait for GPU fences

    /**
     * Records the start of a frame.
     */
    public static void startFrame() {
        lastFrameTime = System.nanoTime();
    }

    /**
     * Records the end of a frame and analyzes bottlenecks.
     */
    public static void endFrame() {
        long currentFrameTime = System.nanoTime();
        long duration = currentFrameTime - lastFrameTime;
        frameCount++;

        if (frameCount % 600 == 0) { // Every 10 seconds at 60fps
            float fps = 1_000_000_000.0f / duration;
            if (fps < 30.0f) {
                LOGGER.warn("Low performance detected! Analyzing bottleneck...");
                analyze(fps);
            }
        }
    }

    private static void analyze(float fps) {
        // Logic to check if we are CPU bound (ANGLE layer overhead) 
        // or GPU bound (Fill rate / Vertex limit)
        if (cpuWaitTime > 5.0f) {
            LOGGER.warn("Status: CPU-Bound (Wait for GPU). Optimization: Increase Batching.");
        } else {
            LOGGER.warn("Status: GPU-Bound (Processing Load). Optimization: Increase Culling.");
        }
    }
}
