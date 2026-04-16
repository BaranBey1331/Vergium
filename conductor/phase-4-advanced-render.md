# Vergium Phase 4: Advanced GPU & Memory Optimization

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Maximize throughput on Xclipse 940 by implementing MDI, Entity Batching, and Shader precision tuning.

**Architecture:** Leverage OpenGL ES 3.1+ extensions for Multi-Draw Indirect and optimized state sorting.

**Tech Stack:** Java 17, Mixin, OpenGL ES 3.2 / GLSL 300 es.

---

### Task 1: Multi-Draw Indirect (MDI) Command Buffer

**Files:**
- Create: `src/main/java/com/vergium/core/render/CommandBuffer.java`
- Modify: `src/main/java/com/vergium/core/render/VergiumBatchRenderer.java`

- [ ] **Step 1: Implement CommandBuffer**
Create a buffer to store draw commands (count, first, etc.) to be used with `glMultiDrawArraysIndirect`.

- [ ] **Step 2: Update VergiumBatchRenderer**
Integrate the command buffer to submit all batched chunks in a single GPU call.

- [ ] **Step 3: Commit MDI logic**
```bash
git add src/main/java/com/vergium/core/render/
git commit -m "feat: Implement Multi-Draw Indirect (MDI) for extreme draw call reduction"
```

### Task 2: Mobile-First Shader Tuning (Precision)

**Files:**
- Modify: `src/main/resources/assets/vergium/shaders/core/position_tex.glslv`
- Create: `src/main/resources/assets/vergium/shaders/core/position_tex.glslf`

- [ ] **Step 1: Optimize Fragment Shaders**
Update fragment shaders to use `mediump` for color and texture coordinates, significantly reducing fragment processor load on mobile.

- [ ] **Step 2: Commit shader tuning**
```bash
git add src/main/resources/assets/vergium/shaders/
git commit -m "perf: Optimize shaders with mediump precision for mobile GPUs"
```

### Task 3: Entity Rendering Optimization

**Files:**
- Create: `src/main/java/com/vergium/mixin/MixinEntityRenderDispatcher.java`
- Modify: `src/main/resources/vergium.mixins.json`

- [ ] **Step 1: Implement Entity Culling/Batching**
Apply the hierarchical culling to entities as well, ensuring we don't animate or draw hidden mobs.

- [ ] **Step 2: Update Mixin config**
Add the new Mixin.

- [ ] **Step 3: Commit entity optimization**
```bash
git add src/main/java/com/vergium/mixin/ src/main/resources/vergium.mixins.json
git commit -m "feat: Extend occlusion culling and batching to entities"
```
