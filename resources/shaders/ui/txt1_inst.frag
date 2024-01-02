#version 400 core

in vec3 normal;
in vec2 texCoord;
in vec3 fragPos;
flat in int index;

out vec4 fragColor;

uniform sampler2D txt1;

void main() {
	fragColor = texture(txt1, texCoord);
}
