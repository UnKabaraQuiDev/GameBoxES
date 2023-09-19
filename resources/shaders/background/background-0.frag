#version 400 core

in vec2 uv;

uniform float hue;

//layout (location=0) out vec4 color;
out vec4 color;

vec3 hue2rgb(float hue) {
	hue = mod(hue, 1);
	float r = abs(hue * 6 - 3) - 1;
	float g = 2 - abs(hue * 6 - 2);
	float b = 2 - abs(hue * 6 - 4);
	vec3 rgb = vec3(r, g, b);
	rgb = clamp(rgb, 0, 1);
	return rgb;
}

void main() {
	color = vec4(hue2rgb(sin(uv.x)+hue) * cos(uv.y), 1.0);
}