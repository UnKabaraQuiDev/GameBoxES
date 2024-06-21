#version 300 es
precision mediump float;

in vec2 uv;

out vec4 color;

#define MAX 60

uniform double dfps[MAX];
uniform double dups[MAX];
uniform double tfps[MAX];
uniform double tups[MAX];

float map(float value, float min1, float max1, float min2, float max2) {
	return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

void main() {
	
	if(uv.x > -1 && uv.x < 0) {
		float x = map(uv.x, -1, 0, 0, 1);
		int down = int(floor(x*MAX));
		float diff = x - float(down);
		float value = mix(float(dfps[down]), float(dfps[down+1]), diff);
		
		color = vec4(uv.y > 0 && uv.y < (value/(1000.0/60.0*2.0)), 0, 0, 1);
	}else if(uv.x > 0 && uv.x < 1) {
		float x = map(uv.x, -1, 0, 0, 1);
		int down = int(floor(x*MAX));
		float diff = x - float(down);
		float value = mix(float(dups[down]), float(dups[down+1]), diff);
		
		color += vec4(0, uv.y > 0 && uv.y < (value/(1000.0/30.0)), 0, 1);
	}
	
	//color = vec4(uv.x < 0 && uv.x > -1, uv.y > 0 && uv.y < 0.5, 0, 1);
	
}