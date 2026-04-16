# Vergium Changelogs

## [1.4.0-FINAL-POLISH] - 2026-04-16
### Fixed
- **Actual MDI Implementation:** Replaced hardcoded draw counts with dynamic buffer-based command counts in `VulkanFastPath`.
- **Culling Actuator:** Corrected `MixinRenderSection` to actually cancel rendering logic by returning empty bounding boxes for non-visible sections.
- **Thread Safety:** Implemented `AtomicLong` and `AtomicInteger` in `BottleneckProfiler` to prevent race conditions.
- **Buffer Stability:** Fixed `glBufferData` usage in the fast path to use `GL_STREAM_DRAW` for better RDNA 3 synchronization.

## [1.3.0-MAJOR] - 2026-04-16
### Overhaul
- Multi-Architecture Pipeline and Zero-Allocation Pooling.
...
