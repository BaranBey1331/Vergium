package com.vergium.core.pipeline;

/**
 * Interface for different rendering strategies based on hardware.
 */
public interface IRenderPipeline {
    void prepare();
    void render(int bufferId, int vertexCount);
    void finish();
}
