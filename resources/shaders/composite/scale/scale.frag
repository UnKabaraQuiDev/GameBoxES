#version 300 es
precision mediump float;

in vec2 uv;

uniform sampler2D color;
uniform sampler2D depth;

out vec4 frag;

uniform int screen_width;
uniform int screen_height;

uniform int screen_dest_width;
uniform int screen_dest_height;

vec2 snap(vec2 value, vec2 step) {
    return floor(value / step) * step;
}

void main() {
	float offsetX = 1.0/screen_dest_width;
	float offsetY = 1.0/screen_dest_height;
	
	frag = texture(color, snap(uv, vec2(offsetX, offsetY)));
}
