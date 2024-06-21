#version 300 es
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;
flat in int applyIcon;

out vec4 fragColor;

uniform sampler2D txt1;

uniform vec2 progress;
uniform vec2 icons;

#define QUAD_START vec2(0.015f, 0.146f)
#define QUAD_END vec2(0.085f, 0.854f)

vec2 map(vec2 value, vec2 min1, vec2 max1, vec2 min2, vec2 max2) {
	return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

void main() {
	vec4 bgColor = texture(txt1, texCoord);

	if(applyIcon == 0) {
		fragColor = bgColor;
		return;
	}
	
	vec4 fgColor;
	if(fragPos.y > 0.0) {
		fgColor = texture(txt1, mix(1.0/vec2(10.0, 1.0)*icons.y, 1.0/vec2(10.0, 1.0)*(icons.y+1.0), map(texCoord, QUAD_START, QUAD_END, vec2(0.0), vec2(1.0))));
	}else {
		fgColor = texture(txt1, mix(1.0/vec2(10.0, 1.0)*icons.y, 1.0/vec2(10.0, 1.0)*(icons.x+1.0), map(texCoord, QUAD_START, QUAD_END, vec2(0.0), vec2(1.0))));
	}
	
	fragColor = vec4(mix(bgColor, fgColor, fgColor.a).rgb, max(fgColor.a, bgColor.a));
}
