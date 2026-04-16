# Design Specification: Vergium Optimization Mod

## 1. Introduction
Vergium is a high-performance optimization mod for Minecraft Forge 1.20.1, specifically engineered for Android devices using the Samsung Xclipse 940 GPU (AMD RDNA 3 architecture) through the ANGLE (Vulkan 1.3) translation layer. It aims to maximize FPS and minimize rendering overhead in launchers like PojavLauncher and Zalith Launcher.

## 2. Goals & Success Criteria
- **Target Platform:** Forge 1.20.1, Xclipse 940 GPU (OpenGL ES 3.2 via Vulkan 1.3).
- **Primary Objective:** Reduce CPU and GPU bottlenecks caused by the ANGLE translation layer.
- **Success Criteria:**
  - Significant reduction in Draw Calls.
  - Improved chunk update speeds.
  - Stable FPS on high-end Android hardware.
  - Automated CI/CD for `.jar` builds.

## 3. Architecture & Technical Strategy

### 3.1. Batching & Draw Call Reduction (Phase 1)
- **Problem:** Every OpenGL draw call in Java must be translated by ANGLE into Vulkan commands, causing heavy CPU overhead.
- **Solution:** Implement a custom **Vertex Buffer Object (VBO)** management system.
  - Group block geometry into large contiguous buffers.
  - Use **Multi-Draw-Indirect (MDI)** if supported, or smart batching to submit multiple chunks in a single GPU command.
  - Minimize state changes (texture binds, shader swaps) by sorting render passes.

### 3.2. Hardware-Accelerated Occlusion Culling (Phase 2)
- **Problem:** Rendering hidden blocks wastes GPU cycles and memory bandwidth.
- **Solution:** Implement **Hierarchical Occlusion Culling**.
  - Use a low-resolution depth buffer to quickly test if a chunk is visible.
  - Leverage Xclipse 940's hardware occlusion queries if efficient, or perform fast software-based frustum culling on the CPU.
  - "Smart Culling": Prioritize culling logic that removes "gereksiz" (unnecessary) geometry before it reaches the GPU pipeline.

### 3.3. Memory & Data Optimization (Phase 3)
- **Problem:** Mobile GPUs are often bandwidth-constrained.
- **Solution:** **Vertex Data Packing**.
  - Compress vertex data (position, UV, normal) into smaller bit-fields (e.g., using `byte` or `short` instead of `float` where possible).
  - Use **Off-heap Memory Management** (`DirectByteBuffers`) to bypass Java GC overhead and allow faster data transfer to the GPU.
  - Optimize shader code (GLSL) specifically for RDNA 3 architecture, avoiding branches and complex math in the fragment shader.

## 4. Implementation Details

### 4.1. Core Components
- `VergiumRenderer`: The main engine replacing parts of the vanilla Forge/Minecraft rendering pipeline.
- `BufferOptimizer`: Manages memory allocation and vertex packing.
- `CullingManager`: Handles frustum and occlusion tests.

### 4.2. Build System
- **GitHub Actions:** A `.yml` workflow to compile the Forge mod using Gradle and upload the resulting `.jar` as a release artifact.

## 5. Security & Stability
- Avoid using unstable experimental OpenGL features that might crash the ANGLE driver.
- Ensure compatibility with standard Forge 1.20.1 mods.

## 6. Self-Review Notes
- **Ambiguity:** ANGLE performance can vary; we must benchmark different buffer upload strategies (e.g., `glBufferSubData` vs `glMapBufferRange`).
- **Scope:** Initial focus is strictly on rendering; lighting and physics optimization are secondary.
- **Dependencies:** Minimize dependencies to keep the mod lightweight.
