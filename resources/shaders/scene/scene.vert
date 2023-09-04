#version 330

layout (location=0) in vec3 position;
layout (location=2) in vec2 uv;

out vec2 texCoord;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform float t;

void main()
{
    gl_Position = projectionMatrix * modelMatrix * vec4(position, 1.0);
    texCoord = uv;
}
