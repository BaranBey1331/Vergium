package com.vergium.simulation;

public record FpsSimulationReport(
        String scenarioName,
        double frameTimeMillis,
        double fps,
        double cpuLoadScore,
        double gpuLoadScore,
        double memoryLoadScore,
        boolean fallbackMode,
        String bottleneck,
        boolean crashRisk
) {
}
