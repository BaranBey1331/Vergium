# Vergium Phase 1 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Rewrite the rendering data path to use off-heap memory and batch draw calls, optimizing for Xclipse 940 (ANGLE/Vulkan).

**Architecture:** Custom memory management bypassing Java GC for vertex data, integrating via Mixins into `LevelRenderer` and `SectionRenderDispatcher`.

**Tech Stack:** Java 17, Forge, Mixin, OpenGL ES 3.2 (via ANGLE).

---

### Task 1: Mixin & Core Configuration

**Files:**
- Modify: `build.gradle`
- Create: `src/main/resources/vergium.mixins.json`
- Modify: `src/main/resources/META-INF/mods.toml`

- [ ] **Step 1: Add Mixin to build.gradle**
Add `org.spongepowered.mixin` plugin and configuration.

- [ ] **Step 2: Create Mixin Config**
Create `vergium.mixins.json`.

- [ ] **Step 3: Update mods.toml**
Add the Mixin configuration entry.

- [ ] **Step 4: Commit configuration**
```bash
git add build.gradle src/main/resources/vergium.mixins.json src/main/resources/META-INF/mods.toml
git commit -m "feat: Add Mixin support and core configuration"
```

### Task 2: Off-Heap Memory Manager

**Files:**
- Create: `src/main/java/com/vergium/core/memory/MemoryManager.java`
- Create: `src/main/java/com/vergium/core/memory/NativeBuffer.java`

- [ ] **Step 1: Implement MemoryManager**
Implement a system to allocate `DirectByteBuffers`.

- [ ] **Step 2: Implement NativeBuffer**
A wrapper for native memory with fast vertex data packing.

- [ ] **Step 3: Commit memory core**
```bash
git add src/main/java/com/vergium/core/memory/
git commit -m "feat: Implement off-heap memory management for vertex data"
```

### Task 3: Batching Renderer Integration

**Files:**
- Create: `src/main/java/com/vergium/core/render/VergiumBatchRenderer.java`
- Create: `src/main/java/com/vergium/mixin/MixinLevelRenderer.java`

- [ ] **Step 1: Implement VergiumBatchRenderer**
The drawing engine that collects chunk data and issues optimized draw calls.

- [ ] **Step 2: Hook into rendering pipeline**
Use Mixins to redirect vanilla rendering to our batch engine.

- [ ] **Step 3: Commit rendering engine**
```bash
git add src/main/java/com/vergium/core/render/ src/main/java/com/vergium/mixin/
git commit -m "feat: Integrate batch rendering engine via Mixins"
```
