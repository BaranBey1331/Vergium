# Vergium Cleanup Plan — 2026-04-17

## Goals
- Replace fake or unsafe “optimization” paths with deterministic, testable behavior.
- Harden memory/resource lifecycle management.
- Reduce dangerous mixin side effects.
- Add automated tests for pure-Java components before large cleanup edits spread further.
- Ship a major documented update with clear release notes.

## High-Risk Areas
1. `MemoryManager` stores global mutable state, exposes internals, and forces GC.
2. `NativeBuffer` has no capacity growth or bounds checking.
3. `BufferPool` allows weak reuse semantics and lacks metrics/reset support.
4. `CommandBuffer` never flips upload buffers and does not model command layout cleanly.
5. `VulkanFastPath` can no-op because no command buffer is attached by dispatchers.
6. `ResourceManager` deletes global GL state unsafely and mixes native-memory cleanup with GL cleanup.
7. `MixinRenderSection` mutates bounding boxes in a brittle way.
8. `MixinEntityRenderDispatcher` cancels rendering aggressively without guardrails.
9. `ShaderManager`, `VergiumBatchRenderer`, and related classes expose half-implemented APIs with no diagnostics.
10. Build/test/release docs are stale and under-specified.

## Behavior to Lock with Tests
- `BufferPool` buffer reuse, normalization, limits, and reset behavior.
- `MemoryManager` allocation tracking and release semantics.
- `NativeBuffer` growth, reset, and packed writes.
- `CommandBuffer` command layout and upload view lifecycle.
- `StateSorter` ordering behavior.
- `BottleneckProfiler` sampling/analyzer semantics.

## Cleanup Sequence
1. Add JUnit 5 and unit-test coverage for pure Java infrastructure.
2. Refactor memory primitives (`MemoryManager`, `NativeBuffer`, `BufferPool`).
3. Refactor render-data primitives (`CommandBuffer`, `StateSorter`, `VergiumBatchRenderer`).
4. Harden resource lifecycle utilities (`ResourceManager`, `ShaderManager`, `UniformBufferManager`, `OcclusionQueryManager`).
5. Make dispatch path deterministic (`VergiumMeshBuilder`, `VergiumRenderDispatcher`, `VulkanFastPath`, `ModBridge`).
6. Reduce risky mixin behavior and add runtime guards/logging.
7. Refresh release metadata/docs (`README`, `TODO`, integrity script, changelog).
8. Run `./gradlew test build`, fix failures, then commit.

## Constraints
- No new runtime dependencies.
- Keep Forge compatibility on Minecraft 1.20.1 / Java 17.
- Prefer safe degradation over unsupported “fast path” behavior.
