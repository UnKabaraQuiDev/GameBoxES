#version 300 es
precision mediump float;

in vec2 uv;

/*layout(binding = 0, rgba) */uniform sampler2D input;
/*layout(binding = 0, r32f) */uniform sampler1D depth;

out vec4 color;

uniform vec4 mul;
uniform vec4 add;

void main() {
	color = texture(input, uv) * mul + add;
}