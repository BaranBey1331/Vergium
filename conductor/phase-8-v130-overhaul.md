# Vergium Phase 8: v1.3.0 Major Overhaul (Vulkan-First Architecture)

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Complete rewrite of the rendering pipeline for high-tier Vulkan/Xclipse 940 optimization and zero-leak reliability.

**Architecture:** Multi-architecture "Fast-Path" pipeline with pool-based memory management and universal mod-bridging.

**Tech Stack:** Java 17, Mixin, OpenGL ES 3.2, Vulkan 1.3 (via ANGLE).

---

### Task 1: Multi-Architecture Pipeline Manager

**Files:**
- Create: `src/main/java/com/vergium/core/pipeline/PipelineManager.java`
- Create: `src/main/java/com/vergium/core/pipeline/VulkanFastPath.java`
- Create: `src/main/java/com/vergium/core/pipeline/GenericPath.java`

- [ ] **Step 1: Implement Pipeline Selection**
Detect GPU capabilities (specifically Xclipse 940) and select the most efficient rendering path.

- [ ] **Step 2: Implement VulkanFastPath**
A pipeline specifically for ANGLE on RDNA 3, using Persistent Mapping and SSBOs (if available) or optimized UBOs.

- [ ] **Step 3: Commit architecture manager**
```bash
git add src/main/java/com/vergium/core/pipeline/
git commit -m "feat: Implement Multi-Architecture Rendering Pipeline Manager"
```

### Task 2: Zero-Allocation Buffer Pooling (Leak-Free)

**Files:**
- Modify: `src/main/java/com/vergium/core/memory/MemoryManager.java`
- Create: `src/main/java/com/vergium/core/memory/BufferPool.java`

- [ ] **Step 1: Implement BufferPool**
Create a system that recycles `DirectByteBuffers` instead of allocating new ones every frame. This eliminates GC stutters and leaks.

- [ ] **Step 2: Refine MemoryManager**
Switch to the pooling system for all vertex data.

- [ ] **Step 3: Commit leak-free pooling**
```bash
git add src/main/java/com/vergium/core/memory/
git commit -m "feat: Implement Zero-Allocation Buffer Pooling for leak-free rendering"
```

### Task 3: Universal Mod-Optimization Bridge

**Files:**
- Create: `src/main/java/com/vergium/core/bridge/ModBridge.java`
- Modify: `src/main/java/com/vergium/mixin/MixinMultiBufferSource.java`

- [ ] **Step 1: Implement ModBridge**
A centralized class that classifies draw calls from other mods and batches them together by state.

- [ ] **Step 2: Update MixinMultiBufferSource**
Redirect ALL mod rendering through the `ModBridge` to ensure no mod skips the optimization pipeline.

- [ ] **Step 3: Commit mod-bridge**
```bash
git add src/main/java/com/vergium/core/bridge/ src/main/java/com/vergium/mixin/
git commit -m "feat: Add Universal Mod-Optimization Bridge for heavy mod packs"
```

### Task 4: Final v1.3.0 Polish & Changelogs

**Files:**
- Modify: `changelogs.md`
- Modify: `README.md`

- [ ] **Step 1: Final Changelog Update**
Document v1.3.0 as the definitive major update.

- [ ] **Step 2: Final commit**
```bash
git add .
git commit -m "docs: Finalize v1.3.0 Major Update documentation"
```
