#version 400 core

layout (location=0) in vec3 i_pos;
layout (location=1) in vec2 i_uv;

out vec2 fragCoord;

void main() {
	gl_Position = vec4(i_pos, 1);
	fragCoord = i_uv;
}