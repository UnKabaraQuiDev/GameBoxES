#version 400 core

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;
flat in int index;
flat in int state;

out vec4 fragColor;

uniform sampler2D txt1;
uniform int columns; 

void main() {
	int colIndex = index % columns;
	
	float colWidth = (1.0f/1.0f);
	
	fragColor = texture(txt1, mix(vec2(colWidth*colIndex, 0), vec2(colWidth*(colIndex+1), 1), texCoord));
}
