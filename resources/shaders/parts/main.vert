#version 330

layout (location=0) in vec3 positions;
layout (location=1) in vec3 normals;
layout (location=2) in vec2 uvs;
layout (location=3) in mat4 transform;

out vec2 texCoord;
out vec3 normal;
out int index;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * (transformationMatrix * transform) * vec4(positions, 1.0);
    texCoord = uvs;
    normal = normals;
}
