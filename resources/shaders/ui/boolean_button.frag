#version 400 core

in vec3 normal;
in vec2 texCoord;
in vec3 fragPos;

out vec4 fragColor;

uniform vec4 color;
uniform float value;

#define thickness 0.05

void main() {
	vec2 correctUv = (texCoord-vec2(0.5, 0.5))*2;
	vec2 absoluteUv = abs(correctUv);

	if(value != 0) {
		fragColor = color;
	} else if(abs(absoluteUv.x-1) < thickness || abs(absoluteUv.y-1) < thickness) {
		fragColor = vec4(1);
	} else {
		discard;
	}
}
