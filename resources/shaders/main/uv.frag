#version 330

in vec2 texCoord;
in vec3 normal;

out vec4 fragColor;

void main()
{
	fragColor = vec4(texCoord, texCoord.y > 0.25, texCoord.y > 0.25);
}