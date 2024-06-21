#version 300 es
precision mediump float;

in vec2 uv;

/*layout(binding = 0, rgba) */uniform sampler2D img;
/*layout(binding = 0, r32f) */uniform sampler1D depth;

out vec4 color;

#define MAX 5*5
uniform float kernel[MAX];
uniform int height;
uniform int width;

uniform int screen_width;
uniform int screen_height;

void main() {
	//color = texture(in, uv);
	
	/*vec3 result = vec3(0, 1, 0);//texture(img, uv);
	for(int x = 0; x < 5; x++) {
		for(int y = 0; y < 5; y++) {
			vec2 coord = gl_FragCoord.xy;
			result += vec3(kernel[int(y*width+x)]) * texture(img, uv+(vec2(-float(width)/2+float(x), -float(height)/2+float(y))/vec2(screen_width, screen_height))).rgb;
		}
	}*/
	//result /= float(width*height);
	//color = vec4(result.rgb, 1);//vec4(uv+(vec2(1, 1)/vec2(screen_width, screen_height)), 0, 1);
	
	//color = vec4(texture(img, gl_FragCoord.xy/vec2(screen_width, screen_height)+vec2(0.1, 0.05)).rgb, 1);
	
	//color = vec4(texture(img, nuv).rgb, 1);
}