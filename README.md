# Vergium

Vergium is a Forge mod targeting **Minecraft 1.20.1** and **Java 17** with a focus on safer render-side experimentation, lower allocation churn, and more predictable lifecycle cleanup.

## What changed in the 2.1.0 major update?

This update replaces several placeholder or misleading optimization paths with safer, verifiable behavior:

- growable native buffers instead of unchecked writes
- tracked direct-memory accounting instead of exposed global internals
- reusable transient bridge buffers with explicit pool limits
- readable command buffers for indirect draw uploads
- safer fast-path dispatch that degrades when no GL context exists
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

## Build

```bash
./gradlew test build
```

## Integrity check

```bash
./check_integrity.sh
```

## Status

Vergium is still an experimental optimization project. The 2.1.0 release focuses on **correctness, cleanup, and testability first**, not on claiming finished engine replacement work that the code does not yet implement.

## License

MIT
