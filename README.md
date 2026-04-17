# Vergium

Vergium is a Forge mod targeting **Minecraft 1.20.1** and **Java 17** with a focus on safer render-side experimentation, lower allocation churn, predictable lifecycle cleanup, and testable rendering architecture.

## What changed in the 2.2.0 hardening update?

This update extends the 2.1.0 stabilization pass with much broader verification and more deterministic runtime behavior:

- growable native buffers instead of unchecked writes
- tracked direct-memory accounting instead of exposed global internals
- reusable transient bridge buffers with explicit pool limits
- readable command buffers for indirect draw uploads
- safer fast-path dispatch that degrades when no GL context exists
- deterministic chunk-build planning instead of placeholder chunk compilation stubs
- pure-Java instance planning extracted from GL submission code
- FPS simulation infrastructure for regression-testing workload behavior without a live game session
- 300+ automated test executions covering memory, batching, dispatch, simulation, and planner logic
- lifecycle cleanup that resets Vergium state consistently on level unload
- reduced mixin risk by removing the brittle section-bounds mutation path
- first automated unit-test suite for pure Java infrastructure
- refreshed changelog, TODO list, and integrity checks

## Current architecture

Vergium currently contains these main subsystems:

1. **Memory primitives**
   - `MemoryManager`
   - `NativeBuffer`
   - `BufferPool`
2. **Render staging**
   - `VergiumBatchRenderer`
   - `CommandBuffer`
   - `StateSorter`
3. **Runtime helpers**
   - `VisibilityManager`
   - `ResourceManager`
   - `BottleneckProfiler`
4. **Experimental fast path**
   - `VergiumRenderDispatcher`
   - `VulkanFastPath`
5. **Simulation / planning**
   - `ChunkBuildResult`
   - `InstanceBatchTracker`
   - `FpsSimulator`
   - `FpsSimulationScenario`
   - `FpsSimulationReport`

## Build

```bash
./gradlew test build
```

## Integrity check

```bash
./check_integrity.sh
```

## Status

Vergium is still an experimental optimization project. The 2.2.0 release focuses on **correctness, crash-resistance, simulation, and testability first**, not on claiming finished engine replacement work that the code does not yet implement.

## License

MIT
