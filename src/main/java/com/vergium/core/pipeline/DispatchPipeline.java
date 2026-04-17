package com.vergium.core.pipeline;

import com.vergium.core.render.CommandBuffer;

public interface DispatchPipeline extends IRenderPipeline {
    boolean isAvailable();

    void setCommandBuffer(CommandBuffer commands);
}
