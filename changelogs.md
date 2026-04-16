# Vergium Changelogs

## [2.0.0-ENGINE-OVERHAUL] - 2026-04-16
### Overhaul
- **Custom Mesh System:** Introduced `VergiumMeshBuilder` that writes block geometry directly to off-heap memory, bypassing vanilla `VertexConsumer`.
- **Direct-to-GPU Pipeline:** Implemented `VergiumRenderDispatcher` to send custom meshes directly to the `VulkanFastPath` without intermediate vanilla layers.
- **Engine Replacement:** Updated `MixinRenderSection` to allow complete replacement of the vanilla chunk compilation logic.
- **Optimized Vertex Format:** Defined a new 16-byte vertex format tailored for Samsung Xclipse 940 bandwidth.

## [1.4.0-FINAL-POLISH] - 2026-04-16
### Fixed
- Actual MDI Implementation, Culling Actuator, and Thread Safety.
...
