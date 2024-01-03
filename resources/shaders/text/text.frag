#version 400 core

flat in struct per_instance {
	uint index;
	uint character;
} instance;

in struct per_Vertex {
	vec3 worldpos;
	vec2 fragpos;
	vec2 uv;
	vec3 normal;
} vertex;

#define CHAR_START 32.0
#define CHAR_COUNT 95.0 //255
//#define CHAR_WIDTH_COUNT 20.0
//#define CHAR_HEIGHT_COUNT 5.0

out vec4 fragColor;

uniform sampler1D lookup;
uniform sampler2D txt1;
uniform vec4 bgColor;
uniform vec4 fgColor;
uniform int length;

void main() {
	if (instance.index >= length) {
		discard;
	}

	float charValue = float(instance.character-CHAR_START)/CHAR_COUNT;
	vec2 size = vec2(1.0/CHAR_COUNT, 1.0);
	vec4 mask = texture(
			txt1,
			vec2(
					vertex.uv.x*(1.0/CHAR_COUNT)+(float(instance.character-CHAR_START)/CHAR_COUNT),
					vertex.uv.y
			)
	);

	if(mask.a == 0) {
		discard;
	}

	fragColor = mix(bgColor, fgColor, mask.r);

}
