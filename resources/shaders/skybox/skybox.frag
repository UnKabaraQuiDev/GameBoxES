#version 400 core

in struct per_vertex {
	vec2 uv;
	vec3 normal;
	vec2 frag;
	vec3 pos;
} vertex;

out vec4 fragColor;

uniform samplerCube skybox;

void main() {
	fragColor = texture(skybox, vertex.pos);
}
