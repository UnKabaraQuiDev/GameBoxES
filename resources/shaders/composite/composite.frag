// CompositeShader.frag (Fragment Shader)
#version 330 core

in vec2 texCoord;

out vec4 fragColor;

uniform sampler2D render;

void main() {
	fragColor = mix(texture(render, texCoord), vec4(0.8, 0.2, 0.2, 1.0), texCoord.y);
}
