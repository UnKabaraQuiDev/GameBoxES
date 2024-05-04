#version 400 core

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;
flat in int index;

out vec4 fragColor;

uniform sampler2D txt1;
uniform int columns; 
uniform int rows;

void main() {
	int rowIndex = index % (columns*rows) / columns;
	int colIndex = index % (columns*rows) % columns;
	
	fragColor = texture(txt1, mix(vec2(0.2*colIndex, 0.2*rowIndex), vec2(0.2*(colIndex+1), 0.2*(rowIndex+1)), texCoord)); // mix(vec2(widthStart, heightStart), vec2(widthEnd, heightEnd), texCoord)
}
