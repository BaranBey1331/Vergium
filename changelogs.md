# Vergium Changelogs

## [1.1.0] - 2026-04-16
### Added
- **Phase 6: Mod Compatibility & Bottleneck Detection**
  - Implemented `BottleneckProfiler` to detect CPU/GPU sync issues on Xclipse 940.
  - Added `MixinMultiBufferSource` relay to intercept and optimize 3rd-party mod rendering.
  - Improved ANGLE pipeline stability by reducing redundant state checks for modded entities.

## [1.0.0-MAJOR] - 2026-04-16
### Added
- **Phase 5: Major Vulkan-Native Optimization**
  - Implemented **Uniform Buffer Objects (UBO)** to consolidate matrix updates.
  - Added **State Sorting Engine** to minimize Vulkan pipeline transitions.
  - Optimized for Samsung Xclipse 940 (AMD RDNA 3) architecture.

## [0.4.0] - 2026-04-16
### Added
- **Phase 4: Advanced GPU & Entity Optimization**
  - Implemented `CommandBuffer` for Multi-Draw Indirect (MDI).
  - Added `MixinEntityRenderDispatcher` for hierarchical culling on mobs.
  - Optimized fragment shaders with `mediump` precision for mobile.
...
