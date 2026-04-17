package com.vergium.core.bridge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vergium.core.memory.BufferPool;
import com.vergium.core.render.VergiumBatchRenderer;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ModBridgeTest {

    @AfterEach
    void tearDown() {
        ModBridge.resetStats();
        VergiumBatchRenderer.clearAll();
        BufferPool.clear();
    }

    @Test
    void bridgeDrawAccumulatesVertexMetrics() {
        ModBridge.bridgeDraw("Example Mod", 12);
        ModBridge.bridgeDraw("Example Mod", 5);

        assertEquals(17, ModBridge.getBridgedVertexCount("example mod"));
        assertTrue(VergiumBatchRenderer.getTrackedLayers().contains("mod_layer_example_mod"));
    }

    @ParameterizedTest(name = "sanitized mod id case #{index}")
    @MethodSource("modIds")
    void bridgeDrawNormalizesManyModIds(String input, String expectedKey) {
        ModBridge.bridgeDraw(input, 1);

        assertTrue(VergiumBatchRenderer.getTrackedLayers().contains(expectedKey));
    }

    static Stream<Arguments> modIds() {
        return Stream.of(
                Arguments.of("Alex's Mobs", "mod_layer_alex_s_mobs"),
                Arguments.of(" Twilight Forest ", "mod_layer_twilight_forest"),
                Arguments.of("A/B\\C", "mod_layer_a_b_c"),
                Arguments.of("MOD.UPPER", "mod_layer_mod.upper"),
                Arguments.of("123", "mod_layer_123"),
                Arguments.of("", "mod_layer_unknown"),
                Arguments.of((String) null, "mod_layer_unknown"),
                Arguments.of("mod-with-dash", "mod_layer_mod-with-dash"),
                Arguments.of("mod_with_underscore", "mod_layer_mod_with_underscore")
        );
    }
}
