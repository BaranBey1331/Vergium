package com.vergium.core.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vergium.core.memory.MemoryManager;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class InstanceBatchTrackerTest {

    @AfterEach
    void tearDown() {
        MemoryManager.freeAll();
    }

    @Test
    void snapshotPlansCaptureRegisteredMeshesAndInstances() {
        InstanceBatchTracker tracker = new InstanceBatchTracker();
        tracker.registerMesh("zombie", 5, 24);
        tracker.submit("zombie", 1.0f, 2.0f, 3.0f);
        tracker.submit("zombie", 4.0f, 5.0f, 6.0f);

        List<InstanceBatchTracker.InstancePlan> plans = tracker.snapshotPlans();

        assertEquals(1, plans.size());
        assertEquals("zombie", plans.get(0).entityType());
        assertEquals(24, plans.get(0).vertexCount());
        assertEquals(2, plans.get(0).instanceCount());
        assertEquals(24, plans.get(0).instanceData().getPosition());
    }

    @Test
    void resetCountsPreservesBuffersButClearsPendingInstances() {
        InstanceBatchTracker tracker = new InstanceBatchTracker();
        tracker.registerMesh("zombie", 5, 24);
        tracker.submit("zombie", 1.0f, 2.0f, 3.0f);

        tracker.resetCounts();

        assertEquals(0, tracker.getPendingInstanceCount());
        assertTrue(tracker.getTrackedEntityTypes().contains("zombie"));
        assertTrue(tracker.snapshotPlans().isEmpty());
    }

    @Test
    void clearAllDropsEverything() {
        InstanceBatchTracker tracker = new InstanceBatchTracker();
        tracker.registerMesh("zombie", 5, 24);
        tracker.submit("zombie", 1.0f, 2.0f, 3.0f);

        tracker.clearAll();

        assertEquals(0, tracker.getPendingInstanceCount());
        assertTrue(tracker.getTrackedEntityTypes().isEmpty());
    }
}
