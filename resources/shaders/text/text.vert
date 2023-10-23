#version 400 core

layout(location = 0) in vec3 pos;
layout(location = 1) in int index;
layout(location = 2) in vec2 uvs;

flat out int QuadIndex;
out vec2 texCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main() {
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(pos, 1);
	QuadIndex = index;
	texCoord = uvs;
}