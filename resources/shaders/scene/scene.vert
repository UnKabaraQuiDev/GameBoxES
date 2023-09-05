#version 330

layout (location=0) in vec3 position;
layout (location=2) in vec2 uv;

out vec2 texCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
    texCoord = uv;
}
