# Vergium Phase 9: Critical Hardening & Audit Fixes

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Resolve all bugs and risks identified in the code audit (Memory leaks, thread safety, non-functional culling, MDI completion).

---

### Task 1: Thread-Safe Buffer Management

**Files:**
- Modify: `src/main/java/com/vergium/core/render/VergiumBatchRenderer.java`

- [ ] **Step 1: Use ConcurrentHashMap**
Replace `HashMap` with `ConcurrentHashMap` to allow safe access from chunk worker threads.

- [ ] **Step 2: Commit thread safety**
```bash
git add src/main/java/com/vergium/core/render/VergiumBatchRenderer.java
git commit -m "fix: Ensure thread safety in batch renderer to prevent crashes during chunk building"
```

### Task 2: Real Visibility Culling (Actuator)

**Files:**
- Modify: `src/main/java/com/vergium/mixin/MixinRenderSection.java`

- [ ] **Step 1: Implement Cancellable Culling**
Inject into the section compilation/rendering methods and cancel them if the chunk is not visible.

- [ ] **Step 2: Commit real culling**
```bash
git add src/main/java/com/vergium/mixin/MixinRenderSection.java
git commit -m "fix: Actually cancel rendering for hidden sections (Culling Actuator)"
```

### Task 3: Automatic Memory Wipe (Unload Hooks)

**Files:**
- Modify: `src/main/java/com/vergium/Vergium.java`
- Modify: `src/main/java/com/vergium/core/memory/MemoryManager.java`

- [ ] **Step 1: Add LevelEvent.Unload listener**
Hook into Forge's world unload event to clear all static caches.

- [ ] **Step 2: Refine Cleanup Logic**
Move `ResourceManager.cleanup()` from JVM shutdown to the main thread during world exit.

- [ ] **Step 3: Commit memory wipe**
```bash
git add src/main/java/com/vergium/Vergium.java src/main/java/com/vergium/core/memory/MemoryManager.java
git commit -m "fix: Implement automatic memory wipe on world unload to prevent OOM leaks"
```

### Task 4: Complete MDI Path for Xclipse 940

**Files:**
- Modify: `src/main/java/com/vergium/core/pipeline/VulkanFastPath.java`

- [ ] **Step 1: Implement glMultiDrawArraysIndirect**
Replace the placeholder with actual Indirect Drawing logic.

- [ ] **Step 2: Commit MDI**
```bash
git add src/main/java/com/vergium/core/pipeline/VulkanFastPath.java
git commit -m "feat: Complete Multi-Draw Indirect implementation for real RDNA 3 gains"
```
