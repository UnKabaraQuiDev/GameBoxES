#version 300 es
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;

out vec4 fragColor;

uniform vec4 color;
uniform float value;
uniform float radius;
uniform float threshold;

void main() {
	float uv = texCoord.y;

	if(abs(uv-value) < radius/2) {
		fragColor = color;
		return;
	}

	if(abs(texCoord.x-0.5) < radius) {
		fragColor = vec4(vec3(0.8), 1);
	}else if (uv < threshold) {
		fragColor = vec4(vec3(0.2), 1);
	}else {
		discard;
	}
	
	// fragColor += vec4(texCoord, 0, 1);

	//fragColor = vec4(uv, abs(uv-value) < radius/2, abs(texCoord.x-0.5)<radius, 1);
}
