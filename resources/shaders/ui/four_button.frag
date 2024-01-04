#version 400 core

in vec3 normal;
in vec2 texCoord;
in vec3 fragPos;

out vec4 fragColor;

uniform vec4 buttons;

#define size 0.33
#define thickness 0.03
#define dist 0.6

void main() {
	vec2 correctUv = (texCoord-vec2(0.5, 0.5))*vec2(2, -2);
	vec2 absoluteUv = abs(correctUv);

	float yRadialMask = distance(absoluteUv, vec2(0, dist));
	float xRadialMask = distance(absoluteUv, vec2(dist, 0));
	bool yCircleMask = yRadialMask < size;
	bool xCircleMask = xRadialMask < size;
	bool xMask = correctUv.x > 0;
	bool yMask = correctUv.y > 0;

	if(xRadialMask < size+thickness/2) {
		if((xMask && buttons.x > 0) || (!xMask && buttons.z > 0)) {
			fragColor = vec4(1);
			return;
		}else if(abs(xRadialMask-size) < thickness/2) {
			fragColor = vec4(1);
			return;
		}
	}else if(yRadialMask < size+thickness/2) {
		if((yMask && buttons.w > 0) || (!yMask && buttons.y > 0)) {
			fragColor = vec4(1);
			return;
		}else if(abs(yRadialMask-size) < thickness/2) {
			fragColor = vec4(1);
			return;
		}
	}

	discard;
}
