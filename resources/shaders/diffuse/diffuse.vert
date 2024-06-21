#version 300 es
precision mediump float;

layout (location=0) in vec3 positions;
layout (location=1) in vec3 normals;
layout (location=2) in vec2 uvs;

out vec2 texCoord;
out vec3 normal;
out vec3 fragPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main()
{
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(positions, 1.0);
	fragPos = (transformationMatrix * vec4(positions, 1.0)).xyz;
	texCoord = uvs;
	normal = mat3(transpose(inverse(transformationMatrix))) * normals;
}
