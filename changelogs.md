# Vergium Changelogs

## [0.4.0] - 2026-04-16
### Added
- **Phase 4: Advanced GPU & Entity Optimization**
  - Implemented `CommandBuffer` for Multi-Draw Indirect (MDI) rendering path.
  - Added `MixinEntityRenderDispatcher` to apply hierarchical culling to all mobs/entities.
  - Optimized fragment shaders with `mediump` precision specifically for mobile RDNA 3 (Xclipse 940).
  - Implemented early alpha discard in fragment shaders to save memory bandwidth.

## [0.3.0] - 2026-04-16
### Added
- **Phase 3: Vertex Packing & Shader Optimization**
  - Implemented `putPackedUV` and `putPackedPos` in `NativeBuffer` to reduce memory bandwidth usage.
  - Added `ShaderManager` for optimized GLSL shader injection.
  - Created initial optimized vertex shaders for RDNA 3 / Xclipse 940.

## [0.2.0] - 2026-04-16
### Added
- **Phase 2: Hardware Occlusion Culling**
  - Implemented `OcclusionQueryManager` for OpenGL ES 3.2 hardware queries.
  - Added `VisibilityManager` for hierarchical frustum and occlusion culling.
  - Integrated culling logic into `SectionRenderDispatcher` via Mixins to skip "gereksiz" (unnecessary) geometry.

## [0.1.0] - 2026-04-16
### Added
- **Phase 1: Core Rendering & Memory Rewrite**
  - Implemented off-heap memory management (`MemoryManager`, `NativeBuffer`) to bypass Java GC.
  - Added `VergiumBatchRenderer` for draw call batching.
  - Integrated Mixin support and established the core rendering hook in `LevelRenderer`.

## [Initial Setup] - 2026-04-16
### Added
- Initial project structure for Forge 1.20.1.
- GitHub Actions workflow for automated `.jar` builds.
- Design specification for Xclipse 940 (ANGLE/Vulkan) optimizations.
