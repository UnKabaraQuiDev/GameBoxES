#version 400 core

in vec2 uv;

uniform sampler2D color;
uniform sampler2D depth;

out vec4 frag;

uniform int screen_width;
uniform int screen_height;

void main() {
	float offsetX = 1.0/screen_width;
	float offsetY = 1.0/screen_height;

	vec3 sa = vec3(0);

	for(int x = -2; x <= 2; x++) {
		for(int y = -2; y <= 2; y++) {
			sa = sa + texture(color, uv+vec2(x*offsetX, y*offsetY)).rgb;
		}
	}

	sa = sa /25;

	frag = vec4(sa, 1);//vec4(1)/*-vec4(vec3(texture(color, uv)), 0)*/-vec4(vec3(texture(depth, uv).r), 0);
}
