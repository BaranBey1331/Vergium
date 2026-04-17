# Vergium Follow-up Tasks

- [ ] Replace placeholder shader compilation with validated compile/link error handling.
- [ ] Add integration smoke tests around mixin loading and Forge startup.
- [ ] Move GL cleanup onto explicitly safe client-thread lifecycle hooks.
- [ ] Decide whether the fast path should remain OpenGL-backed or be renamed from `VulkanFastPath`.
- [ ] Implement a real batched submission path instead of staging-only flush resets.
- [ ] Add metrics/logging around pooled buffer pressure and tracked native memory.
- [ ] Expand entity instancing to use bounded per-type growth strategies.
- [ ] Add scenario snapshots from real gameplay captures to calibrate the FPS simulator.
- [ ] Wire simulation thresholds into CI so extreme regressions fail fast.
