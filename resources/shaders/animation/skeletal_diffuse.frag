#version 400 core

in struct per_Vertex {
	vec3 worldpos;
	vec2 fragpos;
	vec2 uv;
	vec3 normal;
	vec3 joints;
	vec3 weights;
} vertex;

out vec4 fragColor;

uniform vec4 diffuseColor;

void main() {

	fragColor = vec4(1); // diffuseColor * vec4(vertex.weights, 1);

}
