# Vergium Changelog

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
