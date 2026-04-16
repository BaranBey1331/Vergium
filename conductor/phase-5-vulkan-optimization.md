# Vergium Phase 5: Major Vulkan-Specific Optimization

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Overhaul the rendering pipeline to be "Vulkan-Native" friendly, specifically targeting the ANGLE overhead on Xclipse 940.

**Architecture:** Implement Uniform Buffer Objects (UBO), State Sorting, and Circular Buffer Streaming to minimize Vulkan pipeline stalls.

**Tech Stack:** Java 17, Mixin, OpenGL ES 3.2 (MDI, UBO, SSBO).

---

### Task 1: Uniform Buffer Object (UBO) Management

**Files:**
- Create: `src/main/java/com/vergium/core/render/UniformBufferManager.java`

- [ ] **Step 1: Implement UBO Manager**
Create a manager that stores ProjMat, ModelViewMat, and LightData in a single `GL_UNIFORM_BUFFER`. This allows updating all shader data with a single `glBufferSubData` call, which ANGLE maps directly to a Vulkan Descriptor Set update.

- [ ] **Step 2: Commit UBO system**
```bash
git add src/main/java/com/vergium/core/render/UniformBufferManager.java
git commit -m "feat: Implement Uniform Buffer Objects (UBO) for zero-overhead matrix updates"
```

### Task 2: Advanced State Sorting Engine

**Files:**
- Create: `src/main/java/com/vergium/core/render/StateSorter.java`
- Modify: `src/main/java/com/vergium/core/render/VergiumBatchRenderer.java`

- [ ] **Step 1: Implement StateSorter**
Sort draw calls by Shader ID -> Texture ID -> Blending Mode. This minimizes Vulkan pipeline re-creations in the ANGLE layer.

- [ ] **Step 2: Integrate with Batch Renderer**
Redirect batch submission to the `StateSorter` before flushing to the GPU.

- [ ] **Step 3: Commit state sorting**
```bash
git add src/main/java/com/vergium/core/render/
git commit -m "perf: Add State Sorting Engine to minimize Vulkan pipeline transitions"
```

### Task 3: Circular Buffer Streaming (Persistent Mapping Emulation)

**Files:**
- Modify: `src/main/java/com/vergium/core/memory/MemoryManager.java`

- [ ] **Step 1: Implement Circular Buffering**
Instead of re-allocating or clearing buffers, use a large pre-allocated ring buffer. Use `glBufferSubData` at different offsets to prevent CPU waiting for GPU (fences).

- [ ] **Step 2: Commit streaming logic**
```bash
git add src/main/java/com/vergium/core/memory/MemoryManager.java
git commit -m "feat: Implement Circular Buffer Streaming for non-blocking Vulkan uploads"
```
