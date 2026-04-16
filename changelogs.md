# Vergium Changelogs

## [1.3.0-MAJOR] - 2026-04-16
### Overhaul
- **Multi-Architecture Pipeline:** Introduced `VulkanFastPath` specifically optimized for Samsung Xclipse 940 (AMD RDNA 3).
- **Zero-Allocation Pooling:** Rewrote memory management to use `BufferPool`, eliminating heap allocations and memory leaks during long play sessions.
- **Universal Mod-Bridge:** Implemented a new bridge system that intercepts and batches rendering data from all 3rd-party mods.
- **Hardened Resource Lifecycle:** Enhanced `ResourceManager` and `BufferPool` to guarantee 100% cleanup on world exit.

## [1.2.0] - 2026-04-16
### Fixed
- Fixed build error in `Vergium.java` by correctly importing `ResourceManager`.
- Updated `README.md` with realistic project descriptions.

## [1.1.0] - 2026-04-16
### Added
- **Phase 7: Leak-Free Pipeline & Entity Instancing**
  - Implemented `ResourceManager` to prevent GL resource leaks.
  - Added basic `EntityInstancer` for grouping mobs.
...
