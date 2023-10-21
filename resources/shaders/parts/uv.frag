#version 330

in vec2 texCoord;
in vec3 normal;
in int index;

out vec4 fragColor;

void main()
{
	fragColor = vec4(texCoord*index, 0, 1);
}