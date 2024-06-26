#version {version}
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;

out vec4 fragColor;

uniform sampler2D txt1;
uniform float progressGreen;
uniform float progressRed;

#define BAR_MAX_HEIGHT 0.0
#define BAR_X_START 0.017
#define BAR_X_END 0.63
#define BAR_Y_START 0.0
#define BAR_Y_END 0.0
#define BAR_BG vec4(1.0, 1.0, 1.0, 1.0)
#define BAR_RED vec4(1.0, 0.0, 0.0, 1.0)
#define BAR_GREEN vec4(0.0, 1.0, 0.0, 1.0)
#define PIXEL_WIDTH 5.0
#define PIXEL_HEIGHT 3.0

vec2 snap(vec2 x, vec2 interval) {
	return round(x / interval) * interval;
}

float map(float value, float min1, float max1, float min2, float max2) {
  return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

vec2 rotateVec2(vec2 v, float angle) {
	float c = cos(angle);
	float s = sin(angle);
	mat2 rotationMatrix = mat2(c, -s, s, c);
	return rotationMatrix * v;
}

void main() {
	fragColor = texture(txt1, texCoord);
	
	if(texCoord.y < BAR_MAX_HEIGHT && fragColor.b >= 0.0) {
		// vec2 rotUv = rotateVec2(snap(texCoord, vec2(.0/PIXEL_WIDTH, .0/PIXEL_HEIGHT))-vec2(BAR_X_START, BAR_Y_START), radians(-3.0))+vec2(BAR_X_START, BAR_Y_START);
		vec2 rotUv = rotateVec2(texCoord-vec2(BAR_X_START, BAR_Y_START), radians(-30.0))+vec2(BAR_X_START, BAR_Y_START);
		float intMap = map(rotUv.x, BAR_X_START, BAR_X_END, 0.0, 1.0)*1.0;
		fragColor = mix(BAR_GREEN, mix(BAR_RED, BAR_BG, intMap > progressRed ? 1.0 : 0.0), intMap > progressGreen ? 1.0 : 0.0);
	}
	
	if(fragColor.a == 0.0) {
		discard;
	}
}
