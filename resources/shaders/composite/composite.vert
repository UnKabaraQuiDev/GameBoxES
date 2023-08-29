// CompositeShader.vert (Vertex Shader)
#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 uv;

out vec2 texCoord;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;

void main() {
	gl_Position = modelMatrix * vec4(position, 1.0);
	texCoord = gl_Position.xy/gl_Position.w*0.5+0.5;//(inPosition + 1.0) * 0.5;
}
