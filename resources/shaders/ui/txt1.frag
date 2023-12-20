#version 400 core

in vec3 normal;
in vec2 texCoord;
in vec3 fragPos;

out vec4 fragColor;

uniform sampler2D txt1;
uniform vec3 bgColor;
uniform vec3 fgColor;

void main() {
	vec4 col = texture(txt1, texCoord);
	fragColor = vec4(1, 0.2, 0.2, 1);//vec4(col.r*fgColor + col.g*bgColor, col.a);
}
