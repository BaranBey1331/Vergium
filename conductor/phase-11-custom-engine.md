# Vergium Phase 11: v2.0.0 Custom Engine Overhaul (Direct-to-GPU Pipeline)

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace Minecraft's default chunk building and meshing with Vergium's custom high-performance system.

**Architecture:** Custom Mesh System -> Vergium Render Engine -> Vulkan/Xclipse 940 (Bypassing vanilla vertex consumers).

---

### Task 1: Vergium Custom Mesh Builder

**Files:**
- Create: `src/main/java/com/vergium/core/engine/VergiumMeshBuilder.java`
- Create: `src/main/java/com/vergium/core/engine/VertexFormat.java`

- [ ] **Step 1: Define Optimized Vertex Format**
Create a compact vertex format (16-24 bytes) that Xclipse 940 can process with minimal bandwidth.

- [ ] **Step 2: Implement MeshBuilder**
Create a builder that writes block data directly into `NativeBuffer` without using intermediate objects.

- [ ] **Step 3: Commit mesh core**
```bash
git add src/main/java/com/vergium/core/engine/
git commit -m "feat: Implement Vergium Custom Mesh Builder for direct-to-GPU data flow"
```

### Task 2: Chunk Build Replacement (The Replacement Layer)

**Files:**
- Modify: `src/main/java/com/vergium/mixin/MixinSectionRenderDispatcher.java` (Rename or update existing)
- Create: `src/main/java/com/vergium/core/engine/VergiumChunkBuilder.java`

- [ ] **Step 1: Implement VergiumChunkBuilder**
A high-speed alternative to Forge's chunk builder that uses our `VergiumMeshBuilder`.

- [ ] **Step 2: Hook into Chunk Tesselation**
Use Mixins to replace the `compile` process in `RenderSection` with our custom builder.

- [ ] **Step 3: Commit engine replacement**
```bash
git add src/main/java/com/vergium/mixin/ src/main/java/com/vergium/core/engine/
git commit -m "feat: Replace vanilla chunk compilation with Vergium Engine"
```

### Task 3: Direct Render Dispatcher

**Files:**
- Create: `src/main/java/com/vergium/core/engine/VergiumRenderDispatcher.java`
- Modify: `src/main/java/com/vergium/core/render/VergiumBatchRenderer.java`

- [ ] **Step 1: Implement RenderDispatcher**
The brain that manages when and how the custom meshes are sent to the `VulkanFastPath`.

- [ ] **Step 2: Complete Engine Integration**
Ensure all chunk drawing bypasses vanilla's `BufferUploader`.

- [ ] **Step 3: Commit dispatcher**
```bash
git add src/main/java/com/vergium/core/engine/
git commit -m "feat: Implement Vergium Render Dispatcher for direct pipeline management"
```
