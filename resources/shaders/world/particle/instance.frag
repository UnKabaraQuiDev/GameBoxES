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
	
	float colWidth = 1f/float(columns);
	float rowHeight = 1f/float(rows);
	
	fragColor = texture(txt1, mix(vec2(colWidth*colIndex, rowHeight*rowIndex), vec2(colWidth*(colIndex+1), rowHeight*(rowIndex+1)), texCoord))-vec4(0, 0, 0, length(texCoord-vec2(0.5))*2);
}
