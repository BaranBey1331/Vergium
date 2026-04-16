# Vergium Phase 6: Mod Compatibility & Bottleneck Detection

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement a system to detect rendering bottlenecks and optimize 3rd-party mod rendering without breaking their functionality.

**Architecture:** Profile draw calls and state changes per-mod using a relay system for `MultiBufferSource`.

**Tech Stack:** Java 17, Mixin, Forge, OpenGL ES 3.2.

---

### Task 1: Real-Time Bottleneck Profiler

**Files:**
- Create: `src/main/java/com/vergium/core/render/VisibilityManager.java` (Update existing)
- Create: `src/main/java/com/vergium/core/render/OcclusionQueryManager.java` (Update existing)
- Create: `src/main/java/com/vergium/core/render/BottleneckProfiler.java`

- [ ] **Step 1: Implement BottleneckProfiler**
Measure frame-to-frame time spent in OpenGL calls vs. CPU logic. Report if the system is CPU-bound (likely due to ANGLE) or GPU-bound (Xclipse 940 load).

- [ ] **Step 2: Commit profiler core**
```bash
git add src/main/java/com/vergium/core/render/BottleneckProfiler.java
git commit -m "feat: Add real-time bottleneck profiler for Xclipse 940"
```

### Task 2: Mod-Relay Batching (The Mod Optimizer)

**Files:**
- Create: `src/main/java/com/vergium/mixin/MixinMultiBufferSource.java`
- Modify: `src/main/resources/vergium.mixins.json`

- [ ] **Step 1: Implement MultiBufferSource Relay**
Intercept `getBuffer` calls from all mods. If a mod requests a standard vertex format, redirect it to Vergium's `NativeBuffer` to enable batching across different mods.

- [ ] **Step 2: Update Mixin config**
Add the new Mixin.

- [ ] **Step 3: Commit mod relay**
```bash
git add src/main/java/com/vergium/mixin/MixinMultiBufferSource.java src/main/resources/vergium.mixins.json
git commit -m "feat: Implement Mod-Relay Batching to optimize 3rd-party mod rendering"
```

### Task 4: Pipeline Cache Management

**Files:**
- Create: `src/main/java/com/vergium/core/render/PipelineManager.java`

- [ ] **Step 1: Implement Pipeline State Caching**
Store and reuse OpenGL state objects (VAO, Program bindings) to prevent ANGLE from re-validating the Vulkan pipeline on every draw call.

- [ ] **Step 2: Commit pipeline manager**
```bash
git add src/main/java/com/vergium/core/render/PipelineManager.java
git commit -m "perf: Add Pipeline State Caching to reduce ANGLE validation overhead"
```
