# Vergium Changelogs

## [1.4.0-FINAL-POLISH] - 2026-04-16
### Fixed
- **Actual MDI Implementation:** Replaced hardcoded draw counts with dynamic buffer-based command counts in `VulkanFastPath`.
- **Culling Actuator:** Corrected `MixinRenderSection` to actually cancel rendering logic by returning empty bounding boxes for non-visible sections.
- **Thread Safety:** Implemented `AtomicLong` and `AtomicInteger` in `BottleneckProfiler` to prevent race conditions.
- **Buffer Stability:** Fixed `glBufferData` usage in the fast path to use `GL_STREAM_DRAW` for better RDNA 3 synchronization.

## [1.3.0-MAJOR] - 2026-04-16
### Overhaul
- **Multi-Architecture Pipeline:** Introduced `VulkanFastPath` specifically optimized for Samsung Xclipse 940 (AMD RDNA 3).
- **Zero-Allocation Pooling:** Rewrote memory management to use `BufferPool`, eliminating heap allocations and memory leaks during long play sessions.
- **Universal Mod-Bridge:** Implemented a new bridge system that intercepts and batches rendering data from all 3rd-party mods.
- **Hardened Resource Lifecycle:** Enhanced `ResourceManager` and `BufferPool` to guarantee 100% cleanup on world exit.

## [1.2.0] - 2026-04-16
### Fixed
- Fixed build error in `Vergium.java` by correctly importing `ResourceManager`.
- Updated `README.md` with realistic project descriptions and technical details.

## [1.1.0] - 2026-04-16
### Added
- **Phase 7: Leak-Free Pipeline & Entity Instancing**
  - Implemented `ResourceManager` to prevent GL resource leaks.
  - Added basic `EntityInstancer` for grouping mobs.
- **Phase 6: Mod Compatibility**
  - Added `BottleneckProfiler` to track CPU/GPU sync issues.
  - Integrated `MixinMultiBufferSource` to assist with 3rd-party mod rendering.

## [1.0.0-MAJOR] - 2026-04-16
### Added
- **Phase 5: Major Vulkan-Native Optimization**
  - Uniform Buffer Objects (UBO) for matrix updates.
  - State Sorting Engine for efficient command ordering.

## [0.4.0] - 2026-04-16
### Added
- **Phase 4: Advanced GPU & Entity Optimization**
  - Multi-Draw Indirect (MDI) foundation via `CommandBuffer`.
  - Entity culling to skip hidden mobs.
  - Optimized fragment shaders with `mediump` precision for mobile RDNA 3.

## [0.1.0 - 0.3.0] - 2026-04-16
- Initial project structure for Forge 1.20.1.
- GitHub Actions workflow for automated `.jar` builds.
- Off-heap memory management (`MemoryManager`, `NativeBuffer`).
- Vertex packing (Smart Byte Packing) for bandwidth optimization.
- Core rendering hooks via Mixins.
