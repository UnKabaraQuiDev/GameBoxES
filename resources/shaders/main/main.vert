#version 330

layout (location=0) in vec3 positions;
layout (location=1) in vec3 normals;
layout (location=2) in vec2 uvs;

out vec2 texCoord;
out vec3 normal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(positions, 1.0);
    texCoord = uvs;
    normal = normals;
}
