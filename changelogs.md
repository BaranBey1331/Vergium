# Vergium Changelogs

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

## [0.1.0 - 0.3.0]
- Initial setup, build system, vertex packing, and core rendering rewrite.
