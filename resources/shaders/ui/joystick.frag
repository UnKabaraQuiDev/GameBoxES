#version 300 es
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;

out vec4 fragColor;

uniform vec4 color;
uniform vec2 position;
uniform float radius;
uniform float threshold;
uniform float button;

void main() {
	vec2 correctUv = (texCoord-vec2(0.5, 0.5))*2;
	float leng = length(correctUv);

	if(distance(correctUv, position) < radius+(button*2*radius)) {
		fragColor = color;
		return;
	}
	if(leng < threshold) {
		fragColor = vec4(vec3(0.3), 1);
	}else if(leng < 1+radius/2 && leng > 1-radius/2) {
		fragColor = vec4(vec3(0.8), 1);
	}else {
		discard;
	}
}
