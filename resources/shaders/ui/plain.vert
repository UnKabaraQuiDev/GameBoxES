#version 400 core

layout (location = 0) in vec2 i_pos;
layout (location = 1) in vec3 i_norm;
layout (location = 2) in vec2 i_uv;

out vec2 texCoord;
out vec3 normal;
out vec2 fragPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat3x2 transformationMatrix;

void main() {
	mat3x3 transformation3x3 = mat3x3(
	    transformationMatrix[0][0], transformationMatrix[0][1], 0.0,
	    transformationMatrix[1][0], transformationMatrix[1][1], 0.0,
	    transformationMatrix[2][0], transformationMatrix[2][1], 1.0
	);
	
	gl_Position = projectionMatrix * viewMatrix * mat4(transformation3x3) * vec4(i_pos, 1.0, 1.0);
	fragPos = i_pos.xy;
	texCoord = i_uv;
	normal = i_norm;
}