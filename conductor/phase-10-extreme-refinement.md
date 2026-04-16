# Vergium Phase 10: Extreme Refinement & Completion

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Complete skeletal components (EntityInstancer, ModBridge) and fix lingering memory leaks in MemoryManager.

---

### Task 1: Fix MemoryManager Leak

**Files:**
- Modify: `src/main/java/com/vergium/core/memory/MemoryManager.java`

- [ ] **Step 1: Link MemoryManager to ResourceManager**
Ensure that all buffers allocated by `MemoryManager` are tracked by `ResourceManager` or cleared explicitly during cleanup.

- [ ] **Step 2: Commit fix**
```bash
git add src/main/java/com/vergium/core/memory/MemoryManager.java
git commit -m "fix: Resolve MemoryManager buffer leak by ensuring proper cleanup"
```

### Task 2: Complete EntityInstancer Implementation

**Files:**
- Modify: `src/main/java/com/vergium/core/render/EntityInstancer.java`

- [ ] **Step 1: Implement glDrawArraysInstanced logic**
Add the actual OpenGL ES 3.2 calls to render the instanced entities.

- [ ] **Step 2: Commit implementation**
```bash
git add src/main/java/com/vergium/core/render/EntityInstancer.java
git commit -m "feat: Complete EntityInstancer with real OpenGL instancing calls"
```

### Task 3: Finalize ModBridge & MultiBufferSource Integration

**Files:**
- Modify: `src/main/java/com/vergium/core/bridge/ModBridge.java`
- Modify: `src/main/java/com/vergium/mixin/MixinMultiBufferSource.java`

- [ ] **Step 1: Uncomment and fix redirection logic**
Ensure 3rd-party mods are correctly routed to Vergium's optimized buffers.

- [ ] **Step 2: Commit bridge completion**
```bash
git add src/main/java/com/vergium/core/bridge/ModBridge.java src/main/java/com/vergium/mixin/MixinMultiBufferSource.java
git commit -m "feat: Finalize ModBridge to fully optimize 3rd-party mod rendering"
```
