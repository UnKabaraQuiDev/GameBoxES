#version 400 core

in vec2 uv;

uniform sampler2D color;
uniform sampler2D depth;

out vec4 frag;

uniform int screen_width;
uniform int screen_height;

uniform float min_lum;

float computeBrightness(vec4 color) {
    float luminance = 0.2126 * color.r + 0.7152 * color.g + 0.0722 * color.b;
    return luminance;
}

void main() {
	vec4 col = texture(color, uv);
	if(computeBrightness(col) >= min_lum) {
		frag = col;
	}else {
		frag = vec4(0);
	}
}
