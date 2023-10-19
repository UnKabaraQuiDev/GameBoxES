#version 400 core

layout(points) in;
layout(triangle_strip, max_vertices=4) out;

flat in int VertexIndex[];

flat out int FragIndex;
out vec2 texCoord;

uniform vec2 size;
uniform int length;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void createOffset(vec3 offset, vec2 uv) {
	vec4 pos = gl_in[0].gl_Position + vec4(offset, 0);
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * pos;
	texCoord = uv;
	FragIndex = VertexIndex[0];
	EmitVertex();
}

void main() {
	vec2 size2 = vec2(0.2, 0.2);

	FragIndex = VertexIndex[0];

	if(FragIndex <= length) {
		vec4 pos = gl_in[0].gl_Position;

		createOffset(vec3(-size.x, -size.y, 0), vec2(0, 0));
		createOffset(vec3( size.x, -size.y, 0), vec2(1, 0));
		createOffset(vec3(-size.x,  size.y, 0), vec2(0, 1));
		createOffset(vec3( size.x,  size.y, 0), vec2(1, 1));

		EndPrimitive();
	}
}
