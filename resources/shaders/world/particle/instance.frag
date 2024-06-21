#version 300 es
precision mediump float;

in vec3 normal;
in vec2 texCoord;
in vec2 fragPos;
flat in int index;

out vec4 fragColor;

uniform sampler2D txt1;
uniform int columns; 
uniform int rows;
uniform float opacity; // 0.55f

void main() {
	int rowIndex = index % (columns*rows) / columns;
	int colIndex = index % (columns*rows) % columns;
	
	float colWidth = 1.0/float(columns);
	float rowHeight = 1.0/float(rows);
	
	fragColor = texture(txt1, mix(vec2(colWidth*colIndex, rowHeight*rowIndex), vec2(colWidth*(colIndex+1), rowHeight*(rowIndex+1)), texCoord)) - vec4(0, 0, 0, distance(texCoord, vec2(0.5f, 0.5f)))- vec4(0, 0, 0, 1-opacity);
}
