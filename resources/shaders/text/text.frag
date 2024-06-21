#version 300 es
precision mediump float;

flat in struct per_instance {
	int index;
	uint character;
} instance;

in struct per_Vertex {
	vec3 worldpos;
	vec2 fragpos;
	vec2 uv;
	vec3 normal;
} vertex;

#define CHAR_START 32f
#define CHAR_COUNT 95f //255

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
					vertex.uv*vec2(
					1.0/95.0+((float(instance.character)-32.0)/95.0),
					1)
			)
	);

	if(mask.a == 0.0 && transparent) {
		discard;
	}

	if(mask.r >= 0.5f) {
		fragColor = fgColor;
	}else{
		fragColor = bgColor;
	}
}
