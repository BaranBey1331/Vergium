# Vergium Phase 2 Implementation Plan: Occlusion Culling

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement hardware-accelerated occlusion culling to prevent the Xclipse 940 GPU from wasting cycles on hidden geometry.

**Architecture:** Use OpenGL ES 3.2 Occlusion Queries combined with a hierarchical visibility check.

**Tech Stack:** Java 17, Forge, Mixin, OpenGL ES 3.2.

---

### Task 1: Occlusion Query Manager

**Files:**
- Create: `src/main/java/com/vergium/core/render/OcclusionQueryManager.java`

- [ ] **Step 1: Implement OcclusionQueryManager**
Create a class to manage `glGenQueries`, `glBeginQuery`, and `glGetQueryObjectuiv`. It should handle a pool of queries to avoid performance hits from constant allocation.

- [ ] **Step 2: Commit infrastructure**
```bash
git add src/main/java/com/vergium/core/render/OcclusionQueryManager.java
git commit -m "feat: Add Occlusion Query management infrastructure"
```

### Task 2: Visibility & Frustum Culling

**Files:**
- Create: `src/main/java/com/vergium/core/render/VisibilityManager.java`

- [ ] **Step 1: Implement VisibilityManager**
Implement a hierarchical visibility test. It should quickly check if a chunk is within the camera frustum before even attempting an occlusion query.

- [ ] **Step 2: Commit visibility logic**
```bash
git add src/main/java/com/vergium/core/render/VisibilityManager.java
git commit -m "feat: Implement hierarchical visibility manager"
```

### Task 3: Render Integration (The "Gereksiz" Remover)

**Files:**
- Modify: `src/main/java/com/vergium/mixin/MixinSectionRenderDispatcher.java` (Create if not exists)
- Modify: `src/main/resources/vergium.mixins.json`

- [ ] **Step 1: Create/Update MixinSectionRenderDispatcher**
Inject into the chunk rendering logic to skip "gereksiz" (unnecessary) draw calls if the `VisibilityManager` or `OcclusionQueryManager` determines the chunk is hidden.

- [ ] **Step 2: Update Mixin config**
Add the new Mixin to `vergium.mixins.json`.

- [ ] **Step 3: Commit integration**
```bash
git add src/main/java/com/vergium/mixin/ src/main/resources/vergium.mixins.json
git commit -m "feat: Integrate occlusion culling into the chunk rendering pipeline"
```
