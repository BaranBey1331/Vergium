# Vergium Phase 7: Leak-Free Pipeline & Entity Instancing

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Eliminate memory/cache leaks and optimize entity rendering using instancing.

**Architecture:** Implement a central `ResourceManager` for GL objects and a specialized `EntityInstancer`.

**Tech Stack:** Java 17, Mixin, OpenGL ES 3.2.

---

### Task 1: Centralized Resource Manager (Leak Prevention)

**Files:**
- Create: `src/main/java/com/vergium/core/render/ResourceManager.java`
- Modify: `src/main/java/com/vergium/Vergium.java`

- [ ] **Step 1: Implement ResourceManager**
Track every GL object ID (Buffer, Shader, Query, VAO). Provide a `cleanup()` method to be called during world unloads or game exit.

- [ ] **Step 2: Hook into FML Lifecycle**
Ensure `ResourceManager.cleanup()` is called when the game shuts down or world changes.

- [ ] **Step 3: Commit resource manager**
```bash
git add src/main/java/com/vergium/core/render/ResourceManager.java src/main/java/com/vergium/Vergium.java
git commit -m "feat: Add Centralized ResourceManager to prevent GL object leaks"
```

### Task 2: Entity Instancing Engine

**Files:**
- Create: `src/main/java/com/vergium/core/render/EntityInstancer.java`
- Modify: `src/main/java/com/vergium/mixin/MixinEntityRenderDispatcher.java`

- [ ] **Step 1: Implement EntityInstancer**
Group entities by type and state. Use `glDrawArraysInstanced` to render multiple entities in a single call.

- [ ] **Step 2: Update MixinEntityRenderDispatcher**
Redirect repetitive entity renders to the `EntityInstancer`.

- [ ] **Step 3: Commit instancing engine**
```bash
git add src/main/java/com/vergium/core/render/EntityInstancer.java src/main/java/com/vergium/mixin/MixinEntityRenderDispatcher.java
git commit -m "perf: Implement Entity Instancing for massive performance gain with mobs"
```

### Task 3: Cache & Memory Leak Audit

**Files:**
- Modify: `src/main/java/com/vergium/core/memory/MemoryManager.java`
- Modify: `src/main/java/com/vergium/core/render/OcclusionQueryManager.java`

- [ ] **Step 1: Refine MemoryManager**
Implement `AutoCloseable` for buffers and ensure `System.gc()` is hinted only when necessary to clean up direct buffers.

- [ ] **Step 2: Audit OcclusionQueryManager**
Ensure all generated queries are eventually deleted via `glDeleteQueries` during cleanup.

- [ ] **Step 3: Commit leak fixes**
```bash
git add src/main/java/com/vergium/core/memory/ src/main/java/com/vergium/core/render/
git commit -m "fix: Final memory/cache leak audit and hardening"
```
