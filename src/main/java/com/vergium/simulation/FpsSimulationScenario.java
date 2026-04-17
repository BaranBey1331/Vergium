package com.vergium.simulation;

public record FpsSimulationScenario(
        String name,
        int chunkBuilds,
        int bridgeCalls,
        int bridgedVertices,
        int entityInstances,
        int visibleSections,
        int occlusionQueries,
        int memoryPressureMb,
        boolean glAvailable
) {
    public FpsSimulationScenario {
        if (chunkBuilds < 0 || bridgeCalls < 0 || bridgedVertices < 0 || entityInstances < 0
                || visibleSections < 0 || occlusionQueries < 0 || memoryPressureMb < 0) {
            throw new IllegalArgumentException("Simulation values must be non-negative");
        }
    }
}
