# Vergium Changelog

## [2.2.0-HARDENING-AND-SIMULATION] - 2026-04-17

### Added
1. Added a second hardening plan for the aggressive test-expansion and architecture pass.
2. Added `junit-jupiter-params` to support large-scale parameterized regression coverage.
3. Added `ChunkBuildResult` as a deterministic chunk planning artifact.
4. Added `DispatchPipeline` to decouple dispatch logic from a concrete fast-path implementation.
5. Added `InstanceBatchTracker` to separate instance planning from GL submission.
6. Added `FpsSimulationScenario` for synthetic render workload inputs.
7. Added `FpsSimulationReport` for deterministic workload analysis outputs.
8. Added `FpsSimulator` to simulate FPS, bottlenecks, fallback behavior, and crash risk without a live client.
9. Added `BufferPoolMetricsTest`.
10. Added `MemoryManagerParameterizedTest`.
11. Added `NativeBufferParameterizedTest`.
12. Added `InstanceBatchTrackerTest`.
13. Added `ResourceSubsystemTest`.
14. Added `BottleneckProfilerMetricsTest`.
15. Added `VertexFormatTest`.
16. Added `ModBridgeTest`.
17. Added `VergiumChunkBuilderTest`.
18. Added `VergiumRenderDispatcherTest`.
19. Added `FpsSimulatorTest`.

### Changed
20. Bumped the project version to `2.2.0`.
21. Expanded automated verification from the earlier low-20s range to **344 passing test executions**.
22. Reworked `VergiumChunkBuilder` into a deterministic, testable chunk geometry planner.
23. Reworked `VergiumRenderDispatcher` to use a lazily resolved pipeline and safer fallback staging.
24. Hardened `VulkanFastPath` availability checks against missing LWJGL native libraries.
25. Reworked `EntityInstancer` to use `InstanceBatchTracker` instead of mixing planning and GL mutation in one path.
26. Reworked `VergiumBatchRenderer` so mesh payloads can be staged meaningfully in fallback mode.
27. Reworked `ModBridge` to track bridged vertex counts for diagnostics and tests.
28. Reworked `BufferPool` to expose acquire/release/rejection metrics.
29. Reworked `MemoryManager` to track peak native-memory pressure.
30. Reworked `NativeBuffer` to support byte-array writes for staged payload transfers.
31. Reworked `CommandBuffer` to reject invalid negative draw parameters and report command capacity.
32. Reworked `BottleneckProfiler` to track average frame time and slow-frame counts.
33. Reworked `ResourceManager` cleanup to reset related tracked subsystems consistently.
34. Reworked `ShaderManager`, `UniformBufferManager`, and `OcclusionQueryManager` with additional test/reset hooks.
35. Updated README for the simulation-oriented hardening pass.
36. Updated TODO with simulator and CI follow-up tasks.

### Fixed
37. Fixed test-time crashes caused by eager LWJGL/OpenGL initialization when no native library was present.
38. Fixed the chunk build path so it no longer behaves like an empty placeholder under non-GL environments.
39. Fixed the dispatch architecture so non-GL environments fall back deterministically instead of behaving opaquely.
40. Fixed several previously under-specified subsystems so they can be validated outside the Minecraft runtime.

## [2.1.0-MAJOR-STABILIZATION] - 2026-04-17

### Added
1. Added a repo cleanup plan document for the major stabilization pass.
2. Added JUnit 5 test support to Gradle.
3. Added test logging to Gradle test runs.
4. Added unit tests for `BufferPool`.
5. Added unit tests for `MemoryManager`.
6. Added unit tests for `NativeBuffer`.
7. Added unit tests for `VergiumMeshBuilder`.
8. Added unit tests for `VergiumBatchRenderer`.
9. Added unit tests for `CommandBuffer`.
10. Added unit tests for `StateSorter`.
11. Added unit tests for `BottleneckProfiler`.
12. Added tracked byte/count accessors for memory diagnostics.
13. Added readable slice support for native buffers.
14. Added command snapshot support for indirect draw verification.
15. Added flush counters and tracked-layer inspection in the batch renderer.

### Changed
16. Bumped the project version to `2.1.0`.
17. Reworked `MemoryManager` to use defensive tracking snapshots instead of exposing mutable internals.
18. Removed forced `System.gc()` and `System.runFinalization()` calls from memory cleanup paths.
19. Reworked `NativeBuffer` into a growable buffer with bounds-safe writes.
20. Clamped packed UV writes instead of blindly overflowing coordinates.
21. Clamped packed position writes to signed byte range.
22. Reworked `BufferPool` to enforce fixed-size reuse rules and explicit pool limits.
23. Reworked `CommandBuffer` so GPU upload views are readable and correctly flipped.
24. Reworked `BottleneckProfiler` to support injected clocks/analyzers for tests.
25. Reworked `StateSorter` into a deterministic sortable queue with a drain API.
26. Reworked `VergiumBatchRenderer` to normalize layer names and report real flush counts.
27. Reworked `ModBridge` to sanitize external mod ids before routing layer traffic.
28. Reworked `VergiumMeshBuilder` to track vertex counts explicitly.
29. Reworked `VergiumRenderDispatcher` to create a real command buffer before dispatch.
30. Reworked `VulkanFastPath` to degrade safely when no OpenGL context exists.
31. Reworked `ResourceManager` to use thread-safe tracked resource sets and guarded deletion.
32. Reworked `ShaderManager` to validate input and skip unsafe loads without a GL context.
33. Reworked `UniformBufferManager` to initialize only when a GL context exists.
34. Reworked `OcclusionQueryManager` to use a thread-safe reusable query pool.
35. Reworked `EntityInstancer` to reuse readable native slices and reset per-type instance data safely.
36. Reworked `VisibilityManager` to handle null bounds and explicit frustum resets.
37. Reworked the main mod entrypoint to reset pooled state on level unload.
38. Reworked `MixinLevelRenderer` to bracket frame profiling around render passes.
39. Reworked `MixinEntityRenderDispatcher` to avoid null/degenerate culling cancellation.
40. Reworked `MixinMultiBufferSource` to target the nested buffer source class directly.
41. Rewrote the README to match the real codebase state.
42. Rewrote the TODO list into actionable follow-up work.
43. Reworked the integrity script to run tests and build verification instead of compile-only checks.

### Removed
44. Removed the brittle `MixinRenderSection` path from the active mixin config.
45. Removed misleading documentation that implied a finished engine replacement.

### Release Notes
- Main branch was cleaned so generated `.gradle/` and `build/` artifacts are no longer versioned.
- The 2.1.0 line now reflects the actual repository state, tested pure-Java infrastructure, and safer experimental rendering behavior.
- Verification completed with `./gradlew test build --stacktrace` and `./check_integrity.sh` before publish.

## [2.0.0-ENGINE-OVERHAUL] - 2026-04-16

Initial repository bootstrap and experimental rendering prototypes.
