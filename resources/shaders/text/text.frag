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

out vec4 fragColor;

uniform sampler2D txt1;
uniform vec4 bgColor;
uniform vec4 fgColor;
uniform int length;
uniform bool transparent;

void main() {
	if (instance.index >= length) {
		discard;
	}

	vec4 mask = texture(
			txt1,
			vec2(
					vertex.uv.x*(1.0/CHAR_COUNT)+(float(instance.character-CHAR_START)/CHAR_COUNT),
					vertex.uv.y
			)
	);

	if(mask.a == 0 && transparent) {
		discard;
	}

	if(mask.r >= 0.5f) {
		fragColor = fgColor;
	}else{
		fragColor = bgColor;
	}
}
