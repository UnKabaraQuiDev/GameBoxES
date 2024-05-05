#version 400 core

in vec2 uv;

uniform sampler2D color;
uniform sampler2D depth;
uniform sampler2D txt1;

out vec4 frag;

uniform int screen_width;
uniform int screen_height;

uniform vec3 viewPos;

void main() {
	frag = vec4(uv, 0, 1);
}
