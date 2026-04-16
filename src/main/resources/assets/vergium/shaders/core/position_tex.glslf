#version 300 es
precision mediump float; // Standard for mobile GPU efficiency

in vec2 texCoord;
in vec4 vertexColor;

uniform sampler2D Sampler0;

out vec4 fragColor;

void main() {
    // Fast path for mobile GPUs (Xclipse 940 / RDNA 3)
    vec4 texColor = texture(Sampler0, texCoord);
    
    // Discard transparent fragments early to save bandwidth
    if (texColor.a < 0.1) {
        discard;
    }
    
    fragColor = texColor * vertexColor;
}
