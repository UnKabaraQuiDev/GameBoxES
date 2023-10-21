#version 400 core

flat in int QuadIndex;
in vec2 texCoord;

out vec4 fragColor;

#define MAX_BUFFER 64
uniform int buffer[MAX_BUFFER];

#define W 30
#define H 30
#define ROWS 5
#define COLUMNS 19
#define ASCII 255.0
uniform sampler2D raster;
uniform sampler2D rasterIndex;

uniform vec4 fillColor;
uniform int txtLength;
uniform float size;

void main() {
	vec2 index = texture(rasterIndex, vec2(float(buffer[QuadIndex]) /255.0, 0.5)).rg;

	vec2 quadUV = index + texCoord * vec2(1.0 / 19.0, 1.0 / 5.0);
	fragColor = vec4(texture(raster, quadUV).rgb, 1);
	if(QuadIndex >= txtLength) {
		fragColor = vec4(quadUV, 0, 1);
	}
	fragColor = texture(raster, texCoord);
	
	
	vec2 QuadUV = texCoord;
	ivec2 gridSize = ivec2(19, 5);
	
	// Calculate the grid position based on the ASCII value
    ivec2 gridPos = ivec2(QuadIndex % gridSize.x, QuadIndex / gridSize.x);
    
    // Calculate the size of each cell in the grid
    vec2 cellSize = 1.0 / vec2(gridSize);
    
    // Calculate the UV offset for the current cell
    vec2 cellOffset = vec2(gridPos);// * cellSize;
    
    // Calculate the final UV coordinates by adding the cell offset to the QuadUV
    vec2 finalUV = cellOffset + QuadUV;
    
    // Sample the font texture using the final UV coordinates
    vec4 fontColor = texture(raster, finalUV.xy*size);
    
    // Output the color
    fragColor = new vec4(fontColor.rgb, 1);
}