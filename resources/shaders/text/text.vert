#version 400 core

layout(location = 0) in vec3 pos;
layout(location = 1) in int index;

flat out int VertexIndex;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main() {
	gl_Position = vec4(pos, 1);
	VertexIndex = index;
}