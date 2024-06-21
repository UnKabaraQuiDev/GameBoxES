#version 300 es
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;

out vec4 fragColor;

uniform sampler2D txt1;
uniform float progressGreen;
uniform float progressRed;

#define BAR_MAX_HEIGHT 0.2f
#define BAR_X_START 0.3125f
#define BAR_X_END 0.921875f
#define BAR_Y_START 0.075f
#define BAR_Y_END 0.2f
#define BAR_BG vec4(1.0, 1.0, 1.0, 1.0)
#define BAR_RED vec4(1.0, 0.0, 0.0, 1.0)
#define BAR_GREEN vec4(0.0, 1.0, 0.0, 1.0)
#define PIXEL_WIDTH 1.0f/64f
#define PIXEL_HEIGHT 1.0f/40f

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
	
	if(texCoord.y < BAR_MAX_HEIGHT && fragColor.b >= 0.9f) {
		// vec2 rotUv = rotateVec2(snap(texCoord, vec2(1f/64, 1f/20))-vec2(BAR_X_START, BAR_Y_START), radians(-30f))+vec2(BAR_X_START, BAR_Y_START);
		vec2 rotUv = rotateVec2(texCoord-vec2(0.3125f, 0.075f), radians(-30.0))+vec2(0.3125f, 0.075f);
		float intMap = map(rotUv.x, 0.3125f, 0.921875f, 0.0f, 1.0f)*1.3f;
		fragColor = mix(BAR_GREEN, mix(BAR_RED, BAR_BG, intMap > progressRed ? 1.0f : 0.0f), intMap > progressGreen ? 1.0f : 0.0f);
	}
	
	if(fragColor.a == 0.0) {
		discard;
	}
}
