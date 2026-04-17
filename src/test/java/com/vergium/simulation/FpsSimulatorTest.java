package com.vergium.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FpsSimulatorTest {
    private final FpsSimulator simulator = new FpsSimulator();

    @ParameterizedTest(name = "simulation scenario #{index}")
    @MethodSource("scenarioMatrix")
    void simulationProducesStablePositiveMetrics(FpsSimulationScenario scenario) {
        FpsSimulationReport report = simulator.simulate(scenario);

        assertEquals(scenario.name(), report.scenarioName());
        assertTrue(report.frameTimeMillis() > 0.0);
        assertTrue(report.fps() > 0.0);
        assertTrue(report.cpuLoadScore() >= 0.0);
        assertTrue(report.gpuLoadScore() >= 0.0);
        assertTrue(report.memoryLoadScore() >= 0.0);
        assertEquals(!scenario.glAvailable(), report.fallbackMode());
    }

    @Test
    void heavierScenarioHasLowerFpsThanLighterScenario() {
        FpsSimulationReport light = simulator.simulate(new FpsSimulationScenario("light", 8, 2, 400, 50, 12, 4, 64, true));
        FpsSimulationReport heavy = simulator.simulate(new FpsSimulationScenario("heavy", 96, 40, 12000, 1800, 128, 64, 640, true));

        assertTrue(light.fps() > heavy.fps());
        assertTrue(heavy.frameTimeMillis() > light.frameTimeMillis());
    }

    @Test
    void crashRiskTriggersForExtremePressure() {
        FpsSimulationReport report = simulator.simulate(new FpsSimulationScenario("extreme", 400, 200, 30000, 16000, 256, 128, 1024, false));

        assertTrue(report.crashRisk());
        assertTrue(report.frameTimeMillis() > 300.0);
    }

    static Stream<Arguments> scenarioMatrix() {
        return IntStream.range(0, 128)
                .mapToObj(index -> {
                    boolean gl = index % 3 != 0;
                    return Arguments.of(new FpsSimulationScenario(
                            "scenario-" + index,
                            4 + (index % 32),
                            index % 20,
                            400 + (index * 70),
                            20 + (index * 3),
                            8 + (index % 64),
                            index % 24,
                            32 + (index * 4),
                            gl
                    ));
                });
    }
}
