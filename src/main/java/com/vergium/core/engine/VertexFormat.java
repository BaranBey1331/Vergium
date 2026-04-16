package com.vergium.core.engine;

/**
 * Defines a compact vertex format specifically for Xclipse 940 (Vulkan).
 * Uses only 16 bytes per vertex: Pos (3xFloat = 12b) + UV (2xShort = 4b).
 */
public class VertexFormat {
    public static final int STRIDE = 16;
    
    // Using simple packing to save bandwidth on mobile
    public static final int OFFSET_POS = 0;
    public static final int OFFSET_UV = 12;
}
