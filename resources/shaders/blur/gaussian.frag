#version 400 core

in vec2 uv;

uniform sampler2D input;
uniform sampler1D depth;

out vec4 color;

#define MAX 5*5
uniform float kernel[MAX];
uniform int height;
uniform int width;

void main() {
	//color = texture(input, uv);
	
	vec4 result = texture(input, uv);
	for(float x = 0; x < float(width); x++) {
		for(float y = 0; y < float(height); y++) {
			result = kernel[int(x*width+y)] * texture(input, uv+vec2(-float(width)/2+x, -float(height)/2+y));
		}
	}
	
	color = result;
}