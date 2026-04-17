package com.vergium.simulation;

/**
 * Deterministic FPS-oriented simulation for regression testing without a live game runtime.
 */
public final class FpsSimulator {
    public FpsSimulationReport simulate(FpsSimulationScenario scenario) {
        double cpuCost = 4.0
                + (scenario.chunkBuilds() * 0.18)
                + (scenario.bridgeCalls() * 0.04)
                + (scenario.entityInstances() * 0.012)
                + (scenario.occlusionQueries() * 0.01);

        double gpuCost = 3.5
                + (scenario.visibleSections() * 0.09)
                + (scenario.bridgedVertices() / 6500.0)
                + (scenario.entityInstances() / 900.0);

        double memoryCost = scenario.memoryPressureMb() / 48.0;
        double fallbackPenalty = scenario.glAvailable() ? 0.0 : 2.5 + (scenario.chunkBuilds() * 0.03);
        double frameTime = cpuCost + gpuCost + memoryCost + fallbackPenalty;
        double fps = 1000.0 / Math.max(frameTime, 0.001);

        String bottleneck = "balanced";
        double highest = cpuCost;
        if (gpuCost > highest) {
            highest = gpuCost;
            bottleneck = "gpu";
        }
        if (memoryCost > highest) {
            highest = memoryCost;
            bottleneck = "memory";
        }

        boolean crashRisk = scenario.memoryPressureMb() >= 768
                || scenario.entityInstances() >= 15000
                || (!scenario.glAvailable() && scenario.chunkBuilds() >= 300);

        return new FpsSimulationReport(
                scenario.name(),
                round(frameTime),
                round(fps),
                round(cpuCost),
                round(gpuCost),
                round(memoryCost),
                !scenario.glAvailable(),
                bottleneck,
                crashRisk
        );
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
