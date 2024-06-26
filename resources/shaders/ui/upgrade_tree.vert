#version {version}
precision mediump float;

layout (location = 0) in vec3 i_pos;
layout (location = 1) in vec3 i_norm;
layout (location = 2) in vec2 i_uv;

out vec2 texCoord;
out vec3 normal;
out vec2 fragPos;
flat out int applyIcon;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

uniform vec2 progress;

#define START_1 vec2(0.060, 0.250)
#define END_1 vec2(0.070, 0.270)

#define START_2 vec2(0.060, 0.280)
#define END_2 vec2(0.070, 0.300)

#define SWIPE_END 0.020

void main() {
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(i_pos, 1.0);
	fragPos = i_pos.xy;
	applyIcon = 0;
	if(i_uv.x > START_1.x && i_uv.y > START_1.y && i_uv.x < END_1.x && i_uv.y < END_1.y) {
		texCoord = i_uv - vec2(SWIPE_END, 0.0) * progress.x;
	} else if(i_uv.x > START_2.x && i_uv.y > START_2.y && i_uv.x < END_2.x && i_uv.y < END_2.y) {
		texCoord = i_uv - vec2(SWIPE_END, 0.0) * progress.y;
	} else {
		texCoord = i_uv;
		applyIcon = 1;
	}
	normal = i_norm;
}
